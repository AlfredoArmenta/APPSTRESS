package com.example.estres2.util

import com.github.mikephil.charting.data.Entry

object EntropyObject {
    private var entropyUser: Double = 0.0
    private var fc = ArrayList<Entry>()
    private var gsr = ArrayList<Entry>()

    fun setEntropy(entropy: Double) {
        entropyUser = entropy
    }

    fun getEntropy(): Double = entropyUser

    fun setGraphFC(x: Float, y: Float) {
        fc.add(Entry(x, y))
    }

    fun getGraphFC(): ArrayList<Entry> = fc

    fun setGraphGSR(x: Float, y: Float) {
        gsr.add(Entry(x, y))
    }

    fun getGraphGSR(): ArrayList<Entry> = gsr

    fun resetVariables() {
        fc.clear()
        gsr.clear()
    }
}