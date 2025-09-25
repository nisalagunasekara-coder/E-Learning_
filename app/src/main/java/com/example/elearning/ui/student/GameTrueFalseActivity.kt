package com.example.elearning.ui.student

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.data.GamesRepository
import com.example.elearning.databinding.ActivityGameTrueFalseBinding
import com.example.elearning.ui.common.setupBackToolbar

class GameTrueFalseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameTrueFalseBinding

    private data class S(val text: String, val isTrue: Boolean)
    private val items = mutableListOf(
        S("The Earth revolves around the Sun", true),
        S("Water boils at 50°C", false),
        S("Humans need oxygen to live", true),
        S("2 x 3 = 5", false),
        S("Android is a mobile OS", true)
    )
    private var idx = 0
    private var local = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameTrueFalseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        setupBackToolbar(binding.toolbar, title = "True / False")

        items.shuffle()
        idx = 0
        local = 0
        show()

        binding.btnTrue.setOnClickListener { answer(true) }
        binding.btnFalse.setOnClickListener { answer(false) }
    }

    private fun show() {
        binding.tvTfScore.text = "Score: $local"
        binding.tvStatement.text = items[idx].text
    }

    private fun answer(choice: Boolean) {
        if (choice == items[idx].isTrue) {
            local += 3
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show()
        }
        idx++
        if (idx >= 5) {
            GamesRepository.addTfScore(local)
            Toast.makeText(this, "+$local points", Toast.LENGTH_LONG).show()
            finish()
        } else show()
    }
}
