package edu.uw.ischool.dkaria.fintracker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnExpenseTracker = findViewById<Button>(R.id.btnExpenseTracker)
        val btnBudgetPlanner = findViewById<Button>(R.id.btnBudgetPlanner)
        val btnNotifications = findViewById<Button>(R.id.btnNotifications)
        val btnProfile = findViewById<Button>(R.id.btnProfile)


        btnExpenseTracker.setOnClickListener {
            val intent = Intent(this, ExpenseTrackerActivity::class.java)
            startActivity(intent)
        }

        btnBudgetPlanner.setOnClickListener {
            val intent = Intent(this, BudgetPlannerActivity::class.java)
            startActivity(intent)
        }

        btnNotifications.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

    }
}
