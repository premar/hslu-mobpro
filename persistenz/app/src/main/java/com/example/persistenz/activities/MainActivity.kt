package com.example.persistenz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.persistenz.R
import com.example.persistenz.fragments.OverviewFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_host, OverviewFragment.newInstance()).commit()
        }
    }
}