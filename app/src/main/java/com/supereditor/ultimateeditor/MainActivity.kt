package com.supereditor.ultimateeditor

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(50, 50, 50, 50)
        }
        
        val titleView = TextView(this).apply {
            text = "Super AI Editor"
            textSize = 32f
            gravity = Gravity.CENTER
        }
        
        val versionView = TextView(this).apply {
            text = "Version ${EditorCore.getAppVersion()}"
            textSize = 20f
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 0)
        }
        
        layout.addView(titleView)
        layout.addView(versionView)
        
        setContentView(layout)
    }
}
