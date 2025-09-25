package com.example.elearning.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elearning.data.ContentRepository
import com.example.elearning.databinding.ActivityContentManagementBinding
import com.example.elearning.databinding.ItemContentBinding

class ContentManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentManagementBinding
    private val adapter = ContentAdapter(
        onApprove = { id ->
            ContentRepository.approve(id)
            Toast.makeText(this, "Approved #$id", Toast.LENGTH_SHORT).show()
            refresh()
        },
        onReject = { id ->
            ContentRepository.reject(id)
            Toast.makeText(this, "Rejected #$id", Toast.LENGTH_SHORT).show()
            refresh()
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter
        refresh()
    }

    private fun refresh() {
        adapter.submit(ContentRepository.listPending())
    }
}

private class ContentAdapter(
    val onApprove: (Int) -> Unit,
    val onReject: (Int) -> Unit
) : RecyclerView.Adapter<ContentVH>() {
    private val items = mutableListOf<ContentRepository.PendingItem>()

    fun submit(list: List<ContentRepository.PendingItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentVH {
        val binding = ItemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContentVH(binding, onApprove, onReject)
    }

    override fun onBindViewHolder(holder: ContentVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

private class ContentVH(
    private val binding: ItemContentBinding,
    private val onApprove: (Int) -> Unit,
    private val onReject: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ContentRepository.PendingItem) {
        binding.tvDesc.text = "#${item.id} ${item.desc}"
        binding.btnApprove.setOnClickListener { onApprove(item.id) }
        binding.btnReject.setOnClickListener { onReject(item.id) }
    }
}
