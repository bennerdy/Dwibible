package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [BibleVerseEntity::class, VerseStudyEntity::class],
    version = 2,
    exportSchema = false
)
abstract class DwibibleDatabase : RoomDatabase() {
    abstract fun bibleDao(): BibleDao

    companion object {
        @Volatile
        private var INSTANCE: DwibibleDatabase? = null

        fun getInstance(context: Context): DwibibleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DwibibleDatabase::class.java,
                    "dwibible_complete_31102.db"
                )
                    .createFromAsset("dwibible_full.db")
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
