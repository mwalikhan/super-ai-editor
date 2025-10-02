package com.supereditor.ultimateeditor

class OriginalEditor {
    
    fun mergeVideos(video1: String, video2: String, outputPath: String): Boolean {
        val command = "-i $video1 -i $video2 -filter_complex concat=n=2:v=1:a=1 $outputPath"
        return EditorCore.runFFmpeg(command)
    }
    
    fun extractAudio(inputPath: String, outputPath: String): Boolean {
        val command = "-i $inputPath -vn -acodec copy $outputPath"
        return EditorCore.runFFmpeg(command)
    }
    
    fun convertFormat(inputPath: String, outputPath: String): Boolean {
        val command = "-i $inputPath $outputPath"
        return EditorCore.runFFmpeg(command)
    }
}
