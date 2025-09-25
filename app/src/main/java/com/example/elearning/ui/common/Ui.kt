package com.example.elearning.ui.common

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import com.example.elearning.R
import com.example.elearning.ui.student.StudentDashboardActivity
import com.example.elearning.ui.student.PdfMaterialsActivity
import com.example.elearning.ui.student.QuizHubActivity
import com.example.elearning.ui.student.GamesHubActivity

/**
 * Call from an Activity after setContentView, passing your toolbar from ViewBinding.
 * Optionally provide a title. Sets a standard back arrow and finishes the Activity on click.
 */
fun AppCompatActivity.setupBackToolbar(toolbar: MaterialToolbar, title: String? = null) {
    title?.let { toolbar.title = it }
    toolbar.setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)
    toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
}

/**
 * Configure the standard student bottom navigation.
 * Call with the BottomNavigationView instance and the selected item id.
 */
fun AppCompatActivity.setupStudentBottomNav(bottomNav: BottomNavigationView, selectedItemId: Int) {
    if (bottomNav.selectedItemId != selectedItemId) {
        bottomNav.selectedItemId = selectedItemId
    }

    bottomNav.setOnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                if (this !is StudentDashboardActivity) startActivity(Intent(this, StudentDashboardActivity::class.java))
                true
            }
            R.id.nav_pdfs -> {
                if (this !is PdfMaterialsActivity) startActivity(Intent(this, PdfMaterialsActivity::class.java))
                true
            }
            R.id.nav_quizzes -> {
                if (this !is QuizHubActivity) startActivity(Intent(this, QuizHubActivity::class.java))
                true
            }
            R.id.nav_games -> {
                if (this !is GamesHubActivity) startActivity(Intent(this, GamesHubActivity::class.java))
                true
            }
            else -> false
        }
    }
}
