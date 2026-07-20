package com.example.unitconvertor

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextValue: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var buttonConvert: Button
    private lateinit var textViewResult: TextView

    private lateinit var lengthAdapter: ArrayAdapter<CharSequence>
    private lateinit var weightAdapter: ArrayAdapter<CharSequence>
    private lateinit var tempAdapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)   // use the XML layout

        editTextValue = findViewById(R.id.editTextValue)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        buttonConvert = findViewById(R.id.buttonConvert)
        textViewResult = findViewById(R.id.textViewResult)

        val categoryAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.categories,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter

        lengthAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.length_units,
            android.R.layout.simple_spinner_item
        )
        lengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        weightAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.weight_units,
            android.R.layout.simple_spinner_item
        )
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        tempAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.temperature_units,
            android.R.layout.simple_spinner_item
        )
        tempAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val unit = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@MainActivity, "From: $unit", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val unit = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@MainActivity, "To: $unit", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val category = parent.getItemAtPosition(position).toString()
                when (category) {
                    "Length" -> {
                        spinnerFrom.adapter = lengthAdapter
                        spinnerTo.adapter = lengthAdapter
                    }
                    "Weight" -> {
                        spinnerFrom.adapter = weightAdapter
                        spinnerTo.adapter = weightAdapter
                    }
                    "Temperature" -> {
                        spinnerFrom.adapter = tempAdapter
                        spinnerTo.adapter = tempAdapter
                    }
                }
                spinnerFrom.setSelection(0)
                spinnerTo.setSelection(if (spinnerTo.count > 1) 1 else 0)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        buttonConvert.setOnClickListener {
            val inputStr = editTextValue.text.toString().trim()
            if (inputStr.isEmpty()) {
                Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val inputValue = inputStr.toDoubleOrNull()
            if (inputValue == null) {
                Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val category = spinnerCategory.selectedItem.toString()
            val fromUnit = spinnerFrom.selectedItem.toString()
            val toUnit = spinnerTo.selectedItem.toString()

            val result = when (category) {
                "Length" -> convertLength(inputValue, fromUnit, toUnit)
                "Weight" -> convertWeight(inputValue, fromUnit, toUnit)
                "Temperature" -> convertTemperature(inputValue, fromUnit, toUnit)
                else -> inputValue
            }

            textViewResult.text = "Result: $result $toUnit"
        }
    }

    private fun convertLength(value: Double, from: String, to: String): Double {
        var inMetres = when (from) {
            "Centimetre" -> value / 100.0
            "Metre" -> value
            "Inch" -> value * 0.0254
            else -> value
        }

        return when (to) {
            "Centimetre" -> inMetres * 100.0
            "Metre" -> inMetres
            "Inch" -> inMetres / 0.0254
            else -> value
        }
    }

    private fun convertWeight(value: Double, from: String, to: String): Double {
        var inKg = when (from) {
            "Gram" -> value / 1000.0
            "Kilogram" -> value
            "Pound" -> value * 0.45359237
            else -> value
        }

        return when (to) {
            "Gram" -> inKg * 1000.0
            "Kilogram" -> inKg
            "Pound" -> inKg / 0.45359237
            else -> value
        }
    }

    private fun convertTemperature(value: Double, from: String, to: String): Double {
        return when (from) {
            "Celsius" -> when (to) {
                "Fahrenheit" -> (value * 9 / 5) + 32
                "Kelvin" -> value + 273.15
                else -> value
            }
            "Fahrenheit" -> when (to) {
                "Celsius" -> (value - 32) * 5 / 9
                "Kelvin" -> (value - 32) * 5 / 9 + 273.15
                else -> value
            }
            "Kelvin" -> when (to) {
                "Celsius" -> value - 273.15
                "Fahrenheit" -> (value - 273.15) * 9 / 5 + 32
                else -> value
            }
            else -> value
        }
    }
}