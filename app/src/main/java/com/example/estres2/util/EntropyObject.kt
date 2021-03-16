package com.example.estres2.util

import android.graphics.Color
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

object EntropyObject {
    private var entropyUser: Double = 0.0
    private val fc1: LineGraphSeries<DataPoint> = LineGraphSeries()
    private val gsr1: LineGraphSeries<DataPoint> = LineGraphSeries()

    fun setEntropy(entropy: Double) {
        entropyUser = entropy
    }

    fun getEntropy(): Double = entropyUser

    fun setGraphFC(x: Double, y: Double) {
        fc1.appendData(DataPoint(x, y), true, FileCharacteristics.getFc().size)
    }

    fun getGraphFC(): LineGraphSeries<DataPoint> = fc1

    fun setGraphGSR(x: Double, y: Double) {
        gsr1.appendData(DataPoint(x, y), true, FileCharacteristics.getGsr().size)
    }

    fun getGraphGSR(): LineGraphSeries<DataPoint> = gsr1

    init {
        fc1.color = Color.rgb(226, 91, 34)
        fc1.thickness = 6
        fc1.isDrawBackground = true
        fc1.backgroundColor = Color.argb(60, 95, 226, 156)
    }
}