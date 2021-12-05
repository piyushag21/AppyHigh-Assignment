package com.example.mynewsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class Location_Getter : AppCompatActivity() {

    // This activity is the launcher activity of my assignment. In this activity user will enter its Loaction to get Country specific news

    lateinit var changeActivityButton: Button
    lateinit var messageEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location__getter)

        messageEditText = findViewById(R.id.etMessage)
        changeActivityButton = findViewById(R.id.newActivityBtn)
        changeActivityButton.setOnClickListener {

            val intent = Intent(this@Location_Getter, MainActivity::class.java)
            val message = messageEditText.text.toString()
            intent.putExtra("message_key", message)
            startActivity(intent)

        }

    }
}