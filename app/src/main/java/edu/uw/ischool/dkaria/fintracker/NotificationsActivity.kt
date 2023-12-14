package edu.uw.ischool.dkaria.fintracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class NotificationsActivity : AppCompatActivity() {

    private lateinit var phoneNumberEditText: EditText
    private lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber)
        confirmButton = findViewById(R.id.buttonConfirm)

        confirmButton.setOnClickListener {
            enrollForNotifications()
        }
    }

    private fun enrollForNotifications() {
        val phoneNumber = phoneNumberEditText.text.toString().trim()

        if (phoneNumber.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 1)
            } else {
                sendEnrollmentSMS(phoneNumber)
                // Start SummaryActivity with phoneNumber
                val intent = Intent(this, SummaryActivity::class.java)
                intent.putExtra("userPhoneNumber", phoneNumber)
                startActivity(intent)
            }
            showToast("Successfully enrolled")
        } else {
            showToast("Please enter a phone number")
        }
    }

    private fun sendEnrollmentSMS(phoneNumber: String) {
        val smsManager: SmsManager = SmsManager.getDefault()
        val enrollmentMessage = "You have been successfully enrolled to receive updates on your budget!"
        smsManager.sendTextMessage(phoneNumber, null, enrollmentMessage, null, null)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enrollForNotifications()
        } else {
            showToast("SMS permission is required to send notifications")
        }
    }
}


