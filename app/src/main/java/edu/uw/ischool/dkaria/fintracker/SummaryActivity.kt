package edu.uw.ischool.dkaria.fintracker

import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.content.Intent
import java.io.Serializable

data class Transaction(val date: String, val reference: String, val out: Double) : Serializable {
    constructor() : this("", "", 0.0)
}

class SummaryActivity : ComponentActivity() {

    private val transactions = mutableListOf<Transaction>()
    private lateinit var database: DatabaseReference
    private lateinit var userId: String
    private lateinit var budgetTextView: TextView
    private var userPhoneNumber: String? = null // Store the user's phone number

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        database = FirebaseDatabase.getInstance().getReference("transactions")
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        budgetTextView = findViewById<TextView>(R.id.budget)
        val transactionsLayout = findViewById<LinearLayout>(R.id.transactions)
        val dateEditText = findViewById<EditText>(R.id.date)
        val referenceEditText = findViewById<EditText>(R.id.reference)
        val outEditText = findViewById<EditText>(R.id.out)
        val addButton = findViewById<Button>(R.id.add)
        val visualizeButton = findViewById<Button>(R.id.visualize)

        userPhoneNumber = intent.getStringExtra("userPhoneNumber") // Retrieve phone number

        fetchTransactionsFromFirebase()

        addButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val reference = referenceEditText.text.toString()
            val out = outEditText.text.toString().toDouble()

            val transaction = Transaction(date, reference, out)
            transactions.add(transaction)

            database.child(userId).push().setValue(transaction)

            updateUIWithTransaction(transaction)
            updateBudget()

            dateEditText.text.clear()
            referenceEditText.text.clear()
            outEditText.text.clear()
        }

        val receiveNotificationsButton = findViewById<Button>(R.id.receiveNotifications)
        receiveNotificationsButton.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
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
            updateUIWithTransaction(transaction)
        }
    }

    private fun updateUIWithTransaction(transaction: Transaction) {
        val transactionsLayout = findViewById<LinearLayout>(R.id.transactions)
        val transactionTextView = TextView(this)
        transactionTextView.text =
            "Date: ${transaction.date}, Reference: ${transaction.reference}, Out: ${transaction.out}"
        transactionsLayout.addView(transactionTextView)
    }

    private fun updateBudget() {
        val budgetString = intent.getStringExtra("budget")
        val originalBudget = if (budgetString.isNullOrBlank()) 0.0 else budgetString.toDouble()
        val sum = transactions.sumOf { it.out }
        val remaining = originalBudget - sum
        budgetTextView.text = "Remaining budget: $remaining"

        if (remaining <= originalBudget * 0.1) {
            sendBudgetWarningSMS()
        }
    }

    private fun sendBudgetWarningSMS() {
        userPhoneNumber?.let { phoneNumber ->
            val smsManager: SmsManager = SmsManager.getDefault()
            val warningMessage = "Fintrack budget almost exceeded, watch spending closely!"
            smsManager.sendTextMessage(phoneNumber, null, warningMessage, null, null)
        }
    }
}

