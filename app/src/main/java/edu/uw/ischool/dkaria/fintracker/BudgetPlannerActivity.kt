package edu.uw.ischool.dkaria.fintracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BudgetPlannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_planner)

        val editTextBudget = findViewById<EditText>(R.id.editTextBudget)
        val buttonSetBudget = findViewById<Button>(R.id.buttonSetBudget)
        val textViewCurrentBudget = findViewById<TextView>(R.id.textViewCurrentBudget)

        buttonSetBudget.setOnClickListener {
            val budget = editTextBudget.text.toString().toFloatOrNull()
            if (budget != null) {
                // We should save the budget value into a database
                textViewCurrentBudget.text = "Current Budget: $$budget"
            } else {
                editTextBudget.error = "Enter a valid budget"
            }
        }
    }
}
