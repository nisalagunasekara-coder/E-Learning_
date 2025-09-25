package com.example.elearning.ui.lecturer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elearning.data.ProgressRepository
import com.example.elearning.databinding.ActivityStudentProgressBinding
import com.example.elearning.databinding.ItemProgressBinding

class StudentProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentProgressBinding
    private val adapter = ProgressAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter
        adapter.submit(ProgressRepository.list())
    }
}

private class ProgressAdapter : RecyclerView.Adapter<ProgressVH>() {
    private val items = mutableListOf<ProgressRepository.StudentProgress>()

    fun submit(list: List<ProgressRepository.StudentProgress>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressVH {
        val binding = ItemProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProgressVH(binding)
    }

    override fun onBindViewHolder(holder: ProgressVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

private class ProgressVH(private val binding: ItemProgressBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ProgressRepository.StudentProgress) {
        binding.tvStudent.text = item.student
        binding.tvQuizzes.text = "Quizzes Taken: ${item.quizzesTaken}"
        binding.tvAvg.text = "Avg Score: ${item.avgScore}%"
    }
}
