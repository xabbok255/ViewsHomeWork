package com.xabbok.viewshomework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xabbok.viewshomework.ui.StatsView

class MainActivity : AppCompatActivity(R.layout.main_activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val statsView = findViewById<StatsView>(R.id.statsView)

        statsView.data = listOf(500f, 500f, 500f, 500f)
    }
}