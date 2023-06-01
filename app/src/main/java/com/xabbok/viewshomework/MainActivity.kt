package com.xabbok.viewshomework

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Scene
import android.transition.TransitionManager
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.xabbok.viewshomework.ui.StatsView
import com.xabbok.viewshomework.ui.StatsViewData

class MainActivity : AppCompatActivity(R.layout.main_activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val views = listOf<StatsView>(
            findViewById<StatsView>(R.id.statsViewParallel),
            findViewById<StatsView>(R.id.statsViewSequential),
            findViewById<StatsView>(R.id.statsViewBidirectional)
        )

        views.forEach { statsView ->
            statsView.postDelayed({
                statsView.data = StatsViewData(
                    0F, listOf(
                        500F, 500F, 500F, 500F
                    )
                )
            }, 300)
        }

        val root = findViewById<ViewGroup>(R.id.root)

        val scene = Scene.getSceneForLayout(root, R.layout.end_scene, this)

        val animButtonStart = findViewById<Button>(R.id.animButtonStart)
        animButtonStart.setOnClickListener {
            TransitionManager.go(scene, ChangeBounds().apply {
                duration = 3000
            })
        }

        /*statsView.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.animation)
        )*/
    }
}