package com.supereditor.ultimateeditor

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoiceStudioActivity : AppCompatActivity() {
    
    private lateinit var btnBack: ImageButton
    private lateinit var btnHelp: ImageButton
    private lateinit var etScript: EditText
    private lateinit var tvCharCount: TextView
    private lateinit var spinnerLanguage: Spinner
    private lateinit var btnDownloadVoices: MaterialButton
    private lateinit var chipNeutral: Chip
    private lateinit var chipHappy: Chip
    private lateinit var chipSad: Chip
    private lateinit var chipAngry: Chip
    private lateinit var chipExcited: Chip
    private lateinit var chipWhisper: Chip
    private lateinit var btnPreview: MaterialButton
    private lateinit var btnGenerate: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var tvStatus: TextView
    
    private var selectedLanguage = "English"
    private var selectedEmotion = "Neutral"
    
    private val languages = arrayOf(
        "English (US)",
        "English (UK)",
        "Urdu",
        "Hindi",
        "Arabic",
        "Spanish",
        "French",
        "German",
        "Chinese",
        "Japanese",
        "Korean",
        "Portuguese",
        "Russian",
        "Turkish",
        "Italian",
        "Indonesian"
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_studio)
        
        initViews()
        setupLanguageSpinner()
        setupEmotionChips()
        setupClickListeners()
        setupTextWatcher()
    }
    
    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        btnHelp = findViewById(R.id.btnHelp)
        etScript = findViewById(R.id.etScript)
        tvCharCount = findViewById(R.id.tvCharCount)
        spinnerLanguage = findViewById(R.id.spinnerLanguage)
        btnDownloadVoices = findViewById(R.id.btnDownloadVoices)
        chipNeutral = findViewById(R.id.chipNeutral)
        chipHappy = findViewById(R.id.chipHappy)
        chipSad = findViewById(R.id.chipSad)
        chipAngry = findViewById(R.id.chipAngry)
        chipExcited = findViewById(R.id.chipExcited)
        chipWhisper = findViewById(R.id.chipWhisper)
        btnPreview = findViewById(R.id.btnPreview)
        btnGenerate = findViewById(R.id.btnGenerate)
        progressBar = findViewById(R.id.progressBar)
        tvStatus = findViewById(R.id.tvStatus)
    }
    
    private fun setupLanguageSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = adapter
        
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLanguage = languages[position]
                Toast.makeText(
                    this@VoiceStudioActivity,
                    "Selected: $selectedLanguage",
                    Toast.LENGTH_SHORT
                ).show()
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }
    
    private fun setupEmotionChips() {
        val chips = listOf(chipNeutral, chipHappy, chipSad, chipAngry, chipExcited, chipWhisper)
        
        chips.forEach { chip ->
            chip.setOnClickListener {
                // Uncheck all other chips
                chips.forEach { it.isChecked = false }
                // Check clicked chip
                chip.isChecked = true
                selectedEmotion = chip.text.toString()
                Toast.makeText(this, "Emotion: $selectedEmotion", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }
        
        btnHelp.setOnClickListener {
            showHelpDialog()
        }
        
        btnDownloadVoices.setOnClickListener {
            downloadVoices()
        }
        
        btnPreview.setOnClickListener {
            previewVoice()
        }
        
        btnGenerate.setOnClickListener {
            generateVoice()
        }
    }
    
    private fun setupTextWatcher() {
        etScript.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.length ?: 0
                tvCharCount.text = "$length / 5000 characters"
                
                if (length > 5000) {
                    tvCharCount.setTextColor(getColor(R.color.error))
                } else {
                    tvCharCount.setTextColor(getColor(R.color.text_tertiary))
                }
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    private fun showHelpDialog() {
        val helpMessage = """
            AI Voice Studio Help:
            
            1. Enter your script in the text box
            2. Use emotion tags for better results:
               [happy] Welcome to my channel!
               [sad] This is a sad moment
               [excited] Amazing news everyone!
            
            3. Select language
            4. Choose voice type
            5. Pick emotion
            6. Click Preview or Generate
            
            All features are 100% FREE!
        """.trimIndent()
        
        Toast.makeText(this, helpMessage, Toast.LENGTH_LONG).show()
    }
    
    private fun downloadVoices() {
        if (progressBar.visibility == View.VISIBLE) {
            Toast.makeText(this, "Download already in progress", Toast.LENGTH_SHORT).show()
            return
        }
        
        showProgress("Downloading voice packs...")
        
        CoroutineScope(Dispatchers.Main).launch {
            // Simulate download
            delay(2000)
            
            withContext(Dispatchers.Main) {
                hideProgress()
                Toast.makeText(
                    this@VoiceStudioActivity,
                    "Voice pack downloaded! (Demo)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun previewVoice() {
        val script = etScript.text.toString().trim()
        
        if (script.isEmpty()) {
            Toast.makeText(this, "Please enter a script first", Toast.LENGTH_SHORT).show()
            return
        }
        
        showProgress("Generating preview...")
        
        CoroutineScope(Dispatchers.Main).launch {
            // Simulate voice generation
            delay(1500)
            
            withContext(Dispatchers.Main) {
                hideProgress()
                Toast.makeText(
                    this@VoiceStudioActivity,
                    "Preview ready! (Demo)\nLanguage: $selectedLanguage\nEmotion: $selectedEmotion",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    private fun generateVoice() {
        val script = etScript.text.toString().trim()
        
        if (script.isEmpty()) {
            Toast.makeText(this, "Please enter a script first", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (script.length > 5000) {
            Toast.makeText(this, "Script is too long. Maximum 5000 characters", Toast.LENGTH_SHORT).show()
            return
        }
        
        showProgress("Generating AI voice with $selectedEmotion emotion...")
        
        CoroutineScope(Dispatchers.Main).launch {
            // Simulate voice generation process
            updateStatus("Parsing script...")
            delay(1000)
            
            updateStatus("Applying $selectedEmotion emotion...")
            delay(1500)
            
            updateStatus("Generating voice in $selectedLanguage...")
            delay(2000)
            
            updateStatus("Finalizing audio...")
            delay(1000)
            
            withContext(Dispatchers.Main) {
                hideProgress()
                Toast.makeText(
                    this@VoiceStudioActivity,
                    getString(R.string.voice_generated) + "\n\nThis is a demo. Full AI voice generation will be available in the next update!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    private fun showProgress(message: String) {
        progressBar.visibility = View.VISIBLE
        tvStatus.visibility = View.VISIBLE
        tvStatus.text = message
        btnPreview.isEnabled = false
        btnGenerate.isEnabled = false
    }
    
    private fun hideProgress() {
        progressBar.visibility = View.GONE
        tvStatus.visibility = View.GONE
        tvStatus.text = ""
        btnPreview.isEnabled = true
        btnGenerate.isEnabled = true
    }
    
    private suspend fun updateStatus(message: String) {
        withContext(Dispatchers.Main) {
            tvStatus.text = message
        }
    }
}
