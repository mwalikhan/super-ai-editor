package com.supereditor.ultimateeditor

import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode

class EditorCore {
    
    private val TAG = "EditorCore"
    
    fun executeFFmpegCommand(
        command: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        Log.d(TAG, "Executing FFmpeg command: $command")
        
        FFmpegKit.execute(command) { session ->
            val returnCode = session.returnCode
            
            if (ReturnCode.isSuccess(returnCode)) {
                Log.d(TAG, "FFmpeg command succeeded")
                onSuccess()
            } else if (ReturnCode.isCancel(returnCode)) {
                Log.w(TAG, "FFmpeg command cancelled")
                onError("Command was cancelled")
            } else {
                val errorMessage = session.failStackTrace ?: "Unknown error"
                Log.e(TAG, "FFmpeg command failed: $errorMessage")
                onError(errorMessage)
            }
        }
    }
    
    fun getFFmpegVersion(callback: (String) -> Unit) {
        FFmpegKit.execute("-version") { session ->
            val output = session.output ?: "Unknown version"
            Log.d(TAG, "FFmpeg version: $output")
            callback(output)
        }
    }
    
    fun isFFmpegAvailable(): Boolean {
        return try {
            FFmpegKit.execute("-version")
            true
        } catch (e: Exception) {
            Log.e(TAG, "FFmpeg not available: ${e.message}")
            false
        }
    }
}
