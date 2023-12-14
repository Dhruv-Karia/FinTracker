package edu.uw.ischool.dkaria.fintracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.BufferedReader
import java.io.InputStreamReader

class ImportCSVActivity : ComponentActivity() {

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle the returned Uri
        if (uri != null) {
            Toast.makeText(this, "File selected: $uri", Toast.LENGTH_SHORT).show()

            // Read CSV data and push to Firebase
            pushCSVDataToFirebase(uri)

            // Start BudgetActivity
            val intent = Intent(this, BudgetActivity::class.java)
            intent.putExtra("uri", uri.toString()) // Pass the Uri to the next activity
            startActivity(intent)
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_csv)

        val importButton = findViewById<Button>(R.id.import_csv)
        val skipButton = findViewById<Button>(R.id.skip_csv)

        importButton.setOnClickListener {
            // Open the file picker with a more general MIME type
            getContent.launch("*/*")
        }

        skipButton.setOnClickListener {
            // Start BudgetActivity
            val intent = Intent(this, BudgetActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pushCSVDataToFirebase(uri: Uri) {
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("transactions")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val transactions = readCSV(uri)

            for (transaction in transactions) {
                database.child(userId).push().setValue(transaction)
            }
        }
    }

    private fun readCSV(uri: Uri): List<Transaction> {
        val inputStream = contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val transactions = mutableListOf<Transaction>()
        var line: String? = reader.readLine()
        while (reader.readLine().also { line = it } != null) {
            val tokens = line?.split(",")
            if (tokens != null && tokens.size >= 3) {
                val date = tokens[0]
                val reference = tokens[1]
                val outString = tokens[3]
                val out = if (outString.isBlank()) 0.0 else outString.toDouble()

                transactions.add(Transaction(date, reference, out))
            }
        }

        reader.close()

        return transactions
    }
}
