package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.R
import com.example.data.BibleBookInfo
import com.example.data.BibleVerseEntity
import com.example.data.EnglishVersion
import com.example.data.HighlightTone
import com.example.data.ReadingLayoutMode
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
import com.example.ui.theme.OpenSansFontFamily
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.PopupSurface
import com.example.ui.theme.ScrollbarThumb
import com.example.ui.theme.SearchBarBorder
import com.example.ui.theme.SearchBarSurface
import com.example.ui.theme.TextHintSlate
import com.example.ui.theme.TextPrimaryOffWhite
import com.example.ui.theme.TextSecondaryMuted
import com.example.ui.theme.TopBarSurface
import com.example.ui.theme.VerseReferenceGold

@Composable
fun DwibibleTopBar(
    savedBookmarksCount: Int,
    profileAvatarBitmap: ImageBitmap? = null,
    onResetToGenesis: () -> Unit,
    onOpenDailyVerse: () -> Unit,
    onOpenBibleQuiz: () -> Unit,
    onOpenBookmarks: () -> Unit,
    onOpenArchitectureInfo: () -> Unit
) {
    Surface(
        color = TopBarSurface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 6.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onResetToGenesis,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("top_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Genesis 1",
                        tint = TextPrimaryOffWhite
                    )
                }

                IconButton(
                    onClick = onOpenDailyVerse,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("daily_verse_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = stringResource(R.string.daily_verse_title),
                        tint = CelestialBlue,
                        modifier = Modifier.size(21.dp)
                    )
                }

                // Offline Bible Mini-Quiz (Trivia) Button
                Surface(
                    color = CelestialBlue.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, CelestialBlue.copy(alpha = 0.45f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(onClick = onOpenBibleQuiz)
                        .testTag("bible_quiz_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = "Offline Bible Quiz",
                            tint = VerseReferenceGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Quiz",
                            color = TextPrimaryOffWhite,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Center Brand: Open Bible + Cross Emblem + "Dwibible" Serif Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                BibleCrossLogoIcon(modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    color = TextPrimaryOffWhite
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box {
                    IconButton(
                        onClick = onOpenBookmarks,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("bookmarks_sheet_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmarks,
                            contentDescription = stringResource(R.string.bookmarks_and_notes),
                            tint = if (savedBookmarksCount > 0) CelestialBlue else TextSecondaryMuted,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    if (savedBookmarksCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 5.dp, end = 5.dp)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(CrimsonTriangle),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = savedBookmarksCount.coerceAtMost(99).toString(),
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onOpenArchitectureInfo,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("profile_architecture_button")
                ) {
                    if (profileAvatarBitmap != null) {
                        Image(
                            bitmap = profileAvatarBitmap,
                            contentDescription = "Custom Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, VerseReferenceGold, CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = stringResource(R.string.sql_architecture_guide),
                            tint = TextPrimaryOffWhite,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BibleCrossLogoIcon(modifier: Modifier = Modifier) {
    val accentBlue = CelestialBlue
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        // Left & Right Open Book Pages
        val bookPath = Path().apply {
            moveTo(w * 0.5f, h * 0.32f)
            cubicTo(w * 0.35f, h * 0.24f, w * 0.16f, h * 0.26f, w * 0.08f, h * 0.34f)
            lineTo(w * 0.08f, h * 0.82f)
            cubicTo(w * 0.20f, h * 0.76f, w * 0.36f, h * 0.76f, w * 0.5f, h * 0.86f)
            cubicTo(w * 0.64f, h * 0.76f, w * 0.80f, h * 0.76f, w * 0.92f, h * 0.82f)
            lineTo(w * 0.92f, h * 0.34f)
            cubicTo(w * 0.84f, h * 0.26f, w * 0.65f, h * 0.24f, w * 0.5f, h * 0.32f)
            close()
        }
        drawPath(
            path = bookPath,
            color = accentBlue.copy(alpha = 0.25f)
        )
        drawPath(
            path = bookPath,
            color = accentBlue,
            style = Stroke(width = w * 0.07f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        // Center spine
        drawLine(
            color = accentBlue,
            start = Offset(w * 0.5f, h * 0.32f),
            end = Offset(w * 0.5f, h * 0.86f),
            strokeWidth = w * 0.06f
        )
        // Cross rising above book
        drawLine(
            color = accentBlue,
            start = Offset(w * 0.5f, h * 0.06f),
            end = Offset(w * 0.5f, h * 0.34f),
            strokeWidth = w * 0.08f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = accentBlue,
            start = Offset(w * 0.38f, h * 0.16f),
            end = Offset(w * 0.62f, h * 0.16f),
            strokeWidth = w * 0.08f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun SmartSearchSection(
    searchQuery: String,
    searchResults: List<BibleVerseEntity>,
    englishVersion: EnglishVersion = EnglishVersion.NKJV,
    onSearchQueryChanged: (String) -> Unit,
    onClearSearch: () -> Unit,
    onSelectSearchResult: (BibleVerseEntity) -> Unit
) {
    var showQuickSuggestions by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.search_verse_header),
            fontFamily = OpenSansFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            letterSpacing = 0.8.sp,
            color = TextPrimaryOffWhite,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Rounded Pill Search Bar matching the reference image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(SearchBarSurface)
                .border(1.dp, SearchBarBorder, RoundedCornerShape(24.dp))
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextSecondaryMuted,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_placeholder),
                            color = TextHintSlate,
                            fontSize = 15.sp,
                            fontFamily = OpenSansFontFamily,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = {
                            showQuickSuggestions = false
                            onSearchQueryChanged(it)
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = TextPrimaryOffWhite,
                            fontSize = 15.sp
                        ),
                        cursorBrush = SolidColor(CelestialBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_verse_input")
                    )
                }

                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = onClearSearch,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("clear_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = TextSecondaryMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showQuickSuggestions = !showQuickSuggestions },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("quick_voice_presets_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Quick verse presets",
                            tint = TextSecondaryMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Quick preset suggestions when tapping the mic icon
        AnimatedVisibility(visible = showQuickSuggestions && searchQuery.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val presets = listOf("Genesis 1:1", "Mazmur 23:1", "John 3:16", "Roma 8:28")
                presets.forEach { preset ->
                    Surface(
                        color = PopupSurface,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.clickable {
                            showQuickSuggestions = false
                            onSearchQueryChanged(preset)
                        }
                    ) {
                        Text(
                            text = preset,
                            color = CelestialBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Live Offline Search Results Dropdown
        AnimatedVisibility(
            visible = searchResults.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = PopupSurface),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, CelestialBlue.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .heightIn(max = 240.dp)
                    .testTag("search_results_dropdown")
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    items(searchResults, key = { it.verseKey }) { item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectSearchResult(item) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${item.bookNameBm} / ${item.bookNameEn} ${item.chapter}:${item.verse}",
                                    color = CelestialBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Tap to open",
                                    color = TextSecondaryMuted,
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "BM: ${item.textBm}",
                                color = TextPrimaryOffWhite,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            val enSearchText = if (englishVersion == EnglishVersion.KJV) {
                                item.textKjv
                            } else {
                                item.textNkjv
                            }
                            Text(
                                text = "${englishVersion.code}: $enSearchText",
                                color = TextSecondaryMuted,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        HorizontalDivider(color = CardBorder.copy(alpha = 0.6f))
                    }
                }
            }
        }
    }
}

@Composable
fun BilingualToolbarRow(
    fontSizeSp: Float,
    isDarkMode: Boolean,
    layoutMode: ReadingLayoutMode,
    onDecreaseFont: () -> Unit,
    onIncreaseFont: () -> Unit,
    onToggleThemeMode: () -> Unit,
    onToggleLayoutMode: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Column Emblem: BM with open book waves (matching reference image)
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            BmColumnEmblem()
        }

        // Center Modern Controls: Font Resizer (A- / A+), Day/Dark Mode Toggle & Split/Single Mode Toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(CardSurface)
                .border(1.dp, CardBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.font_decrease),
                color = TextPrimaryOffWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onDecreaseFont)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("font_decrease_button")
            )
            Text(
                text = "${fontSizeSp.toInt()}sp",
                color = CelestialBlue,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.font_increase),
                color = TextPrimaryOffWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onIncreaseFont)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("font_increase_button")
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp)
                    .background(CardBorder)
            )

            // Day / Dark Mode Toggle Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onToggleThemeMode)
                    .padding(horizontal = 6.dp, vertical = 3.dp)
                    .testTag("theme_mode_toggle_button")
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = if (isDarkMode) "Switch to Day Mode" else "Switch to Dark Mode",
                    tint = CelestialBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = if (isDarkMode) "Day" else "Dark",
                    color = TextPrimaryOffWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp)
                    .background(CardBorder)
            )

            Icon(
                imageVector = if (layoutMode == ReadingLayoutMode.SPLIT_SCREEN) {
                    Icons.Default.ViewColumn
                } else {
                    Icons.Default.ViewAgenda
                },
                contentDescription = "Toggle split screen or single column",
                tint = CelestialBlue,
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onToggleLayoutMode)
                    .padding(3.dp)
                    .testTag("layout_toggle_button")
            )
        }

        // Right Column Emblem: EN stacked page icon (matching reference image)
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            EnColumnEmblem()
        }
    }
}

@Composable
fun BmColumnEmblem() {
    val accentBlue = CelestialBlue
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "BM",
            color = accentBlue,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 17.sp,
            letterSpacing = 0.5.sp
        )
        Canvas(modifier = Modifier.size(width = 34.dp, height = 10.dp)) {
            val w = size.width
            val h = size.height
            val path1 = Path().apply {
                moveTo(0f, h * 0.25f)
                quadraticTo(w * 0.25f, 0f, w * 0.5f, h * 0.45f)
                quadraticTo(w * 0.75f, 0f, w, h * 0.25f)
            }
            val path2 = Path().apply {
                moveTo(0f, h * 0.7f)
                quadraticTo(w * 0.25f, h * 0.45f, w * 0.5f, h * 0.9f)
                quadraticTo(w * 0.75f, h * 0.45f, w, h * 0.7f)
            }
            drawPath(path1, color = accentBlue, style = Stroke(width = 3.5f, cap = StrokeCap.Round))
            drawPath(path2, color = accentBlue, style = Stroke(width = 3.5f, cap = StrokeCap.Round))
        }
    }
}

@Composable
fun EnColumnEmblem() {
    val accentBlue = CelestialBlue
    val bgTextColor = MidnightBackground
    Box(
        modifier = Modifier.size(34.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            // Back page border
            drawRoundRect(
                color = accentBlue,
                topLeft = Offset(w * 0.18f, h * 0.16f),
                size = Size(w * 0.72f, h * 0.74f),
                cornerRadius = CornerRadius(6f, 6f),
                style = Stroke(width = 3.5f)
            )
            // Front filled badge
            drawRoundRect(
                color = accentBlue,
                topLeft = Offset(w * 0.06f, h * 0.06f),
                size = Size(w * 0.72f, h * 0.72f),
                cornerRadius = CornerRadius(6f, 6f)
            )
        }
        Text(
            text = "EN",
            color = bgTextColor,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
        )
    }
}

@Composable
fun SplitScreenReaderView(
    verses: List<VerseWithStudy>,
    englishVersion: EnglishVersion,
    isVersionMenuExpanded: Boolean,
    fontSizeSp: Float,
    bmListState: LazyListState,
    enListState: LazyListState,
    onToggleVersionMenu: () -> Unit,
    onSelectEnglishVersion: (EnglishVersion) -> Unit,
    onVerseClick: (VerseWithStudy) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // =====================================================================
        // LEFT PANE: BM VERSION (Malay Text)
        // =====================================================================
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, CardBorder),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .testTag("bm_version_card")
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.bm_version_header),
                        color = TextPrimaryOffWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 0.4.sp
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        state = bmListState,
                        contentPadding = PaddingValues(start = 12.dp, end = 14.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(verses, key = { "bm_${it.verse.verseKey}" }) { item ->
                            ScriptureVerseBlock(
                                referenceTitle = "${item.verse.bookNameBm} ${item.verse.chapter}:${item.verse.verse}.",
                                verseBody = item.verse.textBm,
                                fontSizeSp = fontSizeSp,
                                isBookmarked = item.isBookmarked,
                                hasNote = item.hasNote,
                                highlightTone = item.highlightTone,
                                onClick = { onVerseClick(item) }
                            )
                        }
                    }

                    // Visible Scrollbar Indicator on Right Edge of BM Card (matching mockup)
                    VerticalScrollBarIndicator(
                        listState = bmListState,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 4.dp, top = 4.dp, bottom = 12.dp)
                    )
                }
            }
        }

        // =====================================================================
        // RIGHT PANE: ENGLISH VERSION (KJV / NKJV with Red Triangle Pop-Up)
        // =====================================================================
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, CardBorder),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .testTag("en_version_card")
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header with Red Triangle Dropdown Trigger
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onToggleVersionMenu)
                            .padding(vertical = 12.dp, horizontal = 8.dp)
                            .testTag("english_version_selector_trigger"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = englishVersion.shortLabel,
                            color = TextPrimaryOffWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Red downward triangle icon matching reference image
                        RedTriangleIndicator(isExpanded = isVersionMenuExpanded)
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        LazyColumn(
                            state = enListState,
                            contentPadding = PaddingValues(start = 12.dp, end = 14.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(verses, key = { "en_${it.verse.verseKey}" }) { item ->
                                val englishText = if (englishVersion == EnglishVersion.KJV) {
                                    item.verse.textKjv
                                } else {
                                    item.verse.textNkjv
                                }
                                ScriptureVerseBlock(
                                    referenceTitle = "${item.verse.bookNameEn} ${item.verse.chapter}:${item.verse.verse}.",
                                    verseBody = englishText,
                                    fontSizeSp = fontSizeSp,
                                    isBookmarked = item.isBookmarked,
                                    hasNote = item.hasNote,
                                    highlightTone = item.highlightTone,
                                    onClick = { onVerseClick(item) }
                                )
                            }
                        }

                        // Visible Scrollbar Indicator on Right Edge of EN Card (matching mockup)
                        VerticalScrollBarIndicator(
                            listState = enListState,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 4.dp, top = 4.dp, bottom = 12.dp)
                        )
                    }
                }

                // Floating Radio-Button Pop-up Menu for KJV / NKJV (matching reference image)
                androidx.compose.animation.AnimatedVisibility(
                    visible = isVersionMenuExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 40.dp, start = 4.dp, end = 4.dp)
                        .zIndex(10f)
                ) {
                    Surface(
                        color = PopupSurface,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(12.dp, RoundedCornerShape(10.dp))
                            .testTag("version_popup_menu")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 6.dp)
                        ) {
                            EnglishVersion.entries.forEach { versionOption ->
                                val isSelected = versionOption == englishVersion
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onSelectEnglishVersion(versionOption) }
                                        .padding(vertical = 6.dp, horizontal = 4.dp)
                                        .testTag("radio_${versionOption.code.lowercase()}"),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { onSelectEnglishVersion(versionOption) },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = CelestialBlue,
                                            unselectedColor = TextSecondaryMuted
                                        ),
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = versionOption.fullLabel,
                                        color = TextPrimaryOffWhite,
                                        fontSize = 12.5.sp,
                                        lineHeight = 16.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
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

@Composable
fun RedTriangleIndicator(isExpanded: Boolean) {
    val triangleColor = CrimsonTriangle
    Canvas(modifier = Modifier.size(13.dp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            if (isExpanded) {
                moveTo(w * 0.5f, h * 0.15f)
                lineTo(w * 0.92f, h * 0.85f)
                lineTo(w * 0.08f, h * 0.85f)
            } else {
                moveTo(w * 0.08f, h * 0.18f)
                lineTo(w * 0.92f, h * 0.18f)
                lineTo(w * 0.5f, h * 0.88f)
            }
            close()
        }
        drawPath(path = path, color = triangleColor)
    }
}

@Composable
fun VerticalScrollBarIndicator(
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    val thumbFraction by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val total = layoutInfo.totalItemsCount.coerceAtLeast(1)
            val first = listState.firstVisibleItemIndex
            (first.toFloat() / total.toFloat()).coerceIn(0f, 0.7f)
        }
    }

    Box(
        modifier = modifier
            .width(4.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(CardBorder.copy(alpha = 0.35f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .align(Alignment.TopCenter)
                .padding(top = (thumbFraction * 55).dp)
                .clip(RoundedCornerShape(2.dp))
                .background(ScrollbarThumb)
        )
    }
}

@Composable
fun ScriptureVerseBlock(
    referenceTitle: String,
    verseBody: String,
    fontSizeSp: Float,
    isBookmarked: Boolean,
    hasNote: Boolean,
    highlightTone: HighlightTone,
    onClick: () -> Unit
) {
    val bgTint = when (highlightTone) {
        HighlightTone.GOLD -> HighlightGold
        HighlightTone.BLUE -> HighlightBlue
        HighlightTone.EMERALD -> HighlightEmerald
        HighlightTone.ROSE -> HighlightRose
        HighlightTone.NONE -> Color.Transparent
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bgTint)
            .clickable(onClick = onClick)
            .padding(
                horizontal = if (highlightTone != HighlightTone.NONE) 8.dp else 2.dp,
                vertical = if (highlightTone != HighlightTone.NONE) 6.dp else 2.dp
            )
    ) {
        if (isBookmarked || hasNote) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(bottom = 3.dp)
            ) {
                if (isBookmarked) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Bookmarked verse",
                        tint = CelestialBlue,
                        modifier = Modifier.size(14.dp)
                    )
                }
                if (hasNote) {
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = "Has study note",
                        tint = CrimsonTriangle,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }

        val cleanBody = verseBody.trim().let { text ->
            val refTrimmed = referenceTitle.trim()
            val refWithoutDot = refTrimmed.trimEnd('.')
            when {
                text.startsWith(refTrimmed, ignoreCase = true) ->
                    text.substring(refTrimmed.length).trimStart('.', ' ')
                text.startsWith(refWithoutDot, ignoreCase = true) ->
                    text.substring(refWithoutDot.length).trimStart('.', ' ')
                else -> text
            }
        }

        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = VerseReferenceGold)) {
                    append("$referenceTitle ")
                }
                withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = TextPrimaryOffWhite.copy(alpha = 0.94f))) {
                    append(cleanBody)
                }
            },
            fontSize = fontSizeSp.sp,
            lineHeight = (fontSizeSp * 1.42f).sp,
            fontFamily = OpenSansFontFamily
        )
    }
}

@Composable
fun SingleColumnReaderView(
    verses: List<VerseWithStudy>,
    englishVersion: EnglishVersion,
    fontSizeSp: Float,
    onSelectEnglishVersion: (EnglishVersion) -> Unit,
    onVerseClick: (VerseWithStudy) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
    ) {
        // Version switcher pill bar in Single-Column mode
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "BILINGUAL STACKED VIEW",
                color = TextSecondaryMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                EnglishVersion.entries.forEach { ver ->
                    val selected = ver == englishVersion
                    Surface(
                        color = if (selected) CelestialBlue else CardSurface,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, if (selected) CelestialBlue else CardBorder),
                        modifier = Modifier.clickable { onSelectEnglishVersion(ver) }
                    ) {
                        Text(
                            text = ver.code,
                            color = if (selected) MidnightBackground else TextPrimaryOffWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(verses, key = { "single_${it.verse.verseKey}" }) { item ->
                val enText = if (englishVersion == EnglishVersion.KJV) {
                    item.verse.textKjv
                } else {
                    item.verse.textNkjv
                }
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onVerseClick(item) }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${item.verse.bookNameBm} / ${item.verse.bookNameEn} ${item.verse.chapter}:${item.verse.verse}",
                                color = CelestialBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                if (item.isBookmarked) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = "Bookmarked",
                                        tint = CelestialBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                if (item.hasNote) {
                                    Icon(
                                        imageVector = Icons.Default.EditNote,
                                        contentDescription = "Has note",
                                        tint = CrimsonTriangle,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = VerseReferenceGold)) {
                                    append("${item.verse.bookNameBm} ${item.verse.chapter}:${item.verse.verse}. ")
                                }
                                withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = TextPrimaryOffWhite)) {
                                    append(item.verse.textBm)
                                }
                            },
                            fontSize = fontSizeSp.sp,
                            lineHeight = (fontSizeSp * 1.4f).sp,
                            fontFamily = OpenSansFontFamily
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = CardBorder.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = VerseReferenceGold)) {
                                    append("${item.verse.bookNameEn} ${item.verse.chapter}:${item.verse.verse}. ")
                                }
                                withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = TextPrimaryOffWhite.copy(alpha = 0.88f))) {
                                    append(enText)
                                }
                            },
                            fontSize = fontSizeSp.sp,
                            lineHeight = (fontSizeSp * 1.4f).sp,
                            fontFamily = OpenSansFontFamily
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBookChapterFooter(
    selectedBook: BibleBookInfo,
    selectedChapter: Int,
    onOpenBookPicker: () -> Unit,
    onOpenChapterPicker: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Quick Navigation Row: Book Selector & Chapter Selector matching the mockup
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Left Button: Book: Genesis
            Surface(
                color = CardSurface,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .clickable(onClick = onOpenBookPicker)
                    .testTag("book_picker_button")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BookWithCrossSmallIcon(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.book_prefix, selectedBook.nameEn),
                        color = TextPrimaryOffWhite,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Right Button: Chapter: 1
            Surface(
                color = CardSurface,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .clickable(onClick = onOpenChapterPicker)
                    .testTag("chapter_picker_button")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ChapterBadgeSmallIcon(
                        chapterNumber = selectedChapter,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.chapter_prefix, selectedChapter),
                        color = TextPrimaryOffWhite,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Status Bar at the very bottom: "App by Rikko | KJV/NKJV | Offline Mode"
        Text(
            text = stringResource(R.string.footer_status),
            fontFamily = PlayfairFontFamily,
            fontSize = 13.5.sp,
            color = TextSecondaryMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
                .testTag("offline_status_footer")
        )
    }
}

@Composable
fun BookWithCrossSmallIcon(modifier: Modifier = Modifier) {
    val accentBlue = CelestialBlue
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRoundRect(
            color = accentBlue,
            topLeft = Offset(w * 0.15f, h * 0.1f),
            size = Size(w * 0.7f, h * 0.8f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(width = 3.5f)
        )
        // Cross inside book cover
        drawLine(
            color = accentBlue,
            start = Offset(w * 0.5f, h * 0.28f),
            end = Offset(w * 0.5f, h * 0.68f),
            strokeWidth = 3.5f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = accentBlue,
            start = Offset(w * 0.35f, h * 0.42f),
            end = Offset(w * 0.65f, h * 0.42f),
            strokeWidth = 3.5f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun ChapterBadgeSmallIcon(chapterNumber: Int, modifier: Modifier = Modifier) {
    val accentBlue = CelestialBlue
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawRoundRect(
                color = accentBlue,
                topLeft = Offset(w * 0.15f, h * 0.1f),
                size = Size(w * 0.7f, h * 0.8f),
                cornerRadius = CornerRadius(4f, 4f),
                style = Stroke(width = 3.5f)
            )
        }
        Text(
            text = chapterNumber.coerceAtMost(99).toString(),
            color = accentBlue,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
