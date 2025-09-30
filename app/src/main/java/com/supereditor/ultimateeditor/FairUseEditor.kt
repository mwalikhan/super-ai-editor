package com.supereditor.ultimateeditor

class FairUseEditor(private val core: EditorCore) {

    fun enforceFairUse(input: String, commentary: String): String {
        val clipped = core.trimVideo(input, 0, 20)
        val output = clipped.replace(".mp4", "_fairuse.mp4")
        val cmd = "-i $clipped -i $commentary " +
                "-vf drawtext=text='Review/Analysis':x=20:y=20 " +
                "-c:v libx264 -c:a aac -shortest $output"
        core.runFFmpeg(cmd)
        return output
    }
}
