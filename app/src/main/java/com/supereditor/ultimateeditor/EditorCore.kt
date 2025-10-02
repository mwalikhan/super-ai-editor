package com.supereditor.ultimateeditor

import android.util.Log

class EditorCore {
    
    private val TAG = "EditorCore"
    
    fun processVideo(inputPath: String, outputPath: String) {
        Log.d(TAG, "Processing video from $inputPath to $outputPath")
    }
    
    fun getAppVersion(): String {
        return "1.0.0"
    }
}
