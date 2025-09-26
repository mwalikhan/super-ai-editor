package com.supereditor.ultimateeditor

import android.content.Context

class ModelManager(private val context: Context) {

    private val bundledModels = mapOf(
        "english_tts" to "tts_english_model",
        "urdu_tts" to "tts_urdu_model",
        "english_stt" to "stt_english_model"
    )

    fun getLocalModelPath(modelName: String): String? {
        if (bundledModels.containsKey(modelName)) {
            return "file:///android_asset/${bundledModels[modelName]}"
        }
        return null
    }
}
