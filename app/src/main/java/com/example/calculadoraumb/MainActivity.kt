package com.example.calculadoraumb

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var textViewResult: TextView? = null
    private var currentNumber = ""
    private var operation = ""
    private var result = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewResult = findViewById(R.id.textViewResult)

        val buttonIds = intArrayOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6, R.id.button7,
            R.id.button8, R.id.button9, R.id.buttonAdd, R.id.buttonSubtract,
            R.id.buttonMultiply, R.id.buttonDivide, R.id.buttonEquals,
            R.id.buttonC, R.id.buttonDelete
        )

        for (id in buttonIds) {
            findViewById<Button>(id)?.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        if (v is Button) {
            when (val buttonText = v.text.toString()) {
                "+", "-", "×", "÷" -> handleOperation(buttonText)
                "=" -> handleEquals()
                "AC" -> handleClear()
                "DEL" -> handleDelete()
                else -> handleNumber(buttonText)
            }
        }
    }

    private fun formatNumber(number: String): String {
        return try {
            val numberFormat = NumberFormat.getNumberInstance(Locale.US)
            val parsed = numberFormat.parse(number)
            numberFormat.format(parsed)
        } catch (e: Exception) {
            number
        }
    }

    private fun handleOperation(op: String) {
        if (currentNumber.isNotEmpty()) {
            if (operation.isNotEmpty()) {
                calculate()
            } else {
                result = currentNumber.replace(",", "").toDouble().toInt()
            }
            operation = op
            currentNumber = ""
        }
    }

    private fun handleEquals() {
        if (currentNumber.isNotEmpty() && operation.isNotEmpty()) {
            calculate()
            operation = ""
        }
    }

    private fun handleNumber(number: String) {
        currentNumber += number
        textViewResult?.text = formatNumber(currentNumber)
    }

    private fun handleClear() {
        currentNumber = ""
        operation = ""
        result = 0
        textViewResult?.text = "0"
    }

    private fun handleDelete() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = currentNumber.replace(",", "").dropLast(1)
            textViewResult?.text = if (currentNumber.isEmpty()) "0" else formatNumber(currentNumber)
        }
    }

    private fun calculate() {
        if (currentNumber.isNotEmpty()) {
            val secondNumber = currentNumber.replace(",", "").toDouble().toInt()
            result = when (operation) {
                "+" -> result + secondNumber
                "-" -> result - secondNumber
                "×" -> result * secondNumber
                "÷" -> if (secondNumber != 0) result / secondNumber else {
                    textViewResult?.text = "Error"
                    return
                }
                else -> return
            }
            textViewResult?.text = formatNumber(result.toString())
            currentNumber = result.toString()
        }
    }
}