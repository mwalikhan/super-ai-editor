package com.supereditor.ultimateeditor

class AiVisualEditor(private val core: EditorCore) {

    fun changeBackground(video: String, newBg: String): String {
        val output = video.replace(".mp4", "_bg.mp4")
        val cmd = "-i $video -i $newBg -filter_complex \"overlay=0:0\" $output"
        core.runFFmpeg(cmd)
        return output
    }
}
