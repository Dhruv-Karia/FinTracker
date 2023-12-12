package edu.uw.ischool.dkaria.fintracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.webkit.WebView

class ExpenseTrackerActivity : AppCompatActivity() {

    private val expensesData = mutableListOf<ExpenseEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_tracker)

        val webView = findViewById<WebView>(R.id.webview)
        setupWebView(webView)

        val editTextExpenseAmount = findViewById<EditText>(R.id.editTextExpenseAmount)
        val editTextExpenseDescription = findViewById<EditText>(R.id.editTextExpenseDescription)
        val buttonAddExpense = findViewById<Button>(R.id.buttonAddExpense)
        val textViewExpenses = findViewById<TextView>(R.id.textViewExpenses)
        val spinnerTimeFrame = findViewById<Spinner>(R.id.spinnerTimeFrame)

        setupTimeFrameSpinner(spinnerTimeFrame, webView)
        buttonAddExpense.setOnClickListener {
            val amount = editTextExpenseAmount.text.toString().toFloatOrNull()
            val description = editTextExpenseDescription.text.toString()

            if (amount != null && description.isNotEmpty()) {
                expensesData.add(ExpenseEntry(amount, description))
                editTextExpenseAmount.text.clear()
                editTextExpenseDescription.text.clear()
                textViewExpenses.text = expensesData.joinToString("\n") { "${it.amount} - ${it.description}" }
                updateWebViewChartData(webView)
            } else {
                editTextExpenseAmount.error = if(amount == null) "Enter valid amount" else null
                editTextExpenseDescription.error = if(description.isEmpty()) "Enter description" else null
            }
        }
    }

    private fun setupTimeFrameSpinner(spinner: Spinner, webView: WebView) {
        ArrayAdapter.createFromResource(
            this,
            R.array.time_frames, // We should make sure this array resource is defined
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                updateWebViewChartData(webView)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupWebView(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("file:///android_asset/your_chart_file.html") // We need to udate with HTML file path
    }

    private fun updateWebViewChartData(webView: WebView) {
        // We need to add the logic to update chart data in WebView
    }
}

data class ExpenseEntry(val amount: Float, val description: String)