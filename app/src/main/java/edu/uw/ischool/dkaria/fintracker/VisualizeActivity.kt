package edu.uw.ischool.dkaria.fintracker

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import java.text.SimpleDateFormat
import java.util.*

class VisualizeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualize)

        val transactions = intent.getSerializableExtra("transactions") as? ArrayList<Transaction>

        if (!transactions.isNullOrEmpty()) {
            val lineChart = findViewById<LineChart>(R.id.lineChart)

            // Sort transactions by date
            val sortedTransactions = transactions.sortedBy { it.date }

            // Create entries for the line chart
            val entries = mutableListOf<Entry>()
            var cumulativeSum = 0f

            // Format for parsing date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            // Create entries with cumulative sum
            for ((index, transaction) in sortedTransactions.withIndex()) {
                cumulativeSum += transaction.out.toFloat()
                val date = dateFormat.parse(transaction.date)?.time?.toFloat() ?: index.toFloat()
                entries.add(Entry(date, cumulativeSum))
            }

            // Create a LineDataSet and LineData
            val dataSet = LineDataSet(entries, "Cumulative Out Amount")
            val lineData = LineData(dataSet)

            // Apply styling as needed
            dataSet.color = Color.BLUE
            dataSet.valueTextColor = Color.BLACK

            lineChart.data = lineData
            lineChart.invalidate()
        } else {
            // Handle the case where transactions is null or empty
        }
    }
}