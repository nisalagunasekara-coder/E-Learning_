package com.example.elearning.ui.student

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elearning.data.QuizRepository
import com.example.elearning.databinding.ActivityQuizHubBinding
import com.example.elearning.databinding.ItemQuizBinding
import com.example.elearning.ui.common.setupBackToolbar
import com.example.elearning.ui.common.setupStudentBottomNav
import com.example.elearning.R
import com.example.elearning.data.db.DatabaseProvider
import androidx.lifecycle.lifecycleScope
import android.widget.Toast
import kotlinx.coroutines.launch
import android.util.Log

class QuizHubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizHubBinding
    private val adapter = QuizAdapter { quizId ->
        val i = Intent(this, QuizActivity::class.java).apply {
            putExtra("quizId", quizId)
        }
        startActivity(i)
    }

    private val pickPdf = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            // Call real API in background; fallback to local mock on failure
            lifecycleScope.launch {
                try {
                    Toast.makeText(this@QuizHubActivity, "Generating quiz...", Toast.LENGTH_SHORT).show()
                    QuizRepository.generateQuizFromPdfApi(this@QuizHubActivity, it, title = "Quiz from PDF")
                } catch (e: Exception) {
                    Toast.makeText(this@QuizHubActivity, "API failed, using local generator", Toast.LENGTH_LONG).show()
                    QuizRepository.generateQuizFromPdf(it.toString(), title = "Quiz from PDF")
                } finally {
                    refresh()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizHubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init database for repositories
        DatabaseProvider.init(applicationContext)

        // Back button on toolbar
        setupBackToolbar(binding.toolbar, title = "Quizzes")

        // Bottom navigation
        try {
            Toast.makeText(this, "Quiz Hub ready", Toast.LENGTH_SHORT).show()
            setupStudentBottomNav(binding.bottomNav.bottomNav, R.id.nav_quizzes)
        } catch (e: Exception) {
            Log.e("QuizHub", "Bottom nav setup failed", e)
            Toast.makeText(this, "Bottom nav error: ${e.message}", Toast.LENGTH_LONG).show()
        }

        binding.recyclerQuizzes.layoutManager = LinearLayoutManager(this)
        binding.recyclerQuizzes.adapter = adapter

        binding.btnUploadPdfGenerate.setOnClickListener {
            pickPdf.launch(arrayOf("application/pdf"))
        }

        refresh()
    }

    private fun refresh() {
        adapter.submit(QuizRepository.list())
    }
}

private class QuizAdapter(
    val onClick: (Int) -> Unit
) : RecyclerView.Adapter<QuizVH>() {
    private val items = mutableListOf<QuizRepository.Quiz>()

    fun submit(list: List<QuizRepository.Quiz>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizVH {
        val binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizVH(binding, onClick)
    }

    override fun onBindViewHolder(holder: QuizVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

private class QuizVH(
    private val binding: ItemQuizBinding,
    private val onClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: QuizRepository.Quiz) {
        binding.tvTitle.text = item.title
        binding.btnStart.setOnClickListener { onClick(item.id) }
    }
}
