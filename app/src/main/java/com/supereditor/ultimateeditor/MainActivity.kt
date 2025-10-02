package com.supereditor.ultimateeditor

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(50, 50, 50, 50)
                setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
            }
            
            val titleView = TextView(this).apply {
                text = "Super AI Editor"
                textSize = 32f
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                gravity = Gravity.CENTER
            }
            
            val versionView = TextView(this).apply {
                text = "Version ${EditorCore.getAppVersion()}"
                textSize = 20f
                setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                gravity = Gravity.CENTER
                setPadding(0, 20, 0, 0)
            }
            
            val statusView = TextView(this).apply {
                text = "App is running successfully!"
                textSize = 16f
                setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark))
                gravity = Gravity.CENTER
                setPadding(0, 40, 0, 0)
            }
            
            layout.addView(titleView)
            layout.addView(versionView)
            layout.addView(statusView)
            
            setContentView(layout)
            
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback simple view
            val errorText = TextView(this).apply {
                text = "Error: ${e.message}"
                textSize = 16f
                gravity = Gravity.CENTER
                setPadding(50, 50, 50, 50)
            }
            setContentView(errorText)
        }
    }
}
