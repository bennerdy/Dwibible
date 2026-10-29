package com.example.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

enum class EnglishVersion(val code: String, val shortLabel: String, val fullLabel: String) {
    KJV("KJV", "King James Version", "King James Version (KJV)"),
    NKJV("NKJV", "New King James Version", "New King James Version (NKJV)")
}

enum class ReadingLayoutMode {
    SPLIT_SCREEN,
    SINGLE_COLUMN
}

enum class HighlightTone(val key: String, val label: String) {
    NONE("NONE", "None"),
    GOLD("GOLD", "Yellow"),
    EMERALD("EMERALD", "Green"),
    BLUE("BLUE", "Blue"),
    ROSE("ROSE", "Rose")
}

data class QuizOption(
    val letter: String, // "A", "B", "C", "D"
    val text: String
)

data class QuizQuestion(
    val categoryLabel: String,
    val prompt: String,
    val clueTitleBm: String,
    val clueTextBm: String,
    val clueTitleEn: String,
    val clueTextEn: String,
    val options: List<QuizOption>,
    val correctIndex: Int,
    val explanation: String,
    val sourceVerse: BibleVerseEntity
)

data class BibleBookInfo(
    val bookNumber: Int,
    val nameEn: String,
    val nameBm: String,
    val testament: String, // "OT" or "NT"
    val totalChapters: Int
)

@Entity(
    tableName = "bible_verses",
    indices = [
        Index(value = ["bookNumber", "chapter", "verse"], unique = true),
        Index(value = ["bookNameEn"]),
        Index(value = ["bookNameBm"])
    ]
)
data class BibleVerseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bookNumber: Int,
    val bookNameEn: String,
    val bookNameBm: String,
    val testament: String,
    val chapter: Int,
    val verse: Int,
    val textBm: String,
    val textKjv: String,
    val textNkjv: String
) {
    val verseKey: String
        get() = "${bookNumber}_${chapter}_${verse}"
}

@Entity(tableName = "verse_study")
data class VerseStudyEntity(
    @PrimaryKey val verseKey: String,
    val bookNumber: Int,
    val bookNameEn: String,
    val bookNameBm: String,
    val chapter: Int,
    val verse: Int,
    val isBookmarked: Boolean = false,
    val highlightColor: String = HighlightTone.NONE.key,
    val noteText: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

data class VerseWithStudy(
    val verse: BibleVerseEntity,
    val study: VerseStudyEntity?
) {
    val isBookmarked: Boolean
        get() = study?.isBookmarked == true

    val hasNote: Boolean
        get() = !study?.noteText.isNullOrBlank()

    val highlightTone: HighlightTone
        get() = HighlightTone.entries.find { it.key == study?.highlightColor } ?: HighlightTone.NONE
}
