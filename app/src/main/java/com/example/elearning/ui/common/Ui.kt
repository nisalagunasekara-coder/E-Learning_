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
import android.util.Log
import android.widget.Toast

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
                if (this !is StudentDashboardActivity) {
                    try {
                        Toast.makeText(this, "Opening Home...", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, StudentDashboardActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    } catch (e: Exception) {
                        Log.e("BottomNav", "Failed to open StudentDashboardActivity", e)
                        Toast.makeText(this, "Failed to open Home: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
                true
            }
            R.id.nav_pdfs -> {
                if (this !is PdfMaterialsActivity) {
                    try {
                        Toast.makeText(this, "Opening PDFs...", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, PdfMaterialsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    } catch (e: Exception) {
                        Log.e("BottomNav", "Failed to open PdfMaterialsActivity", e)
                        Toast.makeText(this, "Failed to open PDFs: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
                true
            }
            R.id.nav_quizzes -> {
                if (this !is QuizHubActivity) {
                    try {
                        Toast.makeText(this, "Opening Quizzes...", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, QuizHubActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    } catch (e: Exception) {
                        Log.e("BottomNav", "Failed to open QuizHubActivity", e)
                        Toast.makeText(this, "Failed to open Quizzes: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
                true
            }
            R.id.nav_games -> {
                if (this !is GamesHubActivity) {
                    try {
                        Toast.makeText(this, "Opening Games...", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, GamesHubActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    } catch (e: Exception) {
                        Log.e("BottomNav", "Failed to open GamesHubActivity", e)
                        Toast.makeText(this, "Failed to open Games: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
                true
            }
            else -> false
        }
    }
}
