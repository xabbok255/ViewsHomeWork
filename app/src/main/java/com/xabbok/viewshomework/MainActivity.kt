package com.xabbok.viewshomework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.xabbok.viewshomework.ui.StatsView
import com.xabbok.viewshomework.ui.StatsViewData
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity(R.layout.main_activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val statsView = findViewById<StatsView>(R.id.statsView)

        //проверка freePercent от 1 до 100
        lifecycleScope.launchWhenCreated {
            var napr = 1
            var perc = 0

            while (true) {
                statsView.data = StatsViewData(
                    if (napr > 0) perc.toFloat() else 100f - perc,
                    listOf(500f, 500f, 500f, 500f)
                )

                perc += 1
                if (perc > 100) {
                    perc = 0
                    napr *= -1
                    delay(100)
                }

                delay(5)
            }
        }
    }
}