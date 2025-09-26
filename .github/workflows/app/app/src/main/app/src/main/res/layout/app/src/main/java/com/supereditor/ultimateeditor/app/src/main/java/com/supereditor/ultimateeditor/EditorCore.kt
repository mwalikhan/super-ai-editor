package com.supereditor.ultimateeditor

import com.arthenica.ffmpegkit.FFmpegKit

class EditorCore {

    fun trimVideo(input: String, start: Int, end: Int): String {
        val output = input.replace(".mp4", "_trimmed.mp4")
        val cmd = "-i $input -ss $start -to $end -c copy $output"
        runFFmpeg(cmd)
        return output
    }

    fun runFFmpeg(cmd: String) {
        FFmpegKit.executeAsync(cmd) { session ->
            println("FFmpeg finished: ${session.returnCode}")
        }
    }
}
