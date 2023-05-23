package com.xabbok.viewshomework.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import kotlin.math.cos
import kotlin.math.sin

class StatsViewDrawBidirectionalImpl : StatsViewDrawInterface {
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
        var startAngleVar = startAngle + 45F
        realWeights.forEachIndexed { index, d ->
            paint.color = colors.getOrElse(index) { generateRandomColor() }
            canvas.drawArc(oval, startAngleVar - (d * animationProgress * 360F / 2F), (d * animationProgress * 360F) , false, paint)
            startAngleVar += d * 360F
        }

        //исправляем концы дуг
        startAngleVar = startAngle + 45F
        realWeights.forEachIndexed { index, d ->
            paint.color = colors.getOrElse(index) { generateRandomColor() }

            val degree = startAngleVar - (d * animationProgress * 360F / 2F).toDouble()

            val startX =
                oval.centerX() + oval.width() / 2F * cos(Math.toRadians(degree)).toFloat()
            val startY =
                oval.centerY() + oval.width() / 2F * sin(Math.toRadians(degree)).toFloat()

            canvas.drawPoint(startX, startY, paint)
            //canvas.drawArc(oval, startAngleVar - (d * animationProgress * 360F / 2F), Float.MIN_VALUE, false, paint)
            startAngleVar += d * 360F
        }
    }
}