package com.xabbok.viewshomework.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class StatsViewDrawParallelImpl : StatsViewDrawInterface {
    override fun draw(
        data: StatsViewData,
        realWeights: List<Float>,
        canvas: Canvas,
        startAngle: Float,
        animationProgress: Float,
        paint: Paint,
        colors: List<Int>,
        oval: RectF
    ) {
        //рисуем дугу
        var startAngleVar = startAngle
        realWeights.forEachIndexed { index, d ->
            paint.color = colors.getOrElse(index) { generateRandomColor() }
            canvas.drawArc(oval, startAngleVar + animationProgress * 360F, d * 360F * animationProgress, false, paint)
            startAngleVar += d * 360F
        }

        //исправляем концы дуг
        startAngleVar = -90F
        realWeights.forEachIndexed { index, d ->
            paint.color = colors.getOrElse(index) { generateRandomColor() }
            canvas.drawArc(oval, startAngleVar + animationProgress * 360F, Float.MIN_VALUE, false, paint)
            startAngleVar += d * 360F
        }
    }
}