package com.example.elearning.data

import com.example.elearning.model.Role
import com.example.elearning.model.User

object AuthRepository {
    private data class Cred(val username: String, val password: String, val role: Role)

    private val users = listOf(
        Cred("admin", "admin123", Role.ADMIN),
        Cred("lect1", "pass123", Role.LECTURER),
        Cred("stud1", "pass123", Role.STUDENT)
    )

    fun login(role: Role, username: String, password: String): User? {
        val match = users.firstOrNull { it.username.equals(username, true) && it.password == password && it.role == role }
        return match?.let { User(it.username, it.role) }
    }
}
