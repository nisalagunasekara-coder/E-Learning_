package com.example.elearning.data

import com.example.elearning.model.Role
import com.example.elearning.model.User

object AuthRepository {
    // Accept any username/password and log in with the selected role
    fun login(role: Role, username: String, password: String): User? {
        val name = username.ifBlank { "guest" }
        return User(name, role)
    }
}
