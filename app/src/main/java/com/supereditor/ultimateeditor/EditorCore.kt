package com.supereditor.ultimateeditor

import android.util.Log
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg

class EditorCore {
    
    private val TAG = "EditorCore"
    
    fun executeFFmpegCommand(
        command: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        Log.d(TAG, "Executing FFmpeg command: $command")
        
        val result = FFmpeg.execute(command)
        
        when (result) {
            0 -> {
                Log.d(TAG, "FFmpeg command succeeded")
                onSuccess()
            }
            255 -> {
                Log.w(TAG, "FFmpeg command cancelled")
                onError("Command was cancelled")
            }
            else -> {
                val errorMessage = "FFmpeg failed with return code: $result"
                Log.e(TAG, errorMessage)
                onError(errorMessage)
            }
        }
    }
    
    fun getFFmpegVersion(): String {
        return try {
            val version = Config.getFFmpegVersion()
            Log.d(TAG, "FFmpeg version: $version")
            version ?: "Unknown"
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get FFmpeg version: ${e.message}")
            "Unknown"
        }
    }
    
    fun isFFmpegAvailable(): Boolean {
        return try {
            Config.getFFmpegVersion() != null
        } catch (e: Exception) {
            Log.e(TAG, "FFmpeg not available: ${e.message}")
            false
        }
    }
}
