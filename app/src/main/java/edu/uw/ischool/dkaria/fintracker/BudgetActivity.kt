package edu.uw.ischool.dkaria.fintracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity

class BudgetActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        val uri = intent.getStringExtra("uri") // Get the Uri from the previous activity

        val budgetEditText = findViewById<EditText>(R.id.budget)
        val saveButton = findViewById<Button>(R.id.save)

        saveButton.setOnClickListener {
            // TODO: Save the budget
            // Start SummaryActivity
            val intent = Intent(this, SummaryActivity::class.java)
            intent.putExtra("budget", budgetEditText.text.toString()) // Pass the budget to the next activity
            intent.putExtra("uri", uri) // Pass the Uri to the next activity
            startActivity(intent)
        }
    }
}
