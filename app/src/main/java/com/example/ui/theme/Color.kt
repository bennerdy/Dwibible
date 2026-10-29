package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class DwibibleColorPalette(
    val isDark: Boolean,
    val background: Color,
    val topBarSurface: Color,
    val cardSurface: Color,
    val cardBorder: Color,
    val popupSurface: Color,
    val searchBarSurface: Color,
    val searchBarBorder: Color,
    val celestialBlue: Color,
    val celestialBlueLight: Color,
    val crimsonTriangle: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textHint: Color,
    val scrollbarThumb: Color,
    val highlightGold: Color,
    val highlightEmerald: Color,
    val highlightBlue: Color,
    val highlightRose: Color,
    val verseReferenceGold: Color
)

val DarkDwibiblePalette = DwibibleColorPalette(
    isDark = true,
    background = Color(0xFF12161F),
    topBarSurface = Color(0xFF1A202C),
    cardSurface = Color(0xFF1E2433),
    cardBorder = Color(0xFF2D364B),
    popupSurface = Color(0xFF283042),
    searchBarSurface = Color(0xFF202634),
    searchBarBorder = Color(0xFF434E65),
    celestialBlue = Color(0xFF7BA7E1),
    celestialBlueLight = Color(0xFFA3C4F3),
    crimsonTriangle = Color(0xFFE53935),
    textPrimary = Color(0xFFF1F5F9),
    textSecondary = Color(0xFF94A3B8),
    textHint = Color(0xFF6E7A91),
    scrollbarThumb = Color(0xFF647087),
    highlightGold = Color(0xFF4D3E18),
    highlightEmerald = Color(0xFF153E32),
    highlightBlue = Color(0xFF1B3454),
    highlightRose = Color(0xFF4A1D2F),
    verseReferenceGold = Color(0xFFFFD700)
)

val LightDwibiblePalette = DwibibleColorPalette(
    isDark = false,
    background = Color(0xFFF4F1EA),
    topBarSurface = Color(0xFFFFFFFF),
    cardSurface = Color(0xFFFFFFFF),
    cardBorder = Color(0xFFD5DCE6),
    popupSurface = Color(0xFFF8FAFC),
    searchBarSurface = Color(0xFFFFFFFF),
    searchBarBorder = Color(0xFFBCC6D6),
    celestialBlue = Color(0xFF1E5BB8),
    celestialBlueLight = Color(0xFF3B76D4),
    crimsonTriangle = Color(0xFFD32F2F),
    textPrimary = Color(0xFF18202C),
    textSecondary = Color(0xFF526075),
    textHint = Color(0xFF78869B),
    scrollbarThumb = Color(0xFF94A3B8),
    highlightGold = Color(0xFFFEF08A),
    highlightEmerald = Color(0xFFBBF7D0),
    highlightBlue = Color(0xFFBFDBFE),
    highlightRose = Color(0xFFFECDD3),
    verseReferenceGold = Color(0xFFB45309)
)

val LocalDwibibleColors = staticCompositionLocalOf { DarkDwibiblePalette }

val MidnightBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.background

val TopBarSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.topBarSurface

val CardSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.cardSurface

val CardBorder: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.cardBorder

val PopupSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.popupSurface

val SearchBarSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.searchBarSurface

val SearchBarBorder: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.searchBarBorder

val CelestialBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.celestialBlue

val CelestialBlueLight: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.celestialBlueLight

val CrimsonTriangle: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.crimsonTriangle

val TextPrimaryOffWhite: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.textPrimary

val TextSecondaryMuted: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.textSecondary

val TextHintSlate: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.textHint

val ScrollbarThumb: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.scrollbarThumb

val HighlightGold: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.highlightGold

val HighlightEmerald: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.highlightEmerald

val HighlightBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.highlightBlue

val HighlightRose: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.highlightRose

val VerseReferenceGold: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalDwibibleColors.current.verseReferenceGold
