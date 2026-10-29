package com.example.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.BibleSeedData
import com.example.data.ProfileStorageManager
import com.example.data.ReadingLayoutMode
import kotlinx.coroutines.launch
import com.example.ui.components.ArchitectureReferenceSheet
import com.example.ui.components.BilingualToolbarRow
import com.example.ui.components.BookPickerBottomSheet
import com.example.ui.components.BottomBookChapterFooter
import com.example.ui.components.ChapterPickerBottomSheet
import com.example.ui.components.DailyVersePopupDialog
import com.example.ui.components.DwibibleTopBar
import com.example.ui.components.OfflineBibleQuizDialog
import com.example.ui.components.SavedBookmarksBottomSheet
import com.example.ui.components.SingleColumnReaderView
import com.example.ui.components.SmartSearchSection
import com.example.ui.components.SplitScreenReaderView
import com.example.ui.components.VerseComparisonDialog
import com.example.ui.components.VerseStudyBottomSheet
import com.example.ui.theme.DwibibleTheme
import com.example.ui.theme.MidnightBackground

@Composable
fun DwibibleAppScreen(
    viewModel: DwibibleViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val controls = uiState.controls
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var profileAvatarBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var readerName by remember { mutableStateOf(ProfileStorageManager.getReaderName(context)) }

    // Load saved custom profile photo from local app storage on launch
    LaunchedEffect(Unit) {
        profileAvatarBitmap = ProfileStorageManager.loadSavedProfileBitmap(context)
    }

    val bmListState = rememberLazyListState()
    val enListState = rememberLazyListState()

    // Scroll back to top when Book or Chapter changes
    LaunchedEffect(controls.selectedBook.bookNumber, controls.selectedChapter) {
        bmListState.scrollToItem(0)
        enListState.scrollToItem(0)
    }

    LaunchedEffect(controls.statusMessage) {
        controls.statusMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearStatusMessage()
        }
    }

    BackHandler(
        enabled = controls.isVersionMenuExpanded ||
            controls.searchQuery.isNotEmpty() ||
            controls.layoutMode == ReadingLayoutMode.SINGLE_COLUMN
    ) {
        when {
            controls.isVersionMenuExpanded -> viewModel.dismissVersionMenu()
            controls.searchQuery.isNotEmpty() -> viewModel.clearSearch()
            controls.layoutMode == ReadingLayoutMode.SINGLE_COLUMN -> viewModel.toggleLayoutMode()
        }
    }

    DwibibleTheme(isDarkMode = controls.isDarkMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MidnightBackground)
        ) {
            // 1. Top App Bar (includes Daily Verse Sun Icon, Offline Bible Quiz & Custom Profile Avatar)
            DwibibleTopBar(
                savedBookmarksCount = uiState.allSavedStudies.size,
                profileAvatarBitmap = profileAvatarBitmap,
                onResetToGenesis = {
                    viewModel.selectEnglishVersion(com.example.data.EnglishVersion.NKJV)
                    viewModel.selectBook(BibleSeedData.allBooks.first())
                },
                onOpenDailyVerse = viewModel::openDailyVersePopup,
                onOpenBibleQuiz = viewModel::openBibleQuiz,
                onOpenBookmarks = { viewModel.setShowBookmarksSheet(true) },
                onOpenArchitectureInfo = { viewModel.setShowArchitectureSheet(true) }
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 2. Smart Search Bar ("SEARCH VERSE" + rounded input with mic/quick-presets)
            SmartSearchSection(
                searchQuery = controls.searchQuery,
                searchResults = controls.searchResults,
                englishVersion = controls.englishVersion,
                onSearchQueryChanged = viewModel::onSearchQueryChanged,
                onClearSearch = viewModel::clearSearch,
                onSelectSearchResult = viewModel::jumpToVerseFromSearch
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 3. Bilingual Column Emblems (BM | Font Resizer A-/A+, Day/Dark Mode & Layout Toggle | EN)
            BilingualToolbarRow(
                fontSizeSp = controls.fontSizeSp,
                isDarkMode = controls.isDarkMode,
                layoutMode = controls.layoutMode,
                onDecreaseFont = viewModel::decreaseFontSize,
                onIncreaseFont = viewModel::increaseFontSize,
                onToggleThemeMode = viewModel::toggleThemeMode,
                onToggleLayoutMode = viewModel::toggleLayoutMode
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 4. Main Content Area (Split-Screen Side-by-Side or Single-Column)
            if (controls.layoutMode == ReadingLayoutMode.SPLIT_SCREEN) {
                SplitScreenReaderView(
                    verses = uiState.chapterVerses,
                    englishVersion = controls.englishVersion,
                    isVersionMenuExpanded = controls.isVersionMenuExpanded,
                    fontSizeSp = controls.fontSizeSp,
                    bmListState = bmListState,
                    enListState = enListState,
                    onToggleVersionMenu = viewModel::toggleVersionMenu,
                    onSelectEnglishVersion = viewModel::selectEnglishVersion,
                    onVerseClick = { verseWithStudy ->
                        viewModel.openVerseStudySheet(verseWithStudy.verse.verseKey)
                    },
                    modifier = Modifier.weight(1f)
                )
            } else {
                SingleColumnReaderView(
                    verses = uiState.chapterVerses,
                    englishVersion = controls.englishVersion,
                    fontSizeSp = controls.fontSizeSp,
                    onSelectEnglishVersion = viewModel::selectEnglishVersion,
                    onVerseClick = { verseWithStudy ->
                        viewModel.openVerseStudySheet(verseWithStudy.verse.verseKey)
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 5. Bottom Navigation (Book & Chapter Pickers) + Offline Mode Footer
            BottomBookChapterFooter(
                selectedBook = controls.selectedBook,
                selectedChapter = controls.selectedChapter,
                onOpenBookPicker = { viewModel.setShowBookPicker(true) },
                onOpenChapterPicker = { viewModel.setShowChapterPicker(true) }
            )
        }

        // =========================================================================
        // Offline Daily Verse Popup (Calculated from Local Device Date)
        // =========================================================================
        if (controls.showDailyVersePopup) {
            controls.dailyVerseInfo?.let { dailyInfo ->
                DailyVersePopupDialog(
                    dailyVerseInfo = dailyInfo,
                    englishVersion = controls.englishVersion,
                    onShiftDate = viewModel::shiftDailyVerseDate,
                    onReadFullChapter = viewModel::jumpToDailyVerseChapter,
                    onBookmarkDailyVerse = viewModel::bookmarkDailyVerse,
                    onDismiss = viewModel::dismissDailyVersePopup
                )
            }
        }

        // =========================================================================
        // Quick Verse Comparison Modal (Side-by-Side: Malay, KJV, NKJV)
        // =========================================================================
        controls.comparisonVerse?.let { compVerse ->
            VerseComparisonDialog(
                verse = compVerse,
                onReadFullChapter = { verse ->
                    viewModel.dismissVerseComparison()
                    viewModel.jumpToDailyVerseChapter(verse)
                },
                onDismiss = viewModel::dismissVerseComparison
            )
        }

        // =========================================================================
        // Offline Bible Mini-Quiz (Trivia) Modal
        // =========================================================================
        if (controls.showQuizDialog) {
            OfflineBibleQuizDialog(
                question = controls.currentQuizQuestion,
                selectedOptionIndex = controls.quizSelectedIndex,
                score = controls.quizScore,
                totalAnswered = controls.quizTotalAnswered,
                streak = controls.quizStreak,
                onSelectOption = viewModel::selectQuizAnswer,
                onNextQuestion = viewModel::loadNextQuizQuestion,
                onResetStats = viewModel::resetQuizStats,
                onCompareSourceVerse = { verse ->
                    viewModel.dismissBibleQuiz()
                    viewModel.openVerseComparison(verse)
                },
                onReadSourceChapter = { verse ->
                    viewModel.dismissBibleQuiz()
                    viewModel.jumpToDailyVerseChapter(verse)
                },
                onDismiss = viewModel::dismissBibleQuiz
            )
        }

        // =========================================================================
        // Interactive Modal Bottom Sheets
        // =========================================================================
        uiState.activeStudyVerse?.let { activeVerse ->
            VerseStudyBottomSheet(
                verseWithStudy = activeVerse,
                onOpenComparison = viewModel::openVerseComparison,
                onDismiss = viewModel::dismissVerseStudySheet,
                onSave = viewModel::saveVerseStudy
            )
        }

        if (controls.showBookPicker) {
            BookPickerBottomSheet(
                selectedBook = controls.selectedBook,
                onSelectBook = viewModel::selectBook,
                onDismiss = { viewModel.setShowBookPicker(false) }
            )
        }

        if (controls.showChapterPicker) {
            ChapterPickerBottomSheet(
                selectedBook = controls.selectedBook,
                selectedChapter = controls.selectedChapter,
                onSelectChapter = viewModel::selectChapter,
                onDismiss = { viewModel.setShowChapterPicker(false) }
            )
        }

        if (controls.showBookmarksSheet) {
            SavedBookmarksBottomSheet(
                savedStudies = uiState.allSavedStudies,
                onJumpToStudy = viewModel::jumpToSavedStudy,
                onCompareStudy = viewModel::openVerseComparisonFromStudy,
                onDeleteStudy = viewModel::deleteSavedStudy,
                onDismiss = { viewModel.setShowBookmarksSheet(false) }
            )
        }

        if (controls.showArchitectureSheet) {
            ArchitectureReferenceSheet(
                profileAvatarBitmap = profileAvatarBitmap,
                readerName = readerName,
                savedStudiesCount = uiState.allSavedStudies.size,
                isDarkMode = controls.isDarkMode,
                englishVersion = controls.englishVersion,
                onPickProfilePhotoUri = { uri ->
                    coroutineScope.launch {
                        val savedBitmap = ProfileStorageManager.saveProfilePhotoFromUri(context, uri)
                        if (savedBitmap != null) {
                            profileAvatarBitmap = savedBitmap
                            Toast.makeText(context, "Profile picture saved offline!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Could not load selected image", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onRemoveProfilePhoto = {
                    coroutineScope.launch {
                        ProfileStorageManager.clearProfilePhoto(context)
                        profileAvatarBitmap = null
                        Toast.makeText(context, "Profile picture removed", Toast.LENGTH_SHORT).show()
                    }
                },
                onSaveReaderName = { updatedName ->
                    ProfileStorageManager.saveReaderName(context, updatedName)
                    readerName = ProfileStorageManager.getReaderName(context)
                    Toast.makeText(context, "Profile name saved!", Toast.LENGTH_SHORT).show()
                },
                onToggleThemeMode = viewModel::toggleThemeMode,
                onSelectEnglishVersion = viewModel::selectEnglishVersion,
                onDismiss = { viewModel.setShowArchitectureSheet(false) }
            )
        }
    }
}
