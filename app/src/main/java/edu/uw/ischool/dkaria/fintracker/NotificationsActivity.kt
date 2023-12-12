package edu.uw.ischool.dkaria.fintracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationsActivity : AppCompatActivity() {

    private lateinit var notificationsAdapter: NotificationsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        recyclerView = findViewById(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize notifications data here
        val notifications = listOf("Notification 1", "Notification 2", "Notification 3") // This is just for example

        notificationsAdapter = NotificationsAdapter(notifications)
        recyclerView.adapter = notificationsAdapter
    }
}
