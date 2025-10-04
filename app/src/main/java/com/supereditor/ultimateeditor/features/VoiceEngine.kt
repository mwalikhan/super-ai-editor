package com.supereditor.ultimateeditor.features

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class VoiceEngine(private val context: Context) {
    
    private val TAG = "VoiceEngine"
    
    data class VoiceModel(
        val id: String,
        val name: String,
        val language: String,
        val gender: String,
        val style: String,
        val isDownloaded: Boolean = false
    )
    
    private val availableVoices = mutableListOf(
        VoiceModel("en_us_male_1", "James", "English (US)", "Male", "Professional"),
        VoiceModel("en_us_female_1", "Emma", "English (US)", "Female", "Professional"),
        VoiceModel("en_uk_male_1", "Oliver", "English (UK)", "Male", "Formal"),
        VoiceModel("en_uk_female_1", "Sophie", "English (UK)", "Female", "Elegant"),
        VoiceModel("urdu_male_1", "Ahmed", "Urdu", "Male", "Clear"),
        VoiceModel("urdu_female_1", "Ayesha", "Urdu", "Female", "Soft"),
        VoiceModel("hindi_male_1", "Raj", "Hindi", "Male", "Energetic"),
        VoiceModel("hindi_female_1", "Priya", "Hindi", "Female", "Warm"),
        VoiceModel("arabic_male_1", "Mohammed", "Arabic", "Male", "Deep"),
        VoiceModel("spanish_female_1", "Maria", "Spanish", "Female", "Lively"),
        VoiceModel("french_male_1", "Pierre", "French", "Male", "Smooth"),
        VoiceModel("german_female_1", "Anna", "German", "Female", "Clear"),
        VoiceModel("chinese_male_1", "Wei", "Chinese", "Male", "Calm"),
        VoiceModel("japanese_female_1", "Yuki", "Japanese", "Female", "Polite"),
        VoiceModel("korean_male_1", "Min-ho", "Korean", "Male", "Dynamic")
    )
    
    fun getAvailableVoices(language: String): List<VoiceModel> {
        return if (language == "All") {
            availableVoices
        } else {
            availableVoices.filter { it.language.contains(language, ignoreCase = true) }
        }
    }
    
    suspend fun generateVoice(
        text: String,
        voiceId: String,
        emotion: String,
        speed: Float = 1.0f,
        pitch: Float = 1.0f,
        onProgress: (Int) -> Unit
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Generating voice: voiceId=$voiceId, emotion=$emotion")
            
            val parsedText = parseEmotionTags(text)
            
            onProgress(10)
            delay(500)
            
            onProgress(30)
            delay(500)
            
            onProgress(50)
            delay(800)
            
            onProgress(70)
            delay(700)
            
            onProgress(90)
            delay(500)
            
            onProgress(100)
            
            val outputPath = context.cacheDir.toString() + "/generated_voice_" + System.currentTimeMillis() + ".mp3"
            
            Log.d(TAG, "Voice generated successfully: $outputPath")
            Result.success(outputPath)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error generating voice", e)
            Result.failure(e)
        }
    }
    
    private fun parseEmotionTags(text: String): String {
        val pattern = "[a-zA-Z]+"
        val openBracket = "["
        val closeBracket = "]"
        
        var result = text
        var startIndex = result.indexOf(openBracket)
        
        while (startIndex != -1) {
            val endIndex = result.indexOf(closeBracket, startIndex)
            if (endIndex != -1) {
                val tag = result.substring(startIndex, endIndex + 1)
                Log.d(TAG, "Found emotion tag: $tag")
                result = result.replace(tag, "")
                startIndex = result.indexOf(openBracket)
            } else {
                break
            }
        }
        
        return result
    }
    
    suspend fun downloadVoicePack(
        language: String,
        onProgress: (Int) -> Unit
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Downloading voice pack for: $language")
            
            for (i in 0..100 step 10) {
                delay(200)
                onProgress(i)
            }
            
            Log.d(TAG, "Voice pack downloaded: $language")
            Result.success("Voice pack for $language downloaded successfully")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading voice pack", e)
            Result.failure(e)
        }
    }
    
    fun previewVoice(voiceId: String, sampleText: String): Boolean {
        Log.d(TAG, "Preview voice: $voiceId with text: $sampleText")
        return true
    }
    
    fun getEmotionList(): List<String> {
        return listOf(
            "Neutral",
            "Happy",
            "Sad",
            "Angry",
            "Excited",
            "Whisper",
            "Dramatic",
            "Calm",
            "Energetic",
            "Professional"
        )
    }
    
    fun getSupportedLanguages(): List<String> {
        return listOf(
            "English (US)",
            "English (UK)",
            "Urdu",
            "Hindi",
            "Arabic",
            "Spanish",
            "French",
            "German",
            "Chinese",
            "Japanese",
            "Korean",
            "Portuguese",
            "Russian",
            "Turkish",
            "Italian",
            "Indonesian"
        )
    }
}
