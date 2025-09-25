package com.example.elearning.ui.lecturer

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.data.LecturerQuizRepository
import com.example.elearning.databinding.ActivityCreateQuizBinding
import com.example.elearning.ui.common.setupBackToolbar

class CreateQuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button on toolbar
        setupBackToolbar(binding.toolbar, title = "Create Quiz")

        // Mock student list
        val students = listOf("stud1", "stud2", "stud3")
        binding.lvStudents.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, students)
        binding.lvStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)

        binding.btnCreate.setOnClickListener {
            val title = binding.etTitle.text?.toString()?.trim().orEmpty()
            val questionsText = binding.etQuestions.text?.toString()?.trim().orEmpty()
            if (title.isEmpty() || questionsText.isEmpty()) {
                Toast.makeText(this, "Enter title and questions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val qs = questionsText.lines().filter { it.isNotBlank() }
            val assigned = students.filterIndexed { index, _ -> binding.lvStudents.isItemChecked(index) }
            LecturerQuizRepository.create(title, qs, assigned)
            Toast.makeText(this, "Quiz created", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
