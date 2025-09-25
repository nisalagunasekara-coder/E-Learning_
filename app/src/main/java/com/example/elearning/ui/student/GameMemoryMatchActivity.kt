package com.example.elearning.ui.student

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.data.GamesRepository
import com.example.elearning.databinding.ActivityGameMemoryMatchBinding
import kotlin.random.Random
import com.example.elearning.ui.common.setupBackToolbar

class GameMemoryMatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameMemoryMatchBinding

    private val buttons by lazy {
        listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7
        )
    }

    private var symbols = mutableListOf<String>()
    private var firstIndex: Int? = null
    private var matched = mutableSetOf<Int>()
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameMemoryMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        setupBackToolbar(binding.toolbar, title = "Memory Match")

        startNewGame()
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener { onCardClicked(index, button) }
        }
    }

    private fun startNewGame() {
        val base = listOf("A", "B", "C", "D")
        symbols = (base + base).shuffled(Random(System.currentTimeMillis())).toMutableList()
        matched.clear()
        firstIndex = null
        score = 0
        binding.tvMemScore.text = "Score: $score"
        buttons.forEach { btn ->
            btn.isEnabled = true
            btn.text = "?"
        }
    }

    private fun onCardClicked(index: Int, button: Button) {
        if (matched.contains(index)) return
        button.text = symbols[index]

        val first = firstIndex
        if (first == null) {
            firstIndex = index
        } else {
            // disable inputs briefly
            buttons.forEach { it.isEnabled = false }
            button.postDelayed({
                checkMatch(first, index)
                buttons.forEachIndexed { i, b -> if (!matched.contains(i)) b.isEnabled = true }
            }, 500)
        }
    }

    private fun checkMatch(i: Int, j: Int) {
        if (symbols[i] == symbols[j] && i != j) {
            matched.add(i)
            matched.add(j)
            score += 4
            Toast.makeText(this, "Match!", Toast.LENGTH_SHORT).show()
        } else {
            // flip back
            buttons[i].text = "?"
            buttons[j].text = "?"
            Toast.makeText(this, "No match", Toast.LENGTH_SHORT).show()
        }
        binding.tvMemScore.text = "Score: $score"
        firstIndex = null

        if (matched.size == symbols.size) {
            GamesRepository.addMemoryScore(score)
            Toast.makeText(this, "All matched! +$score points", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
