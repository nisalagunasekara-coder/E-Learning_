package com.example.elearning.ui.student

import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.data.ActivityFeedRepository
import com.example.elearning.data.QuizRepository
import com.example.elearning.databinding.ActivityQuizBinding
import com.example.elearning.ui.common.setupBackToolbar
import com.example.elearning.ui.common.setupStudentBottomNav
import com.example.elearning.R
import com.example.elearning.data.db.DatabaseProvider

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private var quiz: QuizRepository.Quiz? = null
    private var index = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ensure DB is initialized
        DatabaseProvider.init(applicationContext)

        // Back button on toolbar
        setupBackToolbar(binding.toolbar, title = "Quiz")

        // Bottom navigation
        setupStudentBottomNav(binding.bottomNav.bottomNav, R.id.nav_quizzes)

        val id = intent.getIntExtra("quizId", -1)
        quiz = QuizRepository.get(id)
        if (quiz == null) {
            Toast.makeText(this, "Quiz not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        index = 0
        score = 0

        showCurrent()

        binding.btnNext.setOnClickListener {
            val selected = binding.rgOptions.checkedRadioButtonId
            if (selected == -1) {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selectedIndex = when (selected) {
                binding.rb1.id -> 0
                binding.rb2.id -> 1
                binding.rb3.id -> 2
                binding.rb4.id -> 3
                else -> -1
            }
            val q = quiz!!.questions[index]
            if (selectedIndex == q.correctIndex) score++

            index++
            if (index < quiz!!.questions.size) {
                binding.rgOptions.clearCheck()
                showCurrent()
            } else {
                val total = quiz!!.questions.size
                Toast.makeText(this, "Score: $score/$total", Toast.LENGTH_LONG).show()
                ActivityFeedRepository.add("Completed quiz '${quiz!!.title}' with score $score/$total")
                finish()
            }
        }
    }

    private fun showCurrent() {
        val q = quiz!!.questions[index]
        binding.tvProgress.text = "Question ${index + 1}/${quiz!!.questions.size}"
        binding.tvQuestion.text = q.text
        val opts = listOf(binding.rb1, binding.rb2, binding.rb3, binding.rb4)
        for (i in 0..3) {
            opts[i].text = q.options[i]
        }
        if (q.options.size < 4) {
            // Ensure 4 radio buttons visible; for safety
            opts.forEach { it.isEnabled = true }
        }
    }
}
