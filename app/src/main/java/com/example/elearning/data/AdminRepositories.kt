package com.example.elearning.data

object UserRepository {
    data class AppUser(var username: String, var role: String)
    private val users = mutableListOf(
        AppUser("admin", "Admin"),
        AppUser("lect1", "Lecturer"),
        AppUser("stud1", "Student"),
        AppUser("stud2", "Student")
    )

    fun list(): List<AppUser> = users
    fun add(username: String, role: String) { users.add(AppUser(username, role)) }
    fun remove(username: String) { users.removeAll { it.username.equals(username, true) } }
    fun update(oldUsername: String, newUsername: String, role: String) {
        users.find { it.username.equals(oldUsername, true) }?.apply {
            this.username = newUsername
            this.role = role
        }
    }
}

object ContentRepository {
    data class PendingItem(val id: Int, val desc: String)
    private val pending = mutableListOf<PendingItem>()
    private var nextId = 1

    fun enqueueApproval(desc: String) {
        pending.add(0, PendingItem(nextId++, desc))
    }
    fun listPending(): List<PendingItem> = pending
    fun approve(id: Int) { pending.removeAll { it.id == id } }
    fun reject(id: Int) { pending.removeAll { it.id == id } }

    fun totalApprovedCount(): Int {
        val enqueued = (nextId - 1).coerceAtLeast(0)
        return (enqueued - pending.size).coerceAtLeast(0)
    }
}

object SettingsRepository {
    var allowUploads: Boolean = true
    var maintenanceMode: Boolean = false
}

object StatsRepository {
    fun totalUsers(): Int = UserRepository.list().size
    fun activeStudents(): Int = UserRepository.list().count { it.role.equals("Student", true) }
    fun activeLecturers(): Int = UserRepository.list().count { it.role.equals("Lecturer", true) }
    fun uploadedMaterials(): Int = ContentRepository.totalApprovedCount()
}
