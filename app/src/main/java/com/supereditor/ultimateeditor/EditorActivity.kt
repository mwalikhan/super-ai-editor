package com.supereditor.ultimateeditor

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditorActivity : AppCompatActivity() {
    
    private lateinit var btnBack: ImageButton
    private lateinit var btnUndo: ImageButton
    private lateinit var btnRedo: ImageButton
    private lateinit var btnExport: ImageButton
    private lateinit var tvProjectName: TextView
    private lateinit var tvPreviewPlaceholder: TextView
    private lateinit var playerView: PlayerView
    
    private lateinit var btnImport: LinearLayout
    private lateinit var btnTrim: LinearLayout
    private lateinit var btnSplit: LinearLayout
    private lateinit var btnMerge: LinearLayout
    private lateinit var btnColor: LinearLayout
    private lateinit var btnEffects: LinearLayout
    private lateinit var btnAudio: LinearLayout
    private lateinit var btnVoice: LinearLayout
    private lateinit var btnSubtitles: LinearLayout
    
    private var player: ExoPlayer? = null
    private var currentVideoUri: Uri? = null
    
    private val videoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { loadVideo(it) }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        
        initViews()
        setupClickListeners()
        initializePlayer()
    }
    
    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        btnUndo = findViewById(R.id.btnUndo)
        btnRedo = findViewById(R.id.btnRedo)
        btnExport = findViewById(R.id.btnExport)
        tvProjectName = findViewById(R.id.tvProjectName)
        tvPreviewPlaceholder = findViewById(R.id.tvPreviewPlaceholder)
        playerView = findViewById(R.id.playerView)
        
        btnImport = findViewById(R.id.btnImport)
        btnTrim = findViewById(R.id.btnTrim)
        btnSplit = findViewById(R.id.btnSplit)
        btnMerge = findViewById(R.id.btnMerge)
        btnColor = findViewById(R.id.btnColor)
        btnEffects = findViewById(R.id.btnEffects)
        btnAudio = findViewById(R.id.btnAudio)
        btnVoice = findViewById(R.id.btnVoice)
        btnSubtitles = findViewById(R.id.btnSubtitles)
    }
    
    private fun setupClickListeners() {
        btnBack.setOnClickListener { onBackPressed() }
        
        btnUndo.setOnClickListener {
            Toast.makeText(this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        
        btnRedo.setOnClickListener {
            Toast.makeText(this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        
        btnExport.setOnClickListener { showExportDialog() }
        
        btnImport.setOnClickListener { openVideoPicker() }
        
        btnTrim.setOnClickListener {
            if (currentVideoUri != null) {
                Toast.makeText(this, "Trim: ${getString(R.string.coming_soon)}", Toast.LENGTH_SHORT).show()
            } else {
                showNoVideoError()
            }
        }
        
        btnSplit.setOnClickListener {
            if (currentVideoUri != null) {
                Toast.makeText(this, "Split: ${getString(R.string.coming_soon)}", Toast.LENGTH_SHORT).show()
            } else {
                showNoVideoError()
            }
        }
        
        btnMerge.setOnClickListener {
            Toast.makeText(this, "Merge: ${getString(R.string.coming_soon)}", Toast.LENGTH_SHORT).show()
        }
        
        btnColor.setOnClickListener {
            if (currentVideoUri != null) {
                Toast.makeText(this, "Color Grading: ${getString(R.string.coming_soon)}", Toast.LENGTH_SHORT).show()
            } else {
                showNoVideoError()
            }
        }
        
        btnEffects.setOnClickListener {
            if (currentVideoUri != null) {
                Toast.makeText(this, "Effects: ${getString(R.string.coming_soon)}", Toast.LENGTH_SHORT).show()
            } else {
                showNoVideoError()
            }
        }
        
        btnAudio.setOnClickListener {
            Toast.makeText(this, "Audio Mix: ${getString(R.string.coming_soon)}", Toast.LENGTH_SHORT).show()
        }
        
        btnVoice.setOnClickListener {
            Toast.makeText(this, "AI Voice: ${getString(R.string.coming_soon)}", Toast.LENGTH_SHORT).show()
        }
        
        btnSubtitles.setOnClickListener {
            if (currentVideoUri != null) {
                Toast.makeText(this, "Subtitles: ${getString(R.string.coming_soon)}", Toast.LENGTH_SHORT).show()
            } else {
                showNoVideoError()
            }
        }
    }
    
    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        // Video is ready
                    }
                    Player.STATE_ENDED -> {
                        // Video finished
                        player?.seekTo(0)
                        player?.pause()
                    }
                }
            }
        })
    }
    
    private fun openVideoPicker() {
        videoPickerLauncher.launch("video/*")
    }
    
    private fun loadVideo(uri: Uri) {
        currentVideoUri = uri
        
        val mediaItem = MediaItem.fromUri(uri)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
        
        tvPreviewPlaceholder.visibility = View.GONE
        playerView.visibility = View.VISIBLE
        
        Toast.makeText(this, "Video loaded successfully!", Toast.LENGTH_SHORT).show()
    }
    
    private fun showNoVideoError() {
        Toast.makeText(this, getString(R.string.no_video_selected), Toast.LENGTH_SHORT).show()
    }
    
    private fun showExportDialog() {
        if (currentVideoUri == null) {
            showNoVideoError()
            return
        }
        
        val qualities = arrayOf("720p (HD)", "1080p (Full HD)", "4K (Ultra HD)")
        
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.export_quality))
            .setItems(qualities) { _, which ->
                val quality = qualities[which]
                Toast.makeText(this, "Exporting in $quality: ${getString(R.string.coming_soon)}", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton(getString(R.string.dialog_cancel), null)
            .show()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
    
    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.confirm_exit))
            .setMessage("Your project will be saved automatically")
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton(getString(R.string.dialog_cancel), null)
            .show()
    }
}
