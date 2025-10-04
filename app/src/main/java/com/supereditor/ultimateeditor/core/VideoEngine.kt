package com.supereditor.ultimateeditor.core

import android.content.Context
import android.net.Uri
import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.arthenica.ffmpegkit.ReturnCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class VideoEngine(private val context: Context) {
    
    private val TAG = "VideoEngine"
    
    suspend fun trimVideo(
        inputUri: Uri,
        startTime: Long,
        endTime: Long,
        outputPath: String,
        onProgress: (Int) -> Unit,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputPath = getPathFromUri(inputUri)
            val duration = (endTime - startTime) / 1000.0
            val startSeconds = startTime / 1000.0
            
            val command = "-i $inputPath -ss $startSeconds -t $duration -c copy $outputPath"
            
            Log.d(TAG, "Trim command: $command")
            
            FFmpegKit.executeAsync(command) { session ->
                val returnCode = session.returnCode
                
                if (ReturnCode.isSuccess(returnCode)) {
                    Log.d(TAG, "Trim successful")
                    onProgress(100)
                    onComplete(true, outputPath)
                } else {
                    Log.e(TAG, "Trim failed: ${session.failStackTrace}")
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error trimming video", e)
            onComplete(false, e.message)
        }
    }
    
    suspend fun mergeVideos(
        videoUris: List<Uri>,
        outputPath: String,
        onProgress: (Int) -> Unit,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val fileListPath = File(context.cacheDir, "filelist.txt")
            val fileListContent = videoUris.joinToString("\n") { uri ->
                "file '${getPathFromUri(uri)}'"
            }
            fileListPath.writeText(fileListContent)
            
            val command = "-f concat -safe 0 -i ${fileListPath.absolutePath} -c copy $outputPath"
            
            Log.d(TAG, "Merge command: $command")
            
            FFmpegKit.executeAsync(command) { session ->
                val returnCode = session.returnCode
                
                if (ReturnCode.isSuccess(returnCode)) {
                    Log.d(TAG, "Merge successful")
                    onProgress(100)
                    onComplete(true, outputPath)
                } else {
                    Log.e(TAG, "Merge failed")
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error merging videos", e)
            onComplete(false, e.message)
        }
    }
    
    suspend fun applyColorGrading(
        inputUri: Uri,
        lutName: String,
        outputPath: String,
        onProgress: (Int) -> Unit,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputPath = getPathFromUri(inputUri)
            
            val filterCommand = when(lutName) {
                "Warm" -> "eq=contrast=1.1:brightness=0.05:saturation=1.2"
                "Cool" -> "eq=contrast=1.1:brightness=-0.05:saturation=0.9"
                "Vivid" -> "eq=contrast=1.2:saturation=1.5"
                "B&W" -> "hue=s=0"
                else -> "eq=contrast=1.0"
            }
            
            val command = "-i $inputPath -vf $filterCommand -c:a copy $outputPath"
            
            Log.d(TAG, "Color grading command: $command")
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    onProgress(100)
                    onComplete(true, outputPath)
                } else {
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error applying color grading", e)
            onComplete(false, e.message)
        }
    }
    
    suspend fun applyEffect(
        inputUri: Uri,
        effectName: String,
        intensity: Float,
        outputPath: String,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputPath = getPathFromUri(inputUri)
            
            val filterCommand = when(effectName) {
                "blur" -> "boxblur=${intensity * 10}:1"
                "sharpen" -> "unsharp=5:5:${intensity}:5:5:0"
                "vignette" -> "vignette=${intensity}"
                "glow" -> "glow=intensity=${intensity}:threshold=0.5"
                else -> "null"
            }
            
            val command = "-i $inputPath -vf $filterCommand -c:a copy $outputPath"
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    onComplete(true, outputPath)
                } else {
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            onComplete(false, e.message)
        }
    }
    
    suspend fun changeSpeed(
        inputUri: Uri,
        speed: Float,
        outputPath: String,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputPath = getPathFromUri(inputUri)
            val pts = 1.0 / speed
            
            val command = "-i $inputPath -filter:v setpts=${pts}*PTS -filter:a atempo=$speed $outputPath"
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    onComplete(true, outputPath)
                } else {
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            onComplete(false, e.message)
        }
    }
    
    suspend fun reverseVideo(
        inputUri: Uri,
        outputPath: String,
        onProgress: (Int) -> Unit,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputPath = getPathFromUri(inputUri)
            
            val command = "-i $inputPath -vf reverse -af areverse $outputPath"
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    onProgress(100)
                    onComplete(true, outputPath)
                } else {
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            onComplete(false, e.message)
        }
    }
    
    private fun getPathFromUri(uri: Uri): String {
        return FFmpegKitConfig.getSafParameterForRead(context, uri)
    }
}
