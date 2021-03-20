package com.example.myhelloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Wann wird offensichtlich der Code dieser Methode onCreate(...) ausgeführt?
        // Wenn die Activity erstellt oder geladen wird

        // Und was ist der Wert von deren Argument savedInstanceState?
        // null
    }
}