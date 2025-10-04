package com.supereditor.ultimateeditor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    
    private lateinit var btnNewProject: MaterialButton
    private lateinit var btnVoiceStudio: MaterialButton
    private lateinit var btnOpenProject: MaterialButton
    private lateinit var btnSettings: MaterialButton
    
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            openEditor()
        } else {
            showPermissionDialog()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        setupClickListeners()
    }
    
    private fun initViews() {
        btnNewProject = findViewById(R.id.btnNewProject)
        btnVoiceStudio = findViewById(R.id.btnVoiceStudio)
        btnOpenProject = findViewById(R.id.btnOpenProject)
        btnSettings = findViewById(R.id.btnSettings)
    }
    
    private fun setupClickListeners() {
        btnNewProject.setOnClickListener {
            checkPermissionsAndStart()
        }
        
        btnVoiceStudio.setOnClickListener {
            openVoiceStudio()
        }
        
        btnOpenProject.setOnClickListener {
            Toast.makeText(this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        
        btnSettings.setOnClickListener {
            Toast.makeText(this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun checkPermissionsAndStart() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        }
        
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        
        if (notGranted.isEmpty()) {
            openEditor()
        } else {
            permissionLauncher.launch(notGranted.toTypedArray())
        }
    }
    
    private fun openEditor() {
        val intent = Intent(this, EditorActivity::class.java)
        startActivity(intent)
    }
    
    private fun openVoiceStudio() {
        val intent = Intent(this, VoiceStudioActivity::class.java)
        startActivity(intent)
    }
    
    private fun showPermissionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.permission_required))
            .setMessage(getString(R.string.permission_storage))
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->
                checkPermissionsAndStart()
            }
            .setNegativeButton(getString(R.string.dialog_cancel), null)
            .show()
    }
}
