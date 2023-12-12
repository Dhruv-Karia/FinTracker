package edu.uw.ischool.dkaria.fintracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val bankNameEditText = findViewById<EditText>(R.id.editTextBankName)
        val accountNumberEditText = findViewById<EditText>(R.id.editTextAccountNumber)
        val routingNumberEditText = findViewById<EditText>(R.id.editTextRoutingNumber)
        val completeOnboardingButton = findViewById<Button>(R.id.buttonCompleteOnboarding)

        completeOnboardingButton.setOnClickListener {
            val bankName = bankNameEditText.text.toString()
            val accountNumber = accountNumberEditText.text.toString()
            val routingNumber = routingNumberEditText.text.toString()

            if (bankName.isEmpty() || accountNumber.isEmpty() || routingNumber.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Implement our onboarding logic here
                // For now we just show a toast message
                Toast.makeText(this, "Onboarding Successful!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
