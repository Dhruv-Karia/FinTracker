package edu.uw.ischool.dkaria.fintracker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val textViewName = findViewById<TextView>(R.id.textViewName)
        val textViewEmail = findViewById<TextView>(R.id.textViewEmail)

        // We should get this information from a database or user session
        textViewName.text = "John Doe"
        textViewEmail.text = "johndoe@gmail.com"
    }
}
