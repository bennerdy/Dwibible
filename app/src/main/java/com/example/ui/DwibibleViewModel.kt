package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.BibleBookInfo
import com.example.data.BibleRepository
import com.example.data.BibleSeedData
import com.example.data.BibleVerseEntity
import com.example.data.DailyVerseInfo
import com.example.data.EnglishVersion
import com.example.data.HighlightTone
import com.example.data.QuizQuestion
import com.example.data.ReadingLayoutMode
import com.example.data.VerseStudyEntity
import com.example.data.VerseWithStudy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DwibibleControlState(
    val selectedBook: BibleBookInfo = BibleSeedData.allBooks.first(),
    val selectedChapter: Int = 1,
    val englishVersion: EnglishVersion = EnglishVersion.NKJV,
    val isVersionMenuExpanded: Boolean = false,
    val fontSizeSp: Float = 16f,
    val isDarkMode: Boolean = true,
    val layoutMode: ReadingLayoutMode = ReadingLayoutMode.SPLIT_SCREEN,
    val searchQuery: String = "",
    val searchResults: List<BibleVerseEntity> = emptyList(),
    val isSearching: Boolean = false,
    val activeVerseKeyForStudy: String? = null,
    val comparisonVerse: BibleVerseEntity? = null,
    val showBookPicker: Boolean = false,
    val showChapterPicker: Boolean = false,
    val showBookmarksSheet: Boolean = false,
    val showArchitectureSheet: Boolean = false,
    val showDailyVersePopup: Boolean = true,
    val dailyVerseInfo: DailyVerseInfo? = null,
    val showQuizDialog: Boolean = false,
    val currentQuizQuestion: QuizQuestion? = null,
    val quizSelectedIndex: Int? = null,
    val quizScore: Int = 0,
    val quizTotalAnswered: Int = 0,
    val quizStreak: Int = 0,
    val quizQuestionCounter: Int = 0,
    val statusMessage: String? = null
)

data class DwibibleUiState(
    val controls: DwibibleControlState = DwibibleControlState(),
    val chapterVerses: List<VerseWithStudy> = emptyList(),
    val allSavedStudies: List<VerseStudyEntity> = emptyList()
) {
    val activeStudyVerse: VerseWithStudy?
        get() = controls.activeVerseKeyForStudy?.let { key ->
            chapterVerses.find { it.verse.verseKey == key }
        }
}

@OptIn(ExperimentalCoroutinesApi::class)
class DwibibleViewModel(
    private val repository: BibleRepository
) : ViewModel() {

    private val _controls = MutableStateFlow(DwibibleControlState())
    val controls: StateFlow<DwibibleControlState> = _controls.asStateFlow()

    private var searchJob: Job? = null

    private val chapterVersesFlow = _controls
        .flatMapLatest { state ->
            repository.getChapterVersesWithStudy(
                bookNumber = state.selectedBook.bookNumber,
                chapter = state.selectedChapter
            )
        }

    val uiState: StateFlow<DwibibleUiState> = combine(
        _controls,
        chapterVersesFlow,
        repository.allStudyRecords
    ) { ctrl, verses, savedStudies ->
        DwibibleUiState(
            controls = ctrl,
            chapterVerses = verses,
            allSavedStudies = savedStudies
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DwibibleUiState()
    )

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            val todayVerse = repository.getDailyVerseByLocalDate(dayOffset = 0)
            _controls.update {
                it.copy(
                    dailyVerseInfo = todayVerse,
                    showDailyVersePopup = true
                )
            }
        }
    }

    fun openDailyVersePopup() {
        viewModelScope.launch {
            val currentOffset = _controls.value.dailyVerseInfo?.dayOffset ?: 0
            val info = repository.getDailyVerseByLocalDate(dayOffset = currentOffset)
            _controls.update {
                it.copy(
                    dailyVerseInfo = info,
                    showDailyVersePopup = true,
                    isVersionMenuExpanded = false
                )
            }
        }
    }

    fun dismissDailyVersePopup() {
        _controls.update { it.copy(showDailyVersePopup = false) }
    }

    fun shiftDailyVerseDate(deltaDays: Int) {
        viewModelScope.launch {
            val nextOffset = if (deltaDays == 0) {
                0
            } else {
                (_controls.value.dailyVerseInfo?.dayOffset ?: 0) + deltaDays
            }
            val updatedInfo = repository.getDailyVerseByLocalDate(dayOffset = nextOffset)
            _controls.update { it.copy(dailyVerseInfo = updatedInfo) }
        }
    }

    fun jumpToDailyVerseChapter(verse: BibleVerseEntity) {
        val book = BibleSeedData.getBookByNumber(verse.bookNumber)
        viewModelScope.launch {
            repository.ensureChapterPopulated(book, verse.chapter)
            _controls.update {
                it.copy(
                    selectedBook = book,
                    selectedChapter = verse.chapter,
                    showDailyVersePopup = false
                )
            }
        }
    }

    fun bookmarkDailyVerse(verse: BibleVerseEntity) {
        viewModelScope.launch {
            repository.saveVerseStudy(
                verse = verse,
                isBookmarked = true,
                highlightColor = HighlightTone.GOLD.key,
                noteText = "Daily Verse (${_controls.value.dailyVerseInfo?.formattedLocalDate ?: ""})"
            )
            _controls.update {
                it.copy(statusMessage = "Daily Verse saved to Offline Bookmarks")
            }
        }
    }

    fun selectEnglishVersion(version: EnglishVersion) {
        _controls.update {
            it.copy(
                englishVersion = version,
                isVersionMenuExpanded = false
            )
        }
    }

    fun toggleVersionMenu() {
        _controls.update { it.copy(isVersionMenuExpanded = !it.isVersionMenuExpanded) }
    }

    fun dismissVersionMenu() {
        _controls.update { it.copy(isVersionMenuExpanded = false) }
    }

    fun increaseFontSize() {
        _controls.update { state ->
            val next = (state.fontSizeSp + 2f).coerceAtMost(26f)
            state.copy(fontSizeSp = next)
        }
    }

    fun decreaseFontSize() {
        _controls.update { state ->
            val next = (state.fontSizeSp - 2f).coerceAtLeast(12f)
            state.copy(fontSizeSp = next)
        }
    }

    fun toggleThemeMode() {
        _controls.update { state ->
            state.copy(isDarkMode = !state.isDarkMode)
        }
    }

    fun toggleLayoutMode() {
        _controls.update { state ->
            val next = if (state.layoutMode == ReadingLayoutMode.SPLIT_SCREEN) {
                ReadingLayoutMode.SINGLE_COLUMN
            } else {
                ReadingLayoutMode.SPLIT_SCREEN
            }
            state.copy(layoutMode = next)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _controls.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        if (query.isBlank()) {
            _controls.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }
        searchJob = viewModelScope.launch {
            _controls.update { it.copy(isSearching = true) }
            delay(150)
            val results = repository.searchVerses(query)
            _controls.update {
                it.copy(
                    searchResults = results,
                    isSearching = false
                )
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _controls.update {
            it.copy(
                searchQuery = "",
                searchResults = emptyList(),
                isSearching = false
            )
        }
    }

    fun jumpToVerseFromSearch(verse: BibleVerseEntity) {
        val book = BibleSeedData.getBookByNumber(verse.bookNumber)
        _controls.update {
            it.copy(
                selectedBook = book,
                selectedChapter = verse.chapter,
                searchQuery = "",
                searchResults = emptyList(),
                isSearching = false
            )
        }
    }

    fun selectBook(book: BibleBookInfo) {
        viewModelScope.launch {
            repository.ensureChapterPopulated(book, 1)
            _controls.update {
                it.copy(
                    selectedBook = book,
                    selectedChapter = 1,
                    showBookPicker = false,
                    showChapterPicker = false
                )
            }
        }
    }

    fun selectChapter(chapter: Int) {
        val currentBook = _controls.value.selectedBook
        val safeChapter = chapter.coerceIn(1, currentBook.totalChapters)
        viewModelScope.launch {
            repository.ensureChapterPopulated(currentBook, safeChapter)
            _controls.update {
                it.copy(
                    selectedChapter = safeChapter,
                    showChapterPicker = false
                )
            }
        }
    }

    fun openVerseStudySheet(verseKey: String) {
        _controls.update {
            it.copy(
                activeVerseKeyForStudy = verseKey,
                isVersionMenuExpanded = false
            )
        }
    }

    fun dismissVerseStudySheet() {
        _controls.update { it.copy(activeVerseKeyForStudy = null) }
    }

    fun saveVerseStudy(
        verse: BibleVerseEntity,
        isBookmarked: Boolean,
        highlightColor: String,
        noteText: String
    ) {
        viewModelScope.launch {
            repository.saveVerseStudy(
                verse = verse,
                isBookmarked = isBookmarked,
                highlightColor = highlightColor,
                noteText = noteText
            )
            _controls.update {
                it.copy(
                    activeVerseKeyForStudy = null,
                    statusMessage = "Saved offline to SQLite"
                )
            }
        }
    }

    fun jumpToSavedStudy(study: VerseStudyEntity) {
        val book = BibleSeedData.getBookByNumber(study.bookNumber)
        viewModelScope.launch {
            repository.ensureChapterPopulated(book, study.chapter)
            _controls.update {
                it.copy(
                    selectedBook = book,
                    selectedChapter = study.chapter,
                    showBookmarksSheet = false
                )
            }
        }
    }

    fun deleteSavedStudy(verseKey: String) {
        viewModelScope.launch {
            repository.removeStudyRecord(verseKey)
        }
    }

    fun setShowBookPicker(show: Boolean) {
        _controls.update { it.copy(showBookPicker = show, isVersionMenuExpanded = false) }
    }

    fun setShowChapterPicker(show: Boolean) {
        _controls.update { it.copy(showChapterPicker = show, isVersionMenuExpanded = false) }
    }

    fun setShowBookmarksSheet(show: Boolean) {
        _controls.update { it.copy(showBookmarksSheet = show, isVersionMenuExpanded = false) }
    }

    fun setShowArchitectureSheet(show: Boolean) {
        _controls.update { it.copy(showArchitectureSheet = show, isVersionMenuExpanded = false) }
    }

    fun openVerseComparison(verse: BibleVerseEntity) {
        _controls.update {
            it.copy(
                comparisonVerse = verse,
                activeVerseKeyForStudy = null,
                isVersionMenuExpanded = false
            )
        }
    }

    fun openVerseComparisonFromStudy(study: VerseStudyEntity) {
        viewModelScope.launch {
            val verse = repository.getVerseForStudy(study)
            if (verse != null) {
                _controls.update {
                    it.copy(
                        comparisonVerse = verse,
                        showBookmarksSheet = false
                    )
                }
            }
        }
    }

    fun dismissVerseComparison() {
        _controls.update { it.copy(comparisonVerse = null) }
    }

    fun openBibleQuiz() {
        viewModelScope.launch {
            val currentQ = _controls.value.currentQuizQuestion
            if (currentQ == null) {
                val nextCounter = _controls.value.quizQuestionCounter + 1
                val question = repository.generateOfflineQuizQuestion(nextCounter)
                _controls.update {
                    it.copy(
                        showQuizDialog = true,
                        currentQuizQuestion = question,
                        quizSelectedIndex = null,
                        quizQuestionCounter = nextCounter,
                        isVersionMenuExpanded = false
                    )
                }
            } else {
                _controls.update {
                    it.copy(
                        showQuizDialog = true,
                        isVersionMenuExpanded = false
                    )
                }
            }
        }
    }

    fun dismissBibleQuiz() {
        _controls.update { it.copy(showQuizDialog = false) }
    }

    fun selectQuizAnswer(optionIndex: Int) {
        val currentState = _controls.value
        if (currentState.quizSelectedIndex != null) return
        val question = currentState.currentQuizQuestion ?: return
        val isCorrect = optionIndex == question.correctIndex
        _controls.update {
            it.copy(
                quizSelectedIndex = optionIndex,
                quizScore = if (isCorrect) it.quizScore + 1 else it.quizScore,
                quizTotalAnswered = it.quizTotalAnswered + 1,
                quizStreak = if (isCorrect) it.quizStreak + 1 else 0
            )
        }
    }

    fun loadNextQuizQuestion() {
        viewModelScope.launch {
            val nextCounter = _controls.value.quizQuestionCounter + 1
            val question = repository.generateOfflineQuizQuestion(nextCounter)
            _controls.update {
                it.copy(
                    currentQuizQuestion = question,
                    quizSelectedIndex = null,
                    quizQuestionCounter = nextCounter
                )
            }
        }
    }

    fun resetQuizStats() {
        viewModelScope.launch {
            val nextCounter = _controls.value.quizQuestionCounter + 1
            val question = repository.generateOfflineQuizQuestion(nextCounter)
            _controls.update {
                it.copy(
                    currentQuizQuestion = question,
                    quizSelectedIndex = null,
                    quizScore = 0,
                    quizTotalAnswered = 0,
                    quizStreak = 0,
                    quizQuestionCounter = nextCounter
                )
            }
        }
    }

    fun clearStatusMessage() {
        _controls.update { it.copy(statusMessage = null) }
    }

    class Factory(private val repository: BibleRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DwibibleViewModel(repository) as T
        }
    }
}
