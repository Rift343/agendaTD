package com.example.agenda

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import java.util.HashMap

class add_event : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.layout_add_event)
        val date = intent?.extras?.getString("date").toString()
        val textDate : TextView  = findViewById(R.id.date)
        textDate.text = date


        val descriptionText = findViewById<EditText>(R.id.description)

        val sharendPreference = getSharedPreferences(getString(R.string.agenda), MODE_PRIVATE)

        var jsonarray = JSONArray()
        val value = sharendPreference.getString(date,"Nothing to do today")
        descriptionText.setText("Nothing to do today")
        if (value != "Nothing to do today")
        {
            jsonarray = JSONArray(value)
        }

        val addEventButton = findViewById<Button>(R.id.button2)
        val mainMenuButton = findViewById<Button>(R.id.button)

        addEventButton.setOnClickListener {
            jsonarray.put(descriptionText.text)
            with (sharendPreference.edit()){
                putString(date, jsonarray.toString())
                apply()
            }
            val text = "Event added"
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(this, text, duration) // in Activity
            toast.show()
        }

        mainMenuButton.setOnClickListener {
            var intent_main_menu = Intent(this,MainActivity::class.java)
            startActivity(intent_main_menu)
        }



    }
}