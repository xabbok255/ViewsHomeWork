package com.xabbok.viewshomework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xabbok.viewshomework.ui.StatsView
import com.xabbok.viewshomework.ui.StatsViewData

class MainActivity : AppCompatActivity(R.layout.main_activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val statsView = findViewById<StatsView>(R.id.statsView)
        statsView.postDelayed({
            statsView.data = StatsViewData(0F, listOf(
                500F, 500F, 500F, 500F
            ))
        }, 300)

        /*statsView.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.animation)
        )*/
    }
}