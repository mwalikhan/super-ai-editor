package com.supereditor.ultimateeditor.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileManager(private val context: Context) {
    
    private val TAG = "FileManager"
    
    data class VideoInfo(
        val name: String,
        val duration: Long,
        val width: Int,
        val height: Int,
        val size: Long
    )
    
    fun createOutputFile(prefix: String, extension: String): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "${prefix}_${timeStamp}.$extension"
        
        val outputDir = File(context.getExternalFilesDir(null), "SuperAIEditor")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        
        return File(outputDir, fileName)
    }
    
    fun getVideoInfo(uri: Uri): VideoInfo? {
        return try {
            val projection = arrayOf(
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DISPLAY_NAME
            )
            
            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                    val widthIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)
                    val heightIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
                    val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                    val nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                    
                    val duration = cursor.getLong(durationIndex)
                    val width = cursor.getInt(widthIndex)
                    val height = cursor.getInt(heightIndex)
                    val size = cursor.getLong(sizeIndex)
                    val name = cursor.getString(nameIndex)
                    
                    VideoInfo(name, duration, width, height, size)
                } else null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting video info", e)
            null
        }
    }
    
    fun formatDuration(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        
        return when {
            hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
            else -> String.format("%02d:%02d", minutes, seconds % 60)
        }
    }
    
    fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> String.format("%.2f KB", bytes / 1024.0)
            bytes < 1024 * 1024 * 1024 -> String.format("%.2f MB", bytes / (1024.0 * 1024.0))
            else -> String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0))
        }
    }
    
    fun getCacheSize(): Long {
        return try {
            context.cacheDir.walkTopDown()
                .filter { it.isFile }
                .map { it.length() }
                .sum()
        } catch (e: Exception) {
            Log.e(TAG, "Error calculating cache size", e)
            0L
        }
    }
    
    fun clearCache(): Boolean {
        return try {
            context.cacheDir.deleteRecursively()
            Log.d(TAG, "Cache cleared successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing cache", e)
            false
        }
    }
    
    fun getOutputDirectory(): File {
        val dir = File(context.getExternalFilesDir(null), "SuperAIEditor")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }
    
    fun getProjectsDirectory(): File {
        val dir = File(context.getExternalFilesDir(null), "SuperAIEditor/Projects")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }
    
    fun getExportsDirectory(): File {
        val dir = File(context.getExternalFilesDir(null), "SuperAIEditor/Exports")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }
    
    fun getVoicesDirectory(): File {
        val dir = File(context.getExternalFilesDir(null), "SuperAIEditor/Voices")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }
    
    fun deleteFile(file: File): Boolean {
        return try {
            if (file.exists()) {
                file.delete()
                Log.d(TAG, "File deleted: ${file.name}")
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting file", e)
            false
        }
    }
    
    fun getAllProjects(): List<File> {
        val projectsDir = getProjectsDirectory()
        return projectsDir.listFiles()?.filter { it.isFile }?.sortedByDescending { it.lastModified() } ?: emptyList()
    }
    
    fun getAllExports(): List<File> {
        val exportsDir = getExportsDirectory()
        return exportsDir.listFiles()?.filter { it.isFile }?.sortedByDescending { it.lastModified() } ?: emptyList()
    }
    
    fun getTotalStorageUsed(): Long {
        val outputDir = getOutputDirectory()
        return try {
            outputDir.walkTopDown()
                .filter { it.isFile }
                .map { it.length() }
                .sum()
        } catch (e: Exception) {
            Log.e(TAG, "Error calculating storage", e)
            0L
        }
    }
}
