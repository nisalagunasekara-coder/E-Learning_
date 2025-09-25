package com.example.elearning.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elearning.data.UserRepository
import com.example.elearning.databinding.ActivityUserManagementBinding
import com.example.elearning.databinding.ItemUserBinding

class UserManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserManagementBinding
    private val adapter = UserAdapter(
        onDelete = { username ->
            UserRepository.remove(username)
            Toast.makeText(this, "Removed $username", Toast.LENGTH_SHORT).show()
            refresh()
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        binding.btnAdd.setOnClickListener {
            val username = binding.etUsername.text?.toString()?.trim().orEmpty()
            val role = binding.spinnerRole.selectedItem?.toString() ?: "Student"
            if (username.isEmpty()) {
                Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show()
            } else {
                UserRepository.add(username, role)
                binding.etUsername.setText("")
                Toast.makeText(this, "Added $username ($role)", Toast.LENGTH_SHORT).show()
                refresh()
            }
        }

        refresh()
    }

    private fun refresh() {
        adapter.submit(UserRepository.list())
    }
}

private class UserAdapter(
    val onDelete: (String) -> Unit
) : RecyclerView.Adapter<UserVH>() {
    private val items = mutableListOf<UserRepository.AppUser>()

    fun submit(list: List<UserRepository.AppUser>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserVH(binding, onDelete)
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

private class UserVH(
    private val binding: ItemUserBinding,
    private val onDelete: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UserRepository.AppUser) {
        binding.tvUsername.text = item.username
        binding.tvRole.text = item.role
        binding.btnDelete.setOnClickListener { onDelete(item.username) }
    }
}
