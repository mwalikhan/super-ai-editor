package com.supereditor.ultimateeditor

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val core = EditorCore()
    private val voiceEngine by lazy { AIVoiceEngine(ModelManager(this)) }
    private val advanced = AdvancedFeatures(core)
    private val visual = AiVisualEditor(core)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnTrim).setOnClickListener {
            core.trimVideo("/sdcard/input.mp4", 0, 10)
        }
        findViewById<Button>(R.id.btnVoice).setOnClickListener {
            voiceEngine.synthesizeUnlimited("Hello! Narration test.", "energetic", "english")
        }
        findViewById<Button>(R.id.btnSubs).setOnClickListener {
            advanced.autoSubtitles("/sdcard/input.mp4", "english")
        }
        findViewById<Button>(R.id.btnBg).setOnClickListener {
            visual.changeBackground("/sdcard/input.mp4", "/sdcard/background.jpg")
        }
    }
}
