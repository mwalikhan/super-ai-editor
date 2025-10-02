package com.supereditor.ultimateeditor

import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode

class EditorCore {
    
    fun executeFFmpegCommand(command: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        FFmpegKit.execute(command) { session ->
            val returnCode = session.returnCode
            if (ReturnCode.isSuccess(returnCode)) {
                onSuccess()
            } else {
                onError(session.failStackTrace ?: "Unknown error")
            }
        }
    }
    
    fun getFFmpegVersion(): String {
        var version = "Unknown"
        FFmpegKit.execute("-version") { session ->
            version = session.output ?: "Unknown"
        }
        return version
    }
}
