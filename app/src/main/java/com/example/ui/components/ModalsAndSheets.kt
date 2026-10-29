package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.BibleBookInfo
import com.example.data.BibleSeedData
import com.example.data.BibleVerseEntity
import com.example.data.DailyVerseInfo
import com.example.data.EnglishVersion
import com.example.data.HighlightTone
import com.example.data.QuizQuestion
import com.example.data.VerseStudyEntity
import com.example.data.VerseWithStudy
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CelestialBlue
import com.example.ui.theme.CrimsonTriangle
import com.example.ui.theme.HighlightBlue
import com.example.ui.theme.HighlightEmerald
import com.example.ui.theme.HighlightGold
import com.example.ui.theme.HighlightRose
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.PopupSurface
import com.example.ui.theme.TextPrimaryOffWhite
import com.example.ui.theme.TextSecondaryMuted
import com.example.ui.theme.VerseReferenceGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerseStudyBottomSheet(
    verseWithStudy: VerseWithStudy,
    onOpenComparison: (BibleVerseEntity) -> Unit,
    onDismiss: () -> Unit,
    onSave: (BibleVerseEntity, Boolean, String, String) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isBookmarked by remember(verseWithStudy) {
        mutableStateOf(verseWithStudy.isBookmarked)
    }
    var selectedHighlight by remember(verseWithStudy) {
        mutableStateOf(verseWithStudy.highlightTone)
    }
    var noteText by remember(verseWithStudy) {
        mutableStateOf(verseWithStudy.study?.noteText ?: "")
    }
    var isSideBySideInline by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CardSurface,
        contentColor = TextPrimaryOffWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 8.dp)
                .padding(bottom = 24.dp)
                .testTag("verse_study_bottom_sheet")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${verseWithStudy.verse.bookNameBm} / ${verseWithStudy.verse.bookNameEn} ${verseWithStudy.verse.chapter}:${verseWithStudy.verse.verse}",
                        color = VerseReferenceGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Quick Version Compare • Color Highlight & Offline Note",
                        color = TextSecondaryMuted,
                        fontSize = 11.5.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clipText = buildString {
                                append("${verseWithStudy.verse.bookNameBm} ${verseWithStudy.verse.chapter}:${verseWithStudy.verse.verse} (BM)\n")
                                append(verseWithStudy.verse.textBm)
                                append("\n\n${verseWithStudy.verse.bookNameEn} ${verseWithStudy.verse.chapter}:${verseWithStudy.verse.verse} (NKJV)\n")
                                append(verseWithStudy.verse.textNkjv)
                                append("\n\n${verseWithStudy.verse.bookNameEn} ${verseWithStudy.verse.chapter}:${verseWithStudy.verse.verse} (KJV)\n")
                                append(verseWithStudy.verse.textKjv)
                            }
                            clipboard.setPrimaryClip(ClipData.newPlainText("Dwibible Verse", clipText))
                            Toast.makeText(context, "All 3 versions copied!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy all versions",
                            tint = TextSecondaryMuted
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondaryMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Verse Comparison Header + Button to open Full Side-by-Side Comparison Modal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { isSideBySideInline = !isSideBySideInline }
                        .padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ViewColumn,
                        contentDescription = null,
                        tint = CelestialBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSideBySideInline) "Side-by-Side Compare (BM • KJV • NKJV)" else "Stacked Compare (BM • KJV • NKJV)",
                        color = CelestialBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Surface(
                    color = CelestialBlue.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, CelestialBlue),
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onOpenComparison(verseWithStudy.verse) }
                        .testTag("open_quick_comparison_modal_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CompareArrows,
                            contentDescription = "Compare Modal",
                            tint = CelestialBlue,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Compare Modal",
                            color = TextPrimaryOffWhite,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Compact 3-Version Comparison View (Side-by-Side 3 Columns or Stacked)
            if (isSideBySideInline) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    VersionCompareColumnCard(
                        versionBadge = "MALAY (BM)",
                        badgeColor = CelestialBlue,
                        verseText = verseWithStudy.verse.textBm,
                        modifier = Modifier.weight(1f)
                    )
                    VersionCompareColumnCard(
                        versionBadge = "KJV",
                        badgeColor = VerseReferenceGold,
                        verseText = verseWithStudy.verse.textKjv,
                        modifier = Modifier.weight(1f)
                    )
                    VersionCompareColumnCard(
                        versionBadge = "NKJV",
                        badgeColor = CrimsonTriangle,
                        verseText = verseWithStudy.verse.textNkjv,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Surface(
                    color = PopupSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "BM: ${verseWithStudy.verse.textBm}",
                            color = TextPrimaryOffWhite,
                            fontSize = 13.5.sp,
                            lineHeight = 19.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        HorizontalDivider(color = CardBorder)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "NKJV: ${verseWithStudy.verse.textNkjv}",
                            color = TextPrimaryOffWhite.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "KJV: ${verseWithStudy.verse.textKjv}",
                            color = TextSecondaryMuted,
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Color-Coded Highlighting Section (Yellow, Green, Blue, Rose, None)
            Text(
                text = "COLOR-CODED HIGHLIGHT",
                color = TextSecondaryMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 0.6.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                HighlightTone.entries.forEach { tone ->
                    val swatchColor = when (tone) {
                        HighlightTone.NONE -> PopupSurface
                        HighlightTone.GOLD -> Color(0xFFFACC15) // Vivid Yellow
                        HighlightTone.EMERALD -> Color(0xFF22C55E) // Vivid Green
                        HighlightTone.BLUE -> Color(0xFF38BDF8) // Vivid Blue
                        HighlightTone.ROSE -> Color(0xFFF43F5E) // Vivid Rose
                    }
                    val bgChipColor = when (tone) {
                        HighlightTone.NONE -> PopupSurface
                        HighlightTone.GOLD -> HighlightGold
                        HighlightTone.EMERALD -> HighlightEmerald
                        HighlightTone.BLUE -> HighlightBlue
                        HighlightTone.ROSE -> HighlightRose
                    }
                    val isSelected = selectedHighlight == tone
                    Surface(
                        color = bgChipColor,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) VerseReferenceGold else CardBorder
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedHighlight = tone }
                            .testTag("highlight_chip_${tone.key.lowercase()}")
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(swatchColor)
                                    .border(1.dp, CardBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (tone == HighlightTone.NONE) {
                                    Text("×", color = TextSecondaryMuted, fontSize = 10.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tone.label,
                                color = TextPrimaryOffWhite,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bookmark Toggle Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { isBookmarked = !isBookmarked },
                    border = BorderStroke(1.dp, if (isBookmarked) CelestialBlue else CardBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isBookmarked) CelestialBlue.copy(alpha = 0.18f) else Color.Transparent,
                        contentColor = if (isBookmarked) CelestialBlue else TextPrimaryOffWhite
                    ),
                    modifier = Modifier.testTag("toggle_bookmark_button")
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBookmarked) stringResource(R.string.bookmarked) else stringResource(R.string.add_bookmark),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (selectedHighlight != HighlightTone.NONE) {
                    Text(
                        text = "Highlight: ${selectedHighlight.label}",
                        color = VerseReferenceGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Personal Study Note (Stored Offline in SQLite)") },
                placeholder = {
                    Text(
                        text = stringResource(R.string.personal_note_hint),
                        color = TextSecondaryMuted,
                        fontSize = 13.sp
                    )
                },
                minLines = 3,
                maxLines = 6,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CelestialBlue,
                    unfocusedBorderColor = CardBorder,
                    focusedTextColor = TextPrimaryOffWhite,
                    unfocusedTextColor = TextPrimaryOffWhite,
                    focusedLabelColor = CelestialBlue,
                    unfocusedLabelColor = TextSecondaryMuted
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("verse_note_input")
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val autoBookmark = isBookmarked || noteText.isNotBlank() || selectedHighlight != HighlightTone.NONE
                    onSave(
                        verseWithStudy.verse,
                        autoBookmark,
                        selectedHighlight.key,
                        noteText
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CelestialBlue,
                    contentColor = MidnightBackground
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_study_note_button")
            ) {
                Text(
                    text = stringResource(R.string.save_note),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun VersionCompareColumnCard(
    versionBadge: String,
    badgeColor: Color,
    verseText: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = PopupSurface,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, CardBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = versionBadge,
                color = badgeColor,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                letterSpacing = 0.4.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = verseText,
                color = TextPrimaryOffWhite,
                fontSize = 12.sp,
                lineHeight = 16.5.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookPickerBottomSheet(
    selectedBook: BibleBookInfo,
    onSelectBook: (BibleBookInfo) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var testamentFilter by remember { mutableStateOf("ALL") } // ALL, OT, NT
    var bookSearch by remember { mutableStateOf("") }

    val filteredBooks = remember(testamentFilter, bookSearch) {
        BibleSeedData.allBooks.filter { book ->
            val matchesTestament = testamentFilter == "ALL" || book.testament == testamentFilter
            val matchesQuery = bookSearch.isBlank() ||
                book.nameEn.contains(bookSearch, ignoreCase = true) ||
                book.nameBm.contains(bookSearch, ignoreCase = true)
            matchesTestament && matchesQuery
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CardSurface,
        contentColor = TextPrimaryOffWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 560.dp)
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp)
                .testTag("book_picker_sheet")
        ) {
            Text(
                text = "Select Bible Book (66 Books Offline)",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimaryOffWhite
            )
            Text(
                text = "Perjanjian Lama (39) & Perjanjian Baru (27)",
                fontSize = 12.sp,
                color = TextSecondaryMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = bookSearch,
                onValueChange = { bookSearch = it },
                placeholder = { Text("Filter book: e.g., Kejadian, Psalms, John…", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondaryMuted)
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CelestialBlue,
                    unfocusedBorderColor = CardBorder,
                    focusedTextColor = TextPrimaryOffWhite,
                    unfocusedTextColor = TextPrimaryOffWhite
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    "ALL" to "All (66)",
                    "OT" to "Old Testament (39)",
                    "NT" to "New Testament (27)"
                ).forEach { (key, label) ->
                    FilterChip(
                        selected = testamentFilter == key,
                        onClick = { testamentFilter = key },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CelestialBlue,
                            selectedLabelColor = MidnightBackground,
                            containerColor = PopupSurface,
                            labelColor = TextPrimaryOffWhite
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredBooks, key = { it.bookNumber }) { book ->
                    val isSelected = book.bookNumber == selectedBook.bookNumber
                    Surface(
                        color = if (isSelected) CelestialBlue.copy(alpha = 0.2f) else PopupSurface,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, if (isSelected) CelestialBlue else CardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectBook(book) }
                            .testTag("book_item_${book.nameEn.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${book.bookNumber}. ${book.nameEn} (${book.nameBm})",
                                    color = if (isSelected) CelestialBlue else TextPrimaryOffWhite,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.5.sp
                                )
                                Text(
                                    text = if (book.testament == "OT") "Old Testament • Perjanjian Lama" else "New Testament • Perjanjian Baru",
                                    color = TextSecondaryMuted,
                                    fontSize = 11.5.sp
                                )
                            }
                            Text(
                                text = "${book.totalChapters} ch",
                                color = CelestialBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterPickerBottomSheet(
    selectedBook: BibleBookInfo,
    selectedChapter: Int,
    onSelectChapter: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val chapters = remember(selectedBook) { (1..selectedBook.totalChapters).toList() }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CardSurface,
        contentColor = TextPrimaryOffWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
                .testTag("chapter_picker_sheet")
        ) {
            Text(
                text = "${selectedBook.nameEn} (${selectedBook.nameBm}) — Select Chapter",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimaryOffWhite
            )
            Text(
                text = "Total ${selectedBook.totalChapters} Chapters available offline",
                fontSize = 12.sp,
                color = TextSecondaryMuted
            )

            Spacer(modifier = Modifier.height(14.dp))

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 54.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(chapters, key = { it }) { ch ->
                    val isSelected = ch == selectedChapter
                    Surface(
                        color = if (isSelected) CelestialBlue else PopupSurface,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, if (isSelected) CelestialBlue else CardBorder),
                        modifier = Modifier
                            .height(48.dp)
                            .clickable { onSelectChapter(ch) }
                            .testTag("chapter_cell_$ch")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = ch.toString(),
                                color = if (isSelected) MidnightBackground else TextPrimaryOffWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedBookmarksBottomSheet(
    savedStudies: List<VerseStudyEntity>,
    onJumpToStudy: (VerseStudyEntity) -> Unit,
    onCompareStudy: (VerseStudyEntity) -> Unit,
    onDeleteStudy: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var activeFilter by remember { mutableStateOf("ALL") } // ALL, HIGHLIGHTS, NOTES, BOOKMARKS

    val filteredStudies = remember(savedStudies, activeFilter) {
        when (activeFilter) {
            "HIGHLIGHTS" -> savedStudies.filter { it.highlightColor != HighlightTone.NONE.key }
            "NOTES" -> savedStudies.filter { it.noteText.isNotBlank() }
            "BOOKMARKS" -> savedStudies.filter { it.isBookmarked }
            else -> savedStudies
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CardSurface,
        contentColor = TextPrimaryOffWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 560.dp)
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
                .testTag("saved_bookmarks_sheet")
        ) {
            Text(
                text = "Saved Bookmarks, Highlights & Notes",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimaryOffWhite
            )
            Text(
                text = "Stored 100% offline in local SQLite • Tap any item to jump or compare",
                fontSize = 12.sp,
                color = TextSecondaryMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Tabs: All, Highlights, Notes, Bookmarks
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(
                    "ALL" to "All (${savedStudies.size})",
                    "HIGHLIGHTS" to "Highlights (${savedStudies.count { it.highlightColor != HighlightTone.NONE.key }})",
                    "NOTES" to "Notes (${savedStudies.count { it.noteText.isNotBlank() }})"
                ).forEach { (key, label) ->
                    FilterChip(
                        selected = activeFilter == key,
                        onClick = { activeFilter = key },
                        label = { Text(label, fontSize = 11.5.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CelestialBlue,
                            selectedLabelColor = MidnightBackground,
                            containerColor = PopupSurface,
                            labelColor = TextPrimaryOffWhite
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (filteredStudies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No saved items in this filter yet.\nTap any verse in the reader to highlight (Yellow, Green, Blue, Rose) or add a personal note!",
                        color = TextSecondaryMuted,
                        fontSize = 13.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredStudies, key = { it.verseKey }) { study ->
                        val tone = HighlightTone.entries.find { it.key == study.highlightColor } ?: HighlightTone.NONE
                        val dotColor = when (tone) {
                            HighlightTone.GOLD -> Color(0xFFFACC15)
                            HighlightTone.EMERALD -> Color(0xFF22C55E)
                            HighlightTone.BLUE -> Color(0xFF38BDF8)
                            HighlightTone.ROSE -> Color(0xFFF43F5E)
                            HighlightTone.NONE -> CelestialBlue
                        }
                        Card(
                            colors = CardDefaults.cardColors(containerColor = PopupSurface),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, CardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onJumpToStudy(study) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "${study.bookNameBm} / ${study.bookNameEn} ${study.chapter}:${study.verse}",
                                            color = VerseReferenceGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        if (tone != HighlightTone.NONE) {
                                            Surface(
                                                color = dotColor.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(6.dp),
                                                border = BorderStroke(1.dp, dotColor.copy(alpha = 0.7f))
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(8.dp)
                                                            .clip(CircleShape)
                                                            .background(dotColor)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = tone.label,
                                                        color = TextPrimaryOffWhite,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    if (study.noteText.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(
                                            text = study.noteText,
                                            color = TextPrimaryOffWhite,
                                            fontSize = 13.sp,
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "Tap to open chapter • Compare all 3 versions",
                                            color = TextSecondaryMuted,
                                            fontSize = 11.5.sp
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onCompareStudy(study) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CompareArrows,
                                            contentDescription = "Compare 3 versions",
                                            tint = CelestialBlue
                                        )
                                    }
                                    IconButton(
                                        onClick = { onDeleteStudy(study.verseKey) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = "Delete bookmark",
                                            tint = CrimsonTriangle
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchitectureReferenceSheet(
    profileAvatarBitmap: ImageBitmap? = null,
    readerName: String = "Dwibible Reader",
    savedStudiesCount: Int = 0,
    isDarkMode: Boolean = true,
    englishVersion: EnglishVersion = EnglishVersion.NKJV,
    onPickProfilePhotoUri: (Uri) -> Unit = {},
    onRemoveProfilePhoto: () -> Unit = {},
    onSaveReaderName: (String) -> Unit = {},
    onToggleThemeMode: () -> Unit = {},
    onSelectEnglishVersion: (EnglishVersion) -> Unit = {},
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var nameDraft by remember(readerName) { mutableStateOf(readerName) }
    var showSqlSchema by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onPickProfilePhotoUri(uri)
        }
    }

    val sqlSchemaSnippet = """
-- 1. Bilingual Scripture Table (SQLite / Room / sqflite)
CREATE TABLE bible_verses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    bookNumber INTEGER NOT NULL,
    bookNameEn TEXT NOT NULL,
    bookNameBm TEXT NOT NULL,
    testament TEXT NOT NULL, -- 'OT' or 'NT'
    chapter INTEGER NOT NULL,
    verse INTEGER NOT NULL,
    textBm TEXT NOT NULL,    -- Alkitab Bahasa Melayu
    textKjv TEXT NOT NULL,   -- King James Version
    textNkjv TEXT NOT NULL   -- New King James Version
);
CREATE UNIQUE INDEX idx_book_ch_v ON bible_verses(bookNumber, chapter, verse);

-- 2. Offline Bookmarks & Personal Notes Table
CREATE TABLE verse_study (
    verseKey TEXT PRIMARY KEY, -- e.g. '1_1_1'
    bookNumber INTEGER NOT NULL,
    bookNameEn TEXT NOT NULL,
    bookNameBm TEXT NOT NULL,
    chapter INTEGER NOT NULL,
    verse INTEGER NOT NULL,
    isBookmarked INTEGER NOT NULL DEFAULT 0,
    highlightColor TEXT NOT NULL DEFAULT 'NONE',
    noteText TEXT NOT NULL DEFAULT '',
    updatedAt INTEGER NOT NULL
);
    """.trimIndent()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CardSurface,
        contentColor = TextPrimaryOffWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 8.dp)
                .padding(bottom = 28.dp)
                .testTag("architecture_reference_sheet"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Reader Profile & Offline Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimaryOffWhite
                    )
                    Text(
                        text = "Custom Profile Photo & Preferences (Saved Offline)",
                        fontSize = 12.sp,
                        color = TextSecondaryMuted
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Profile Sheet",
                        tint = TextSecondaryMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Avatar Circle + Camera Badge
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                    .testTag("profile_avatar_picker_box")
            ) {
                if (profileAvatarBitmap != null) {
                    Image(
                        bitmap = profileAvatarBitmap,
                        contentDescription = "Selected Profile Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .border(2.5.dp, VerseReferenceGold, CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(PopupSurface)
                            .border(2.dp, CelestialBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Default Profile Avatar",
                            tint = CelestialBlue,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(CelestialBlue)
                        .border(1.5.dp, CardSurface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Change Profile Photo",
                        tint = MidnightBackground,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Upload / Change & Remove Photo Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CelestialBlue,
                        contentColor = MidnightBackground
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("upload_profile_photo_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (profileAvatarBitmap != null) "Change Photo" else "Select Photo from Gallery",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                if (profileAvatarBitmap != null) {
                    OutlinedButton(
                        onClick = onRemoveProfilePhoto,
                        border = BorderStroke(1.dp, CrimsonTriangle),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonTriangle),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("remove_profile_photo_button")
                    ) {
                        Text(
                            text = "Remove",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reader Display Name Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nameDraft,
                    onValueChange = { nameDraft = it },
                    label = { Text("Reader Display Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CelestialBlue,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimaryOffWhite,
                        unfocusedTextColor = TextPrimaryOffWhite,
                        focusedLabelColor = CelestialBlue,
                        unfocusedLabelColor = TextSecondaryMuted
                    ),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { onSaveReaderName(nameDraft) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PopupSurface,
                        contentColor = VerseReferenceGold
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(52.dp)
                ) {
                    Text("Save", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Offline Settings Card
            Surface(
                color = PopupSurface,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reading Theme",
                            color = TextPrimaryOffWhite,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.5.sp
                        )
                        Surface(
                            color = CelestialBlue.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, CelestialBlue),
                            modifier = Modifier.clickable { onToggleThemeMode() }
                        ) {
                            Text(
                                text = if (isDarkMode) "Dark Mode (Tap for Day)" else "Day Mode (Tap for Dark)",
                                color = CelestialBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = CardBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "English Bible Version",
                            color = TextPrimaryOffWhite,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.5.sp
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            EnglishVersion.entries.forEach { ver ->
                                val selected = ver == englishVersion
                                Surface(
                                    color = if (selected) CelestialBlue else MidnightBackground,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, if (selected) CelestialBlue else CardBorder),
                                    modifier = Modifier.clickable { onSelectEnglishVersion(ver) }
                                ) {
                                    Text(
                                        text = ver.code,
                                        color = if (selected) MidnightBackground else TextPrimaryOffWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = CardBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Saved Offline Highlights & Notes",
                            color = TextPrimaryOffWhite,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.5.sp
                        )
                        Text(
                            text = "$savedStudiesCount saved",
                            color = VerseReferenceGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Expandable SQLite Database Architecture Reference
            Text(
                text = if (showSqlSchema) "Hide Offline SQLite Database Schema ▲" else "View Offline SQLite Database Schema ▼",
                color = CelestialBlue,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.5.sp,
                modifier = Modifier
                    .clickable { showSqlSchema = !showSqlSchema }
                    .padding(vertical = 4.dp)
            )

            if (showSqlSchema) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MidnightBackground,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = sqlSchemaSnippet,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp,
                        color = TextPrimaryOffWhite,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DailyVersePopupDialog(
    dailyVerseInfo: DailyVerseInfo,
    englishVersion: EnglishVersion = EnglishVersion.NKJV,
    onShiftDate: (Int) -> Unit,
    onReadFullChapter: (BibleVerseEntity) -> Unit,
    onBookmarkDailyVerse: (BibleVerseEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val verse = dailyVerseInfo.verse
    var popupEnglishVersion by remember { mutableStateOf(EnglishVersion.NKJV) }
    val enText = if (popupEnglishVersion == EnglishVersion.KJV) verse.textKjv else verse.textNkjv

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.5.dp, CelestialBlue.copy(alpha = 0.65f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .testTag("daily_verse_popup_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {
                // Header Row: Sun Emblem + Daily Verse Title + Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(CelestialBlue.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = null,
                                tint = CelestialBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.daily_verse_title),
                                color = TextPrimaryOffWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = stringResource(R.string.daily_verse_subtitle),
                                color = TextSecondaryMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("close_daily_verse_popup_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Daily Verse Popup",
                            tint = TextSecondaryMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Local Device Date Bar with Previous / Today / Next Day Controls
                Surface(
                    color = MidnightBackground,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onShiftDate(-1) },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("daily_verse_prev_day_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Previous day verse",
                                tint = CelestialBlue
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { onShiftDate(0) }
                                .padding(horizontal = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = CelestialBlue,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = dailyVerseInfo.formattedLocalDate,
                                    color = CelestialBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.5.sp
                                )
                            }
                            Text(
                                text = if (dailyVerseInfo.dayOffset == 0) {
                                    "Today (Day ${dailyVerseInfo.dayOfYear} of ${dailyVerseInfo.year})"
                                } else {
                                    "Tap to return to Today"
                                },
                                color = TextSecondaryMuted,
                                fontSize = 10.5.sp
                            )
                        }

                        IconButton(
                            onClick = { onShiftDate(1) },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("daily_verse_next_day_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Next day verse",
                                tint = CelestialBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Verse Reference Header + Action Icons (Copy & Bookmark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${verse.bookNameBm} / ${verse.bookNameEn} ${verse.chapter}:${verse.verse}",
                        color = CelestialBlue,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.5.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = { onBookmarkDailyVerse(verse) },
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("bookmark_daily_verse_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Bookmark Daily Verse",
                                tint = CelestialBlue,
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val textToCopy = buildString {
                                    append("Daily Verse - Dwibible (${dailyVerseInfo.formattedLocalDate})\n")
                                    append("${verse.bookNameBm} ${verse.chapter}:${verse.verse} (BM): ${verse.textBm}\n\n")
                                    append("${verse.bookNameEn} ${verse.chapter}:${verse.verse} (${popupEnglishVersion.code}): $enText")
                                }
                                clipboard.setPrimaryClip(ClipData.newPlainText("Dwibible Daily Verse", textToCopy))
                                Toast.makeText(context, "Daily Verse copied!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("copy_daily_verse_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Daily Verse",
                                tint = TextSecondaryMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bahasa Melayu (BM) Box on Top
                Surface(
                    color = PopupSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "BAHASA MELAYU (BM)",
                            color = CelestialBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"${verse.textBm}\"",
                            color = TextPrimaryOffWhite,
                            fontSize = 14.5.sp,
                            lineHeight = 21.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // English (Default: New King James Version (NKJV)) Box on Bottom
                Surface(
                    color = PopupSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = popupEnglishVersion.fullLabel.uppercase(),
                                color = CrimsonTriangle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = if (popupEnglishVersion == EnglishVersion.NKJV) "Switch to KJV" else "Switch to NKJV",
                                color = CelestialBlue,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        popupEnglishVersion = if (popupEnglishVersion == EnglishVersion.NKJV) {
                                            EnglishVersion.KJV
                                        } else {
                                            EnglishVersion.NKJV
                                        }
                                    }
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"$enText\"",
                            color = TextPrimaryOffWhite.copy(alpha = 0.92f),
                            fontSize = 14.5.sp,
                            lineHeight = 21.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons: Close & Read Full Chapter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        border = BorderStroke(1.dp, CardBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextSecondaryMuted
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(0.85f)
                            .height(46.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.close_popup),
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }

                    Button(
                        onClick = { onReadFullChapter(verse) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CelestialBlue,
                            contentColor = MidnightBackground
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1.45f)
                            .height(46.dp)
                            .testTag("read_daily_verse_chapter_button")
                    ) {
                        Text(
                            text = stringResource(R.string.read_full_chapter),
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VerseComparisonDialog(
    verse: BibleVerseEntity,
    onReadFullChapter: (BibleVerseEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.5.dp, CelestialBlue.copy(alpha = 0.65f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .testTag("verse_comparison_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Quick Verse Comparison",
                            color = TextPrimaryOffWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${verse.bookNameBm} / ${verse.bookNameEn} ${verse.chapter}:${verse.verse} • 3 Versions Side-by-Side",
                            color = VerseReferenceGold,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.5.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val textToCopy = buildString {
                                    append("${verse.bookNameBm} / ${verse.bookNameEn} ${verse.chapter}:${verse.verse}\n\n")
                                    append("[Malay - BM]\n${verse.textBm}\n\n")
                                    append("[King James Version - KJV]\n${verse.textKjv}\n\n")
                                    append("[New King James Version - NKJV]\n${verse.textNkjv}")
                                }
                                clipboard.setPrimaryClip(ClipData.newPlainText("Dwibible 3-Version Compare", textToCopy))
                                Toast.makeText(context, "All 3 versions copied!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy all 3 versions",
                                tint = CelestialBlue
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close comparison",
                                tint = TextSecondaryMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Side-by-Side 3-Column Comparison (Malay, KJV, NKJV)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Column 1: Malay (BM)
                    Surface(
                        color = PopupSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, CelestialBlue.copy(alpha = 0.5f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "MALAY (BM)",
                                color = CelestialBlue,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${verse.bookNameBm} ${verse.chapter}:${verse.verse}",
                                color = VerseReferenceGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = verse.textBm,
                                color = TextPrimaryOffWhite,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    // Column 2: KJV
                    Surface(
                        color = PopupSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, VerseReferenceGold.copy(alpha = 0.5f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "ENGLISH (KJV)",
                                color = VerseReferenceGold,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${verse.bookNameEn} ${verse.chapter}:${verse.verse}",
                                color = VerseReferenceGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = verse.textKjv,
                                color = TextPrimaryOffWhite,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    // Column 3: NKJV
                    Surface(
                        color = PopupSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, CrimsonTriangle.copy(alpha = 0.5f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "ENGLISH (NKJV)",
                                color = CrimsonTriangle,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${verse.bookNameEn} ${verse.chapter}:${verse.verse}",
                                color = VerseReferenceGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = verse.textNkjv,
                                color = TextPrimaryOffWhite,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        border = BorderStroke(1.dp, CardBorder),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondaryMuted),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(0.9f)
                            .height(44.dp)
                    ) {
                        Text("Close", fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp)
                    }

                    Button(
                        onClick = { onReadFullChapter(verse) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CelestialBlue,
                            contentColor = MidnightBackground
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1.4f)
                            .height(44.dp)
                    ) {
                        Text("Read Full Chapter", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun OfflineBibleQuizDialog(
    question: QuizQuestion?,
    selectedOptionIndex: Int?,
    score: Int,
    totalAnswered: Int,
    streak: Int,
    onSelectOption: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onResetStats: () -> Unit,
    onCompareSourceVerse: (BibleVerseEntity) -> Unit,
    onReadSourceChapter: (BibleVerseEntity) -> Unit,
    onDismiss: () -> Unit
) {
    if (question == null) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.5.dp, CelestialBlue.copy(alpha = 0.7f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("offline_bible_quiz_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {
                // Header: Quiz Icon + Title + Reset & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(VerseReferenceGold.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = VerseReferenceGold,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Offline Bible Mini-Quiz",
                                color = TextPrimaryOffWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.5.sp
                            )
                            Text(
                                text = "100% Offline Trivia from Local SQLite (31,102 Verses)",
                                color = TextSecondaryMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onResetStats,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Quiz Score",
                                tint = CelestialBlue,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("close_quiz_dialog_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Quiz",
                                tint = TextSecondaryMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Score & Streak Bar
                Surface(
                    color = MidnightBackground,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Score: $score / $totalAnswered",
                            color = VerseReferenceGold,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                        Surface(
                            color = CelestialBlue.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = question.categoryLabel,
                                color = CelestialBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = "Streak: $streak",
                            color = if (streak > 0) Color(0xFF22C55E) else TextSecondaryMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Question Prompt
                Text(
                    text = question.prompt,
                    color = TextPrimaryOffWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.5.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bilingual Scripture Clue Card (BM + NKJV)
                Surface(
                    color = PopupSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = question.clueTitleBm,
                            color = CelestialBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = question.clueTextBm,
                            color = TextPrimaryOffWhite,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = CardBorder)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = question.clueTitleEn,
                            color = CrimsonTriangle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = question.clueTextEn,
                            color = TextPrimaryOffWhite.copy(alpha = 0.92f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Multiple Choice Options (A, B, C, D)
                val hasAnswered = selectedOptionIndex != null
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    question.options.forEachIndexed { idx, option ->
                        val isCorrectOption = idx == question.correctIndex
                        val isChosenOption = idx == selectedOptionIndex

                        val borderColor = when {
                            !hasAnswered -> CardBorder
                            isCorrectOption -> Color(0xFF22C55E)
                            isChosenOption && !isCorrectOption -> CrimsonTriangle
                            else -> CardBorder
                        }
                        val bgColor = when {
                            !hasAnswered -> PopupSurface
                            isCorrectOption -> Color(0xFF153E32)
                            isChosenOption && !isCorrectOption -> Color(0xFF4A1D2F)
                            else -> PopupSurface.copy(alpha = 0.6f)
                        }

                        Surface(
                            color = bgColor,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(if (hasAnswered && (isCorrectOption || isChosenOption)) 1.5.dp else 1.dp, borderColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable(enabled = !hasAnswered) { onSelectOption(idx) }
                                .testTag("quiz_option_${option.letter.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    hasAnswered && isCorrectOption -> Color(0xFF22C55E)
                                                    hasAnswered && isChosenOption -> CrimsonTriangle
                                                    else -> CelestialBlue.copy(alpha = 0.22f)
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = option.letter,
                                            color = if (hasAnswered && (isCorrectOption || isChosenOption)) Color.White else VerseReferenceGold,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.5.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = option.text,
                                        color = TextPrimaryOffWhite,
                                        fontWeight = if (hasAnswered && isCorrectOption) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 13.5.sp
                                    )
                                }

                                if (hasAnswered && isCorrectOption) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Correct Answer",
                                        tint = Color(0xFF22C55E),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Answer Feedback & Explanation
                if (hasAnswered) {
                    val answeredCorrectly = selectedOptionIndex == question.correctIndex
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = if (answeredCorrectly) Color(0xFF153E32).copy(alpha = 0.65f) else Color(0xFF4A1D2F).copy(alpha = 0.65f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (answeredCorrectly) Color(0xFF22C55E) else CrimsonTriangle
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (answeredCorrectly) "Correct! Well done!" else "Not quite — keep studying!",
                                color = if (answeredCorrectly) Color(0xFF4ADE80) else Color(0xFFFCA5A5),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = question.explanation,
                                color = TextPrimaryOffWhite,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = "Compare 3 Versions",
                                    color = VerseReferenceGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    modifier = Modifier.clickable {
                                        onCompareSourceVerse(question.sourceVerse)
                                    }
                                )
                                Text(
                                    text = "•",
                                    color = TextSecondaryMuted,
                                    fontSize = 11.5.sp
                                )
                                Text(
                                    text = "Open Chapter in Reader",
                                    color = CelestialBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    modifier = Modifier.clickable {
                                        onReadSourceChapter(question.sourceVerse)
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Footer Action Buttons: Close & Next Question
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        border = BorderStroke(1.dp, CardBorder),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondaryMuted),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(0.85f)
                            .height(46.dp)
                    ) {
                        Text("Close", fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp)
                    }

                    Button(
                        onClick = onNextQuestion,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CelestialBlue,
                            contentColor = MidnightBackground
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1.45f)
                            .height(46.dp)
                            .testTag("next_quiz_question_button")
                    ) {
                        Text(
                            text = "Next Question",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp
                        )
                    }
                }
            }
        }
    }
}
