package com.example.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class ParsedReference(
    val book: BibleBookInfo,
    val chapter: Int,
    val verse: Int?
)

data class DailyVerseInfo(
    val verse: BibleVerseEntity,
    val formattedLocalDate: String,
    val dayOfYear: Int,
    val year: Int,
    val dayOffset: Int
)

class BibleRepository(private val bibleDao: BibleDao) {

    private val seedMutex = Mutex()

    val allStudyRecords: Flow<List<VerseStudyEntity>> = bibleDao.getAllStudyRecords()

    suspend fun ensureSeeded() {
        seedMutex.withLock {
            val count = bibleDao.getVerseCount()
            if (count == 0) {
                bibleDao.insertVerses(BibleSeedData.buildInitialSeedVerses())
            }
        }
    }

    /**
     * Computes the Daily Verse 100% offline using the device's local calendar date
     * across the complete 31,102-verse database.
     */
    suspend fun getDailyVerseByLocalDate(dayOffset: Int = 0): DailyVerseInfo {
        ensureSeeded()
        val calendar = Calendar.getInstance()
        if (dayOffset != 0) {
            calendar.add(Calendar.DAY_OF_YEAR, dayOffset)
        }

        val year = calendar.get(Calendar.YEAR)
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val dateFormat = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        val totalCount = bibleDao.getVerseCount().coerceAtLeast(1)
        val targetId = (abs(year * 367 + dayOfYear * 29) % totalCount) + 1
        val selectedVerse = bibleDao.getVerseById(targetId)
            ?: bibleDao.getVersesByReference(1, 1, 1).firstOrNull()
            ?: BibleSeedData.buildInitialSeedVerses().first()

        return DailyVerseInfo(
            verse = sanitizeVerse(selectedVerse),
            formattedLocalDate = formattedDate,
            dayOfYear = dayOfYear,
            year = year,
            dayOffset = dayOffset
        )
    }

    suspend fun ensureChapterPopulated(book: BibleBookInfo, chapter: Int) {
        ensureSeeded()
    }

    fun getChapterVersesWithStudy(bookNumber: Int, chapter: Int): Flow<List<VerseWithStudy>> {
        return combine(
            bibleDao.getVersesForChapter(bookNumber, chapter),
            bibleDao.getAllStudyRecords()
        ) { verses, studyList ->
            val studyMap = studyList.associateBy { it.verseKey }
            verses.map { rawVerse ->
                val cleanVerse = sanitizeVerse(rawVerse)
                VerseWithStudy(
                    verse = cleanVerse,
                    study = studyMap[cleanVerse.verseKey]
                )
            }
        }
    }

    private fun sanitizeVerse(v: BibleVerseEntity): BibleVerseEntity {
        return v.copy(
            textBm = BibleSeedData.stripRedundantPrefix(v.textBm, v.bookNameBm, v.chapter, v.verse),
            textKjv = BibleSeedData.stripRedundantPrefix(v.textKjv, v.bookNameEn, v.chapter, v.verse),
            textNkjv = BibleSeedData.stripRedundantPrefix(v.textNkjv, v.bookNameEn, v.chapter, v.verse)
        )
    }

    suspend fun searchVerses(rawQuery: String): List<BibleVerseEntity> {
        ensureSeeded()
        val trimmed = rawQuery.trim()
        if (trimmed.isEmpty()) return emptyList()

        val parsed = parseReference(trimmed)
        if (parsed != null) {
            val refMatches = bibleDao.getVersesByReference(
                bookNumber = parsed.book.bookNumber,
                chapter = parsed.chapter,
                verse = parsed.verse
            )
            if (refMatches.isNotEmpty()) {
                return refMatches.map(::sanitizeVerse)
            }
        }

        return bibleDao.searchVersesByKeyword(trimmed).map(::sanitizeVerse)
    }

    fun parseReference(input: String): ParsedReference? {
        val cleaned = input.trim()
        val regex = Regex("""^([1-3]?\s*[A-Za-z\- ]+?)\s+(\d+)(?::(\d+))?$""")
        val match = regex.find(cleaned) ?: return null

        val bookPart = match.groupValues[1].trim().lowercase()
        val chapterPart = match.groupValues[2].toIntOrNull() ?: return null
        val versePart = match.groupValues[3].takeIf { it.isNotEmpty() }?.toIntOrNull()

        val matchedBook = BibleSeedData.allBooks.find { book ->
            book.nameEn.lowercase() == bookPart ||
                book.nameBm.lowercase() == bookPart ||
                book.nameEn.lowercase().startsWith(bookPart) ||
                book.nameBm.lowercase().startsWith(bookPart)
        } ?: return null

        val validChapter = chapterPart.coerceIn(1, matchedBook.totalChapters)
        return ParsedReference(
            book = matchedBook,
            chapter = validChapter,
            verse = versePart
        )
    }

    suspend fun saveVerseStudy(
        verse: BibleVerseEntity,
        isBookmarked: Boolean,
        highlightColor: String,
        noteText: String
    ) {
        if (!isBookmarked && highlightColor == HighlightTone.NONE.key && noteText.isBlank()) {
            bibleDao.deleteStudyRecord(verse.verseKey)
        } else {
            bibleDao.upsertStudyRecord(
                VerseStudyEntity(
                    verseKey = verse.verseKey,
                    bookNumber = verse.bookNumber,
                    bookNameEn = verse.bookNameEn,
                    bookNameBm = verse.bookNameBm,
                    chapter = verse.chapter,
                    verse = verse.verse,
                    isBookmarked = isBookmarked,
                    highlightColor = highlightColor,
                    noteText = noteText.trim(),
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun removeStudyRecord(verseKey: String) {
        bibleDao.deleteStudyRecord(verseKey)
    }

    suspend fun getVerseForStudy(study: VerseStudyEntity): BibleVerseEntity? {
        ensureSeeded()
        return bibleDao.getVersesByReference(
            bookNumber = study.bookNumber,
            chapter = study.chapter,
            verse = study.verse
        ).firstOrNull()?.let(::sanitizeVerse)
    }

    /**
     * Generates a multiple-choice (A, B, C, D) trivia question 100% offline
     * using random verses from the local SQLite database.
     */
    suspend fun generateOfflineQuizQuestion(questionCounter: Int): QuizQuestion {
        ensureSeeded()
        val totalCount = bibleDao.getVerseCount().coerceAtLeast(1)
        val random = kotlin.random.Random(System.nanoTime() xor questionCounter.toLong())

        // Pick 12 random candidate IDs so we can select 4 verses from distinct books/chapters
        val candidateIds = (1..12).map { random.nextInt(1, totalCount + 1) }.distinct()
        val fetched = bibleDao.getVersesByIds(candidateIds)
            .map(::sanitizeVerse)
            .filter { it.textNkjv.length >= 30 && it.textBm.length >= 25 }

        val distinctVerses = fetched
            .distinctBy { "${it.bookNumber}_${it.chapter}" }
            .take(4)
            .toMutableList()

        if (distinctVerses.size < 4) {
            val fallbacks = BibleSeedData.buildInitialSeedVerses()
                .map(::sanitizeVerse)
                .shuffled(random)
            for (fb in fallbacks) {
                if (distinctVerses.size >= 4) break
                if (distinctVerses.none { it.bookNumber == fb.bookNumber && it.chapter == fb.chapter }) {
                    distinctVerses.add(fb)
                }
            }
        }

        val targetVerse = distinctVerses.first()
        val letters = listOf("A", "B", "C", "D")
        val mode = questionCounter % 3

        return when (mode) {
            0 -> {
                // Mode 0: Identify the exact Book & Chapter:Verse from bilingual text
                val shuffledChoices = distinctVerses.shuffled(random)
                val correctIdx = shuffledChoices.indexOfFirst { it.verseKey == targetVerse.verseKey }.coerceAtLeast(0)
                val options = shuffledChoices.mapIndexed { idx, v ->
                    QuizOption(
                        letter = letters[idx],
                        text = "${v.bookNameBm} / ${v.bookNameEn} ${v.chapter}:${v.verse}"
                    )
                }
                QuizQuestion(
                    categoryLabel = "Scripture Reference Challenge",
                    prompt = "Which Bible Book & Verse does this passage come from?",
                    clueTitleBm = "BAHASA MELAYU (BM)",
                    clueTextBm = "\"${targetVerse.textBm}\"",
                    clueTitleEn = "NEW KING JAMES VERSION (NKJV)",
                    clueTextEn = "\"${targetVerse.textNkjv}\"",
                    options = options,
                    correctIndex = correctIdx,
                    explanation = "${targetVerse.bookNameBm} / ${targetVerse.bookNameEn} ${targetVerse.chapter}:${targetVerse.verse}",
                    sourceVerse = targetVerse
                )
            }
            1 -> {
                // Mode 1: Missing Key Word in NKJV with full BM verse as bilingual clue
                val words = targetVerse.textNkjv
                    .split(Regex("\\s+"))
                    .map { it.trim(',', '.', ';', ':', '!', '?', '"', '\'', '(', ')') }
                    .filter { it.length >= 5 && it.all { ch -> ch.isLetter() } }
                val answerWord = words.randomOrNull(random) ?: "Lord"
                val maskedEn = targetVerse.textNkjv.replaceFirst(
                    Regex("\\b${Regex.escape(answerWord)}\\b"),
                    "_______"
                )

                val distractorPool = distinctVerses.drop(1)
                    .flatMap { v ->
                        v.textNkjv.split(Regex("\\s+"))
                            .map { it.trim(',', '.', ';', ':', '!', '?', '"', '\'', '(', ')') }
                            .filter {
                                it.length >= 5 &&
                                    it.all { ch -> ch.isLetter() } &&
                                    !it.equals(answerWord, ignoreCase = true)
                            }
                    }
                    .distinctBy { it.lowercase() }
                    .shuffled(random)
                    .take(3)
                    .toMutableList()

                val defaultDistractors = listOf("spirit", "heaven", "wisdom", "temple", "kingdom", "covenant")
                for (d in defaultDistractors) {
                    if (distractorPool.size >= 3) break
                    if (!d.equals(answerWord, ignoreCase = true) && distractorPool.none { it.equals(d, ignoreCase = true) }) {
                        distractorPool.add(d)
                    }
                }

                val rawChoices = (listOf(answerWord) + distractorPool.take(3)).shuffled(random)
                val correctIdx = rawChoices.indexOfFirst { it.equals(answerWord, ignoreCase = true) }.coerceAtLeast(0)
                val options = rawChoices.mapIndexed { idx, w ->
                    QuizOption(letter = letters[idx], text = w)
                }

                QuizQuestion(
                    categoryLabel = "Fill in the Missing Word",
                    prompt = "Complete the missing word in ${targetVerse.bookNameEn} ${targetVerse.chapter}:${targetVerse.verse} (use the BM verse as a hint):",
                    clueTitleBm = "${targetVerse.bookNameBm} ${targetVerse.chapter}:${targetVerse.verse} (BM Clue)",
                    clueTextBm = "\"${targetVerse.textBm}\"",
                    clueTitleEn = "${targetVerse.bookNameEn} ${targetVerse.chapter}:${targetVerse.verse} (NKJV)",
                    clueTextEn = "\"$maskedEn\"",
                    options = options,
                    correctIndex = correctIdx,
                    explanation = "The missing word is \"$answerWord\" in ${targetVerse.bookNameEn} ${targetVerse.chapter}:${targetVerse.verse}.",
                    sourceVerse = targetVerse
                )
            }
            else -> {
                // Mode 2: Identify the Bible Book (Bilingual BM / EN Book Name)
                val otherBooks = BibleSeedData.allBooks
                    .filter { it.bookNumber != targetVerse.bookNumber }
                    .shuffled(random)
                    .take(3)
                val targetBook = BibleSeedData.getBookByNumber(targetVerse.bookNumber)
                val bookChoices = (listOf(targetBook) + otherBooks).shuffled(random)
                val correctIdx = bookChoices.indexOfFirst { it.bookNumber == targetBook.bookNumber }.coerceAtLeast(0)
                val options = bookChoices.mapIndexed { idx, b ->
                    val testamentLabel = if (b.testament == "OT") "OT" else "NT"
                    QuizOption(
                        letter = letters[idx],
                        text = "${b.nameBm} / ${b.nameEn} ($testamentLabel)"
                    )
                }

                QuizQuestion(
                    categoryLabel = "Bible Book Trivia",
                    prompt = "Which Book of the Bible contains this verse (Chapter ${targetVerse.chapter}, Verse ${targetVerse.verse})?",
                    clueTitleBm = "BAHASA MELAYU (BM)",
                    clueTextBm = "\"${targetVerse.textBm}\"",
                    clueTitleEn = "NEW KING JAMES VERSION (NKJV)",
                    clueTextEn = "\"${targetVerse.textNkjv}\"",
                    options = options,
                    correctIndex = correctIdx,
                    explanation = "This verse is from ${targetVerse.bookNameBm} / ${targetVerse.bookNameEn} ${targetVerse.chapter}:${targetVerse.verse}.",
                    sourceVerse = targetVerse
                )
            }
        }
    }
}
