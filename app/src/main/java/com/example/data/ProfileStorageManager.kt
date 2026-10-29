package com.example.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ProfileStorageManager {
    private const val PREFS_NAME = "dwibible_profile_prefs"
    private const val KEY_HAS_CUSTOM_PHOTO = "has_custom_photo"
    private const val KEY_PHOTO_UPDATED_AT = "photo_updated_at"
    private const val KEY_READER_NAME = "reader_display_name"
    private const val AVATAR_FILE_NAME = "dwibible_profile_avatar.jpg"

    fun getReaderName(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_READER_NAME, "Dwibible Reader") ?: "Dwibible Reader"
    }

    fun saveReaderName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_READER_NAME, name.trim().ifBlank { "Dwibible Reader" }).apply()
    }

    suspend fun loadSavedProfileBitmap(context: Context): ImageBitmap? = withContext(Dispatchers.IO) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val hasPhoto = prefs.getBoolean(KEY_HAS_CUSTOM_PHOTO, false)
        val avatarFile = File(context.filesDir, AVATAR_FILE_NAME)
        if (!hasPhoto || !avatarFile.exists()) return@withContext null

        runCatching {
            BitmapFactory.decodeFile(avatarFile.absolutePath)?.asImageBitmap()
        }.getOrNull()
    }

    suspend fun saveProfilePhotoFromUri(context: Context, uri: Uri): ImageBitmap? = withContext(Dispatchers.IO) {
        runCatching {
            val rawBitmap = context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            } ?: return@withContext null

            val maxDimension = 512
            val largestSide = max(rawBitmap.width, rawBitmap.height).coerceAtLeast(1)
            val scaledBitmap = if (largestSide > maxDimension) {
                val ratio = maxDimension.toFloat() / largestSide.toFloat()
                val targetW = (rawBitmap.width * ratio).toInt().coerceAtLeast(1)
                val targetH = (rawBitmap.height * ratio).toInt().coerceAtLeast(1)
                Bitmap.createScaledBitmap(rawBitmap, targetW, targetH, true)
            } else {
                rawBitmap
            }

            val avatarFile = File(context.filesDir, AVATAR_FILE_NAME)
            FileOutputStream(avatarFile).use { out ->
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                out.flush()
            }

            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit()
                .putBoolean(KEY_HAS_CUSTOM_PHOTO, true)
                .putLong(KEY_PHOTO_UPDATED_AT, System.currentTimeMillis())
                .apply()

            scaledBitmap.asImageBitmap()
        }.getOrNull()
    }

    suspend fun clearProfilePhoto(context: Context) = withContext(Dispatchers.IO) {
        runCatching {
            val avatarFile = File(context.filesDir, AVATAR_FILE_NAME)
            if (avatarFile.exists()) {
                avatarFile.delete()
            }
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit()
                .putBoolean(KEY_HAS_CUSTOM_PHOTO, false)
                .remove(KEY_PHOTO_UPDATED_AT)
                .apply()
        }
    }
}
