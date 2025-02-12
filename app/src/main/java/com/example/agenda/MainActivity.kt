package com.example.agenda

import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
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

    fun display(previewEvent:LinearLayout){
        val sharendPreference = getSharedPreferences(getString(R.string.agenda), MODE_PRIVATE)
        var date = selectedDate
        var jsonarray = JSONArray()
        val value = sharendPreference.getString(date,"Nothing to do today")
        previewEvent.removeAllViews()
        if (value == "Nothing to do today")
        {
            val textView = TextView(this).apply {
                text = "Nothing to do today"
                textSize = 16f
                setPadding(16, 16, 16, 16)
            }
            previewEvent.addView(textView)
        }else{
            jsonarray = JSONArray(value)
            var index = 0
            while (index < jsonarray.length()){
                val textView = TextView(this).apply {
                    text = jsonarray.getString(index)
                    textSize = 16f
                    setPadding(16, 16, 16, 16)
                    setTextColor(Color.parseColor("#000000"))
                    if (((index+2) % 2) == 0){
                        setBackgroundColor(Color.parseColor("#cdf1c7"))
                    }else{
                        setBackgroundColor(Color.parseColor("#c7e6f1"))
                    }
                }
                previewEvent.addView(textView)
                index += 1
            }
        }



    }
}

