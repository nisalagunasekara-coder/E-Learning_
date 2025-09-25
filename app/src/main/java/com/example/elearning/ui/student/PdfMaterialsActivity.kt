package com.example.elearning.ui.student

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elearning.data.PdfRepository
import com.example.elearning.databinding.ActivityPdfMaterialsBinding
import com.example.elearning.databinding.ItemPdfBinding
import com.example.elearning.ui.common.setupBackToolbar
import com.example.elearning.ui.common.setupStudentBottomNav
import com.example.elearning.R

class PdfMaterialsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfMaterialsBinding
    private val adapter = PdfAdapter(
        onOpen = { uriString ->
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(Uri.parse(uriString), "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, "Open PDF"))
        }
    )

    private val pickPdf = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            PdfRepository.add(this, it)
            refresh()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfMaterialsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button on toolbar
        setupBackToolbar(binding.toolbar, title = "PDF Materials")

        // Bottom navigation
        setupStudentBottomNav(binding.bottomNav.bottomNav, R.id.nav_pdfs)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        binding.btnUpload.setOnClickListener {
            pickPdf.launch(arrayOf("application/pdf"))
        }

        refresh()
    }

    private fun refresh() {
        adapter.submit(PdfRepository.list())
    }
}

private class PdfAdapter(
    val onOpen: (String) -> Unit
) : RecyclerView.Adapter<PdfVH>() {
    private val items = mutableListOf<PdfRepository.PdfItem>()

    fun submit(list: List<PdfRepository.PdfItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfVH {
        val binding = ItemPdfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PdfVH(binding, onOpen)
    }

    override fun onBindViewHolder(holder: PdfVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

private class PdfVH(
    private val binding: ItemPdfBinding,
    private val onOpen: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PdfRepository.PdfItem) {
        binding.tvName.text = item.name
        binding.btnOpen.setOnClickListener { onOpen(item.uri) }
    }
}
