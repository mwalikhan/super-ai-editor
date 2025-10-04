package com.supereditor.ultimateeditor

import android.app.Application
import android.util.Log

class EditorApp : Application() {
    
    private val TAG = "EditorApp"
    
    override fun onCreate() {
        super.onCreate()
        
        Log.d(TAG, "Super AI Editor Pro Initialized")
        Log.d(TAG, "Version: ${EditorCore.getAppVersion()}")
        Log.d(TAG, "Total Features: ${EditorCore.getTotalFeatures()}")
        
        initializeApp()
    }
    
    private fun initializeApp() {
        Log.d(TAG, "Initializing app components...")
        
        try {
            createRequiredDirectories()
            Log.d(TAG, "App initialization complete")
        } catch (e: Exception) {
            Log.e(TAG, "Error during initialization", e)
        }
    }
    
    private fun createRequiredDirectories() {
        val dirs = listOf(
            "SuperAIEditor",
            "SuperAIEditor/Projects",
            "SuperAIEditor/Exports",
            "SuperAIEditor/Voices",
            "SuperAIEditor/Temp"
        )
        
        dirs.forEach { dirName ->
            val dir = getExternalFilesDir(null)?.resolve(dirName)
            if (dir?.exists() == false) {
                dir.mkdirs()
                Log.d(TAG, "Created directory: $dirName")
            }
        }
    }
}
