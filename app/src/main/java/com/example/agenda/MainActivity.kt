package com.example.agenda

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import org.json.JSONArray

import java.util.Calendar
import java.util.HashMap
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val calendar = Calendar.getInstance()
    private var selectedDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_layout)
        enableEdgeToEdge()
        val calendar_value = findViewById<CalendarView>(R.id.calendarView1)
        val button_add_event = findViewById<Button>(R.id.button2)
        val previewEvent = findViewById<LinearLayout>(R.id.eventView)


        calendar_value.setOnDateChangeListener { view, year, month, dayOfMonth ->

            calendar.set(year, month, dayOfMonth)
            selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)

            display(previewEvent)
        }


            button_add_event.setOnClickListener {
                var intent_add_event = Intent(this,add_event::class.java)
                intent_add_event.putExtra("date",selectedDate)
                startActivity(intent_add_event)
        }



        display(previewEvent)


    }

    fun display(previewEvent: LinearLayout) {
        val sharedPreferences = getSharedPreferences(getString(R.string.agenda), MODE_PRIVATE)
        val date = selectedDate
        val value = sharedPreferences.getString(date, "Nothing to do today")
        previewEvent.removeAllViews()

        if (value == "Nothing to do today" || value == "[]") {
            val textView = TextView(this).apply {
                text = "Nothing to do today"
                textSize = 16f
                setPadding(16, 16, 16, 16)
                setTextColor(Color.parseColor("#757575"))
                gravity = Gravity.CENTER
            }
            previewEvent.addView(textView)
        } else {
            val jsonArray = JSONArray(value)
            for (index in 0 until jsonArray.length()) {
                val event = jsonArray.getString(index)
                val eventLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    setPadding(16, 16, 16, 16)
                    background = if ((index + 1) % 2 == 0) {
                        ColorDrawable(Color.parseColor("#cdf1c7"))
                    } else {
                        ColorDrawable(Color.parseColor("#c7e6f1"))
                    }
                }

                val button = Button(this).apply {
                    text = "Suppression"

                    setOnClickListener {

                        jsonArray.remove(index);
                        Toast.makeText(context, "Suppresion complete", Toast.LENGTH_SHORT).show()
                        with (sharedPreferences.edit()){
                            putString(date, jsonArray.toString())
                            apply()
                        }
                        display(previewEvent)

                    }
                }

                val textView = TextView(this).apply {
                    text = event
                    textSize = 16f
                    setTextColor(Color.parseColor("#000000"))
                    setPadding(16, 0, 0, 0)
                }


                eventLayout.addView(textView)
                eventLayout.addView(button)
                previewEvent.addView(eventLayout)
            }
        }
    }


}

