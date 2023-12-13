package edu.uw.ischool.dkaria.fintracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class ImportCSVActivity : ComponentActivity() {

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle the returned Uri
        if (uri != null) {
            Toast.makeText(this, "File selected: $uri", Toast.LENGTH_SHORT).show()

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

        importButton.setOnClickListener {
            // Open the file picker with a more general MIME type
            getContent.launch("*/*")
        }
    }
}
