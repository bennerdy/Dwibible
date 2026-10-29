package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BibleDao {
    @Query("SELECT COUNT(*) FROM bible_verses")
    suspend fun getVerseCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<BibleVerseEntity>)

    @Query("SELECT * FROM bible_verses WHERE id = :id LIMIT 1")
    suspend fun getVerseById(id: Int): BibleVerseEntity?

    @Query("SELECT * FROM bible_verses WHERE id IN (:ids)")
    suspend fun getVersesByIds(ids: List<Int>): List<BibleVerseEntity>

    @Query("SELECT * FROM bible_verses ORDER BY bookNumber ASC, chapter ASC, verse ASC")
    suspend fun getAllVersesOrdered(): List<BibleVerseEntity>

    @Query(
        """
        SELECT * FROM bible_verses 
        WHERE bookNumber = :bookNumber AND chapter = :chapter 
        ORDER BY verse ASC
        """
    )
    fun getVersesForChapter(bookNumber: Int, chapter: Int): Flow<List<BibleVerseEntity>>

    @Query(
        """
        SELECT DISTINCT chapter FROM bible_verses 
        WHERE bookNumber = :bookNumber 
        ORDER BY chapter ASC
        """
    )
    fun getAvailableChapters(bookNumber: Int): Flow<List<Int>>

    @Query(
        """
        SELECT * FROM bible_verses 
        WHERE textBm LIKE '%' || :query || '%' 
           OR textKjv LIKE '%' || :query || '%' 
           OR textNkjv LIKE '%' || :query || '%'
           OR bookNameEn LIKE '%' || :query || '%'
           OR bookNameBm LIKE '%' || :query || '%'
        ORDER BY bookNumber ASC, chapter ASC, verse ASC
        LIMIT 80
        """
    )
    suspend fun searchVersesByKeyword(query: String): List<BibleVerseEntity>

    @Query(
        """
        SELECT * FROM bible_verses 
        WHERE bookNumber = :bookNumber 
          AND chapter = :chapter 
          AND (:verse IS NULL OR verse = :verse)
        ORDER BY verse ASC
        """
    )
    suspend fun getVersesByReference(bookNumber: Int, chapter: Int, verse: Int?): List<BibleVerseEntity>

    @Query("SELECT * FROM verse_study ORDER BY updatedAt DESC")
    fun getAllStudyRecords(): Flow<List<VerseStudyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStudyRecord(record: VerseStudyEntity)

    @Query("DELETE FROM verse_study WHERE verseKey = :verseKey")
    suspend fun deleteStudyRecord(verseKey: String)
}
