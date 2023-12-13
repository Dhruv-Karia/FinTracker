package edu.uw.ischool.dkaria.fintracker

import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.io.BufferedReader
import java.io.InputStreamReader

data class Transaction(val date: String, val reference: String, val out: Double)

class SummaryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val budgetTextView = findViewById<TextView>(R.id.budget)
        val transactionsLayout = findViewById<LinearLayout>(R.id.transactions) // Assuming you have a LinearLayout with id 'transactions' in your layout

        val budgetString = intent.getStringExtra("budget") // Get the budget from the previous activity
        val budget = if (budgetString.isNullOrBlank()) 0.0 else budgetString.toDouble()
        val uri = Uri.parse(intent.getStringExtra("uri")) // Get the Uri from the previous activity

        val transactions = readCSV(uri) // Read the transactions from the CSV

        val sum = transactions.sumOf { it.out } // Calculate the sum of all transactions
        val remaining = budget?.minus(sum) // Subtract the sum from the budget

        budgetTextView.text = "Remaining budget: $remaining" // Display the remaining budget

        // Display each transaction
        for (transaction in transactions) {
            val transactionTextView = TextView(this)
            transactionTextView.text = "Date: ${transaction.date}, Reference: ${transaction.reference}, Out: ${transaction.out}"
            transactionsLayout.addView(transactionTextView)
        }
    }

    private fun readCSV(uri: Uri): List<Transaction> {
        val inputStream = contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val transactions = mutableListOf<Transaction>()
        var line: String? = reader.readLine() // Read the header
        while (reader.readLine().also { line = it } != null) {
            val tokens = line?.split(",")
            if (tokens != null) {
                if (tokens.size >= 3) {
                    val date = tokens[0]
                    val reference = tokens[1]
                    val outString = tokens[3]
                    val out = if (outString.isBlank()) 0.0 else outString.toDouble()

                    transactions.add(Transaction(date, reference, out)) // Add the transaction to the list
                }
            }
        }

        reader.close()

        return transactions
    }
}
