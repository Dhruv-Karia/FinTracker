package edu.uw.ischool.dkaria.fintracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class EditExpenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_expense)

        val editTextExpenseAmount = findViewById<EditText>(R.id.editTextExpenseAmount)
        val editTextExpenseCategory = findViewById<EditText>(R.id.editTextExpenseCategory)
        val editTextExpenseDate = findViewById<EditText>(R.id.editTextExpenseDate)
        val buttonSaveExpense = findViewById<Button>(R.id.buttonSaveExpense)

        buttonSaveExpense.setOnClickListener {
            val amount = editTextExpenseAmount.text.toString().toFloatOrNull()
            val category = editTextExpenseCategory.text.toString()
            val dateString = editTextExpenseDate.text.toString()

            if (amount != null && category.isNotEmpty() && isValidDate(dateString)) {
                // We should save the expense data here
                // most likely might want to add it to a database or a list
            } else {
                // Show an error message or handle the invalid input
            }
        }
    }

    private fun isValidDate(dateStr: String): Boolean {
        return try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }
}
