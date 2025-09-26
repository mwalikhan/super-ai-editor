package com.supereditor.ultimateeditor

class OriginalEditor(private val core: EditorCore) {

    fun applyLUT(video: String, lutFile: String): String {
        val output = video.replace(".mp4", "_lut.mp4")
        val cmd = "-i $video -vf lut3d=$lutFile $output"
        core.runFFmpeg(cmd)
        return output
    }

    fun greenScreen(video: String, bg: String): String {
        val output = video.replace(".mp4", "_chroma.mp4")
        val cmd = "-i $video -i $bg -filter_complex " +
                  "\"[0:v]chromakey=0x00FF00:0.3:0.1[fg];[1:v][fg]overlay[out]\" " +
                  "-map \"[out]\" -c:v libx264 -c:a copy $output"
        core.runFFmpeg(cmd)
        return output
    }
}
