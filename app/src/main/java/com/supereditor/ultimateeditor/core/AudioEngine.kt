package com.supereditor.ultimateeditor.core

import android.content.Context
import android.net.Uri
import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.arthenica.ffmpegkit.ReturnCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioEngine(private val context: Context) {
    
    private val TAG = "AudioEngine"
    
    suspend fun extractAudio(
        videoUri: Uri,
        outputPath: String,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputPath = FFmpegKitConfig.getSafParameterForRead(context, videoUri)
            
            val command = "-i $inputPath -vn -acodec libmp3lame -q:a 2 $outputPath"
            
            Log.d(TAG, "Extract audio command: $command")
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    Log.d(TAG, "Audio extracted successfully")
                    onComplete(true, outputPath)
                } else {
                    Log.e(TAG, "Audio extraction failed")
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting audio", e)
            onComplete(false, e.message)
        }
    }
    
    suspend fun mixAudio(
        audioTracks: List<Uri>,
        volumes: List<Float>,
        outputPath: String,
        onProgress: (Int) -> Unit,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputs = audioTracks.joinToString(" ") { uri ->
                "-i " + FFmpegKitConfig.getSafParameterForRead(context, uri)
            }
            
            val filters = StringBuilder()
            audioTracks.forEachIndexed { index, _ ->
                val vol = volumes.getOrElse(index) { 1.0f }
                filters.append("[$index:a]volume=$vol[a$index];")
            }
            
            audioTracks.indices.forEach { index ->
                filters.append("[a$index]")
            }
            filters.append("amix=inputs=${audioTracks.size}:duration=longest[out]")
            
            val command = "$inputs -filter_complex \"$filters\" -map \"[out]\" $outputPath"
            
            Log.d(TAG, "Mix audio command: $command")
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    onProgress(100)
                    onComplete(true, outputPath)
                } else {
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error mixing audio", e)
            onComplete(false, e.message)
        }
    }
    
    suspend fun addBackgroundMusic(
        videoUri: Uri,
        musicUri: Uri,
        musicVolume: Float,
        outputPath: String,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val videoPath = FFmpegKitConfig.getSafParameterForRead(context, videoUri)
            val musicPath = FFmpegKitConfig.getSafParameterForRead(context, musicUri)
            
            val command = "-i $videoPath -i $musicPath -filter_complex [1:a]volume=$musicVolume[a1];[0:a][a1]amix=inputs=2:duration=first[out] -map 0:v -map [out] -c:v copy $outputPath"
            
            Log.d(TAG, "Add background music command: $command")
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    onComplete(true, outputPath)
                } else {
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error adding background music", e)
            onComplete(false, e.message)
        }
    }
    
    suspend fun applyAudioEffect(
        audioUri: Uri,
        effectType: String,
        intensity: Float,
        outputPath: String,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputPath = FFmpegKitConfig.getSafParameterForRead(context, audioUri)
            
            val filterCommand = when(effectType) {
                "echo" -> "aecho=0.8:0.88:60:0.4"
                "reverb" -> "aecho=0.8:0.9:1000|1800:0.3|0.25"
                "bass_boost" -> "bass=g=${intensity * 10}"
                "treble_boost" -> "treble=g=${intensity * 10}"
                else -> "null"
            }
            
            val command = "-i $inputPath -af $filterCommand $outputPath"
            
            Log.d(TAG, "Apply audio effect command: $command")
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    onComplete(true, outputPath)
                } else {
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error applying audio effect", e)
            onComplete(false, e.message)
        }
    }
    
    suspend fun removeNoise(
        audioUri: Uri,
        level: Int,
        outputPath: String,
        onProgress: (Int) -> Unit,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputPath = FFmpegKitConfig.getSafParameterForRead(context, audioUri)
            
            val command = "-i $inputPath -af highpass=f=200,lowpass=f=3000 $outputPath"
            
            Log.d(TAG, "Remove noise command: $command")
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    onProgress(100)
                    onComplete(true, outputPath)
                } else {
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error removing noise", e)
            onComplete(false, e.message)
        }
    }
    
    suspend fun normalizeAudio(
        audioUri: Uri,
        targetLevel: Float,
        outputPath: String,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputPath = FFmpegKitConfig.getSafParameterForRead(context, audioUri)
            
            val command = "-i $inputPath -af loudnorm=I=-16:TP=-1.5:LRA=11 $outputPath"
            
            Log.d(TAG, "Normalize audio command: $command")
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    onComplete(true, outputPath)
                } else {
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error normalizing audio", e)
            onComplete(false, e.message)
        }
    }
    
    suspend fun changeSpeed(
        audioUri: Uri,
        speed: Float,
        maintainPitch: Boolean,
        outputPath: String,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputPath = FFmpegKitConfig.getSafParameterForRead(context, audioUri)
            
            val filterCommand = if (maintainPitch) {
                "atempo=$speed"
            } else {
                "asetrate=44100*$speed,aresample=44100"
            }
            
            val command = "-i $inputPath -af $filterCommand $outputPath"
            
            Log.d(TAG, "Change speed command: $command")
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    onComplete(true, outputPath)
                } else {
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error changing speed", e)
            onComplete(false, e.message)
        }
    }
    
    suspend fun addFadeEffect(
        audioUri: Uri,
        fadeInDuration: Long,
        fadeOutDuration: Long,
        outputPath: String,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputPath = FFmpegKitConfig.getSafParameterForRead(context, audioUri)
            val fadeInSec = fadeInDuration / 1000.0
            val fadeOutSec = fadeOutDuration / 1000.0
            
            val command = "-i $inputPath -af afade=t=in:st=0:d=$fadeInSec,afade=t=out:st=5:d=$fadeOutSec $outputPath"
            
            Log.d(TAG, "Add fade effect command: $command")
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    onComplete(true, outputPath)
                } else {
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error adding fade effect", e)
            onComplete(false, e.message)
        }
    }
    
    suspend fun applyEqualizer(
        audioUri: Uri,
        bands: Map<Int, Float>,
        outputPath: String,
        onComplete: (Boolean, String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val inputPath = FFmpegKitConfig.getSafParameterForRead(context, audioUri)
            
            val eqFilters = bands.entries.joinToString(",") { (freq, gain) ->
                "equalizer=f=$freq:width_type=h:width=200:g=$gain"
            }
            
            val command = "-i $inputPath -af $eqFilters $outputPath"
            
            Log.d(TAG, "Apply equalizer command: $command")
            
            FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    onComplete(true, outputPath)
                } else {
                    onComplete(false, session.failStackTrace)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error applying equalizer", e)
            onComplete(false, e.message)
        }
    }
}
