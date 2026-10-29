package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.BibleSeedData
import com.example.data.DwibibleDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `verify complete 31102 verses in prepackaged SQLite database`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Dwibible", appName)
        assertEquals(66, BibleSeedData.allBooks.size)

        val db = DwibibleDatabase.getInstance(context)
        val dao = db.bibleDao()
        val totalVerses = dao.getVerseCount()
        assertEquals(31102, totalVerses)

        // Verify Genesis 1 has all 31 verses without any missing verses
        val gen1 = dao.getVersesByReference(1, 1, null)
        assertEquals(31, gen1.size)
        assertEquals(1, gen1.first().verse)
        assertEquals(31, gen1.last().verse)
    }
}
