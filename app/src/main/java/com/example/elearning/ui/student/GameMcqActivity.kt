package com.example.elearning.ui.student

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.data.GamesRepository
import com.example.elearning.databinding.ActivityGameMcqBinding
import com.example.elearning.ui.common.setupBackToolbar

class GameMcqActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameMcqBinding
    private data class Q(val t: String, val opts: List<String>, val correct: Int)

    private val qs = mutableListOf(
        Q("Capital of France?", listOf("Berlin", "Madrid", "Paris", "Rome"), 2),
        Q("2 + 2 = ?", listOf("3", "4", "5", "6"), 1),
        Q("Sun rises in the?", listOf("North", "South", "East", "West"), 2),
        Q("Water formula?", listOf("CO2", "H2O", "O2", "NaCl"), 1),
        Q("Largest planet?", listOf("Earth", "Jupiter", "Mars", "Venus"), 1)
    )
    private var idx = 0
    private var localScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameMcqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        setupBackToolbar(binding.toolbar, title = "MCQ Challenge")

        qs.shuffle()
        idx = 0
        localScore = 0
        show()

        binding.btnOpt1.setOnClickListener { answer(0) }
        binding.btnOpt2.setOnClickListener { answer(1) }
        binding.btnOpt3.setOnClickListener { answer(2) }
        binding.btnOpt4.setOnClickListener { answer(3) }
    }

    private fun show() {
        val q = qs[idx]
        binding.tvGameQuestion.text = q.t
        binding.btnOpt1.text = q.opts[0]
        binding.btnOpt2.text = q.opts[1]
        binding.btnOpt3.text = q.opts[2]
        binding.btnOpt4.text = q.opts[3]
        binding.tvScore.text = "Score: $localScore"
    }

    private fun answer(sel: Int) {
        val q = qs[idx]
        if (sel == q.correct) {
            localScore += 5
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show()
        }
        idx++
        if (idx >= 5) {
            GamesRepository.addMcqScore(localScore)
            Toast.makeText(this, "Game over. +$localScore points", Toast.LENGTH_LONG).show()
            finish()
        } else {
            show()
        }
    }
}
