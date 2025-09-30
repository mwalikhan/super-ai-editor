package com.supereditor.ultimateeditor

import java.io.File

class AIVoiceEngine(private val modelManager: ModelManager) {

    private fun chunkText(script: String, chunkSize: Int = 1000) = script.chunked(chunkSize)

    fun synthesizeUnlimited(script: String, emotion: String, language: String): String {
        val modelPath = modelManager.getLocalModelPath("${language}_tts")
            ?: return "No TTS model for $language"
        val chunks = chunkText(script)
        val outputs = mutableListOf<String>()

        for ((i, part) in chunks.withIndex()) {
            val fname = "tts_${language}_chunk_${i}.wav"
            File(fname).writeText("VOICE[$language][$emotion]: $part")
            outputs.add(fname)
        }

        val final = "tts_${language}_output.wav"
        val listFile = File("concat.txt")
        listFile.writeText(outputs.joinToString("\n") { "file '$it'" })

        EditorCore().runFFmpeg("-f concat -safe 0 -i concat.txt -c copy $final")
        return final
    }
}
