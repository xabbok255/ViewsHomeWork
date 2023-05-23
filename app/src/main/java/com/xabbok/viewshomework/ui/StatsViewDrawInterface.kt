package com.xabbok.viewshomework.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

interface StatsViewDrawInterface {
    fun draw(
        data: StatsViewData,
        realWeights: List<Float>,
        canvas: Canvas,
        startAngle: Float,
        animationProgress: Float,
        paint: Paint,
        colors: List<Int>,
        oval: RectF
    )
}