package com.supereditor.ultimateeditor

class FairUseEditor {
    
    fun createFairUseClip(inputPath: String, startTime: String, duration: String, outputPath: String): Boolean {
        return EditorCore.trimVideo(inputPath, startTime, duration, outputPath)
    }
    
    fun addCommentary(videoPath: String, audioPath: String, outputPath: String): Boolean {
        val command = "-i $videoPath -i $audioPath -c:v copy -map 0:v:0 -map 1:a:0 $outputPath"
        return EditorCore.runFFmpeg(command)
    }
}
