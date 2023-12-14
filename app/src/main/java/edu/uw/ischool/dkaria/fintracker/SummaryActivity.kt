package edu.uw.ischool.dkaria.fintracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.Serializable
import java.util.*

data class Transaction(val date: String, val reference: String, val out: Double) : Serializable {
    // Add an empty constructor for Firebase to deserialize the data
    constructor() : this("", "", 0.0)
}
class SummaryActivity : ComponentActivity() {

    private val transactions = mutableListOf<Transaction>()
    private lateinit var database: DatabaseReference
    private lateinit var userId: String
    private lateinit var budgetTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("transactions")
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        budgetTextView = findViewById<TextView>(R.id.budget)
        val transactionsLayout = findViewById<LinearLayout>(R.id.transactions)
        val dateEditText = findViewById<EditText>(R.id.date)
        val referenceEditText = findViewById<EditText>(R.id.reference)
        val outEditText = findViewById<EditText>(R.id.out)
        val addButton = findViewById<Button>(R.id.add)
        val visualizeButton = findViewById<Button>(R.id.visualize)

//        val budgetString = intent.getStringExtra("budget")
//        val budget = if (budgetString.isNullOrBlank()) 0.0 else budgetString.toDouble()

        // Fetch transactions from Firebase
        fetchTransactionsFromFirebase()

        // Update budget initiall

        for (transaction in transactions) {
            val transactionTextView = TextView(this)
            transactionTextView.text =
                "Date: ${transaction.date}, Reference: ${transaction.reference}, Out: ${transaction.out}"
            transactionsLayout.addView(transactionTextView)
        }

        addButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val reference = referenceEditText.text.toString()
            val out = outEditText.text.toString().toDouble()

            val transaction = Transaction(date, reference, out)
            transactions.add(transaction)

            // Add the transaction to Firebase
            database.child(userId).push().setValue(transaction)

            val transactionTextView = TextView(this)
            transactionTextView.text =
                "Date: ${transaction.date}, Reference: ${transaction.reference}, Out: ${transaction.out}"
            transactionsLayout.addView(transactionTextView)

            // Update the budget when transactions are added
            updateBudget()

            // Clear the EditText fields after adding the transaction
            dateEditText.text.clear()
            referenceEditText.text.clear()
            outEditText.text.clear()
        }


        visualizeButton.setOnClickListener {
            val intent = Intent(this, VisualizeActivity::class.java)
            intent.putExtra("transactions", ArrayList(transactions))
            startActivity(intent)
        }
    }

    private fun fetchTransactionsFromFirebase() {
        database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactions.clear()

                for (childSnapshot in snapshot.children) {
                    val transaction = childSnapshot.getValue(Transaction::class.java)
                    transaction?.let { transactions.add(it) }
                }

                // Update the UI with fetched transactions
                updateBudget()
                updateUIWithTransactions()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun updateUIWithTransactions() {
        val transactionsLayout = findViewById<LinearLayout>(R.id.transactions)
        transactionsLayout.removeAllViews()

        for (transaction in transactions) {
            val transactionTextView = TextView(this)
            transactionTextView.text =
                "Date: ${transaction.date}, Reference: ${transaction.reference}, Out: ${transaction.out}"
            transactionsLayout.addView(transactionTextView)
        }
    }

    private fun updateBudget() {
        val budgetString = intent.getStringExtra("budget")
        val budget = if (budgetString.isNullOrBlank()) 0.0 else budgetString.toDouble()
        val sum = transactions.sumOf { it.out }
        val remaining = budget.minus(sum)
        budgetTextView.text = "Remaining budget: $remaining"
    }
}
