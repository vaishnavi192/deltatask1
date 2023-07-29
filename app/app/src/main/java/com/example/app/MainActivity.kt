package com.example.app
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layoutone)
        val btn =findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            val intent = Intent(this, Mainactivitytwo::class.java)
            startActivity(intent)
        }
        }
    }








   
