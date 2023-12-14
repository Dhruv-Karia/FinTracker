package edu.uw.ischool.dkaria.fintracker

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class VisualizeActivity : ComponentActivity() {
    class DateValueFormatter : ValueFormatter() {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return dateFormat.format(Date(value.toLong()))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualize)

        val transactions = intent.getSerializableExtra("transactions") as? ArrayList<Transaction>

        if (!transactions.isNullOrEmpty()) {
            val lineChart = findViewById<LineChart>(R.id.lineChart)
            lineChart.setBackgroundColor(Color.WHITE)
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

            // Customize x-axis
            val xAxis = lineChart.xAxis
            xAxis.valueFormatter = DateValueFormatter()
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            // Remove numbers on top
            lineChart.axisRight.isEnabled = false
            dataSet.setDrawValues(false)
            lineChart.data = lineData
            lineChart.invalidate()
        } else {
            // Handle the case where transactions is null or empty
        }
    }
}