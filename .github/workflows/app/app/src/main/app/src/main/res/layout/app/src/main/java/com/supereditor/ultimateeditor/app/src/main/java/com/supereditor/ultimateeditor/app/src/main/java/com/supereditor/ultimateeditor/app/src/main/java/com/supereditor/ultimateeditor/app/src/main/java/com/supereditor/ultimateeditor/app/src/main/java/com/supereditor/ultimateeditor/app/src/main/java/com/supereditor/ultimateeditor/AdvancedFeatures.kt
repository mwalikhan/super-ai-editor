package com.supereditor.ultimateeditor

class AdvancedFeatures(private val core: EditorCore) {

    fun stabilize(video: String): String {
        val output = video.replace(".mp4", "_stab.mp4")
        core.runFFmpeg("-i $video -vf deshake $output")
        return output
    }

    fun autoSubtitles(video: String, language: String): String {
        val modelPath = "stt_${language}_model"
        val output = video.replace(".mp4", "_subs.srt")
        println("Generating subtitles for $video using model $modelPath → $output")
        return output
    }
}
