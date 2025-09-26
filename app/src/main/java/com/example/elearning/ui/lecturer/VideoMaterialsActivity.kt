package com.example.elearning.ui.lecturer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elearning.data.VideoRepository
import com.example.elearning.databinding.ActivityVideoMaterialsBinding
import com.example.elearning.databinding.ItemVideoBinding

class VideoMaterialsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoMaterialsBinding
    private val adapter = VideoAdapter { uriString ->
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(uriString), "video/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Open Video"))
    }

    private val pickVideo = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            VideoRepository.add(it)
            refresh()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoMaterialsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        binding.btnUpload.setOnClickListener {
            pickVideo.launch(arrayOf("video/*"))
        }

        refresh()
    }

    private fun refresh() {
        adapter.submit(VideoRepository.list())
    }
}

private class VideoAdapter(
    val onOpen: (String) -> Unit
) : RecyclerView.Adapter<VideoVH>() {
    private val items = mutableListOf<VideoRepository.VideoItem>()

    fun submit(list: List<VideoRepository.VideoItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVH {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoVH(binding, onOpen)
    }

    override fun onBindViewHolder(holder: VideoVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

private class VideoVH(
    private val binding: ItemVideoBinding,
    private val onOpen: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: VideoRepository.VideoItem) {
        binding.tvName.text = item.name
        binding.btnOpen.setOnClickListener { onOpen(item.uri) }
    }
}
