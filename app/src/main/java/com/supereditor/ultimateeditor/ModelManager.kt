package com.supereditor.ultimateeditor

import android.content.Context

class ModelManager(private val context: Context) {

    private val bundledModels = mapOf(
        "english_tts" to "tts_xtts_model",
        "urdu_tts" to "tts_xtts_model",
        "english_stt" to "stt_english_model"
    )

    fun getLocalModelPath(modelName: String): String? {
        return bundledModels[modelName]?.let { "file:///android_asset/$it" }
    }
}
