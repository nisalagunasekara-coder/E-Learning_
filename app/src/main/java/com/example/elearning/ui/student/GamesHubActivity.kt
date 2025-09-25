package com.example.elearning.ui.student

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.data.GamesRepository
import com.example.elearning.databinding.ActivityGamesHubBinding
import com.example.elearning.ui.common.setupBackToolbar
import com.example.elearning.ui.common.setupStudentBottomNav
import com.example.elearning.R

class GamesHubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamesHubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesHubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button on toolbar
        setupBackToolbar(binding.toolbar, title = "Games")

        // Bottom navigation
        setupStudentBottomNav(binding.bottomNav.bottomNav, R.id.nav_games)

        binding.btnMcq.setOnClickListener {
            startActivity(Intent(this, GameMcqActivity::class.java))
        }
        binding.btnTf.setOnClickListener {
            startActivity(Intent(this, GameTrueFalseActivity::class.java))
        }
        binding.btnMemory.setOnClickListener {
            startActivity(Intent(this, GameMemoryMatchActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshScores()
    }

    private fun refreshScores() {
        val s = GamesRepository.scores
        binding.tvMcqScore.text = "Score: ${s.mcq}"
        binding.tvTfScore.text = "Score: ${s.tf}"
        binding.tvMemoryScore.text = "Score: ${s.memory}"
    }
}
