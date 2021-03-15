package com.example.estres2.util

object FileCharacteristics {
    lateinit var boleta: String
    lateinit var materia: String
    lateinit var fecha: String
    private var fc: MutableList<Double> = ArrayList()
    private var fcTime: MutableList<Double> = ArrayList()
    private var gsr: MutableList<Double> = ArrayList()
    private var gsrTime: MutableList<Double> = ArrayList()

    fun setBoletaFile(boletaObject: String) {
        boleta = boletaObject
    }

    fun getBoletaFile(): String = boleta

    fun isBoletaInitialized(): Boolean {
        return FileCharacteristics::boleta.isInitialized
    }

    fun setMateriaFile(materiaObject: String) {
        materia = materiaObject
    }

    fun getMateriaFile(): String = materia

    fun isMateriaInitialized(): Boolean {
        return FileCharacteristics::materia.isInitialized
    }

    fun setFechaFile(fechaObject: String) {
        fecha = fechaObject
    }

    fun getFechaFile(): String = fecha

    fun isFechaInitialized(): Boolean {
        return FileCharacteristics::fecha.isInitialized
    }

    fun setFc(fcObject: String) {
        fc.add(fcObject.toDouble())
    }

    fun getFc(): MutableList<Double> = fc

    fun setFcTime(fcTimeObject: String) {
        fcTime.add(fcTimeObject.toDouble())
    }

    fun getFcTime(): MutableList<Double> = fcTime

    fun setGsr(gsrObject: String) {
        gsr.add(gsrObject.toDouble())
    }

    fun getGsr(): MutableList<Double> = gsr

    fun setGsrTime(gsrTimeObject: String) {
        gsrTime.add(gsrTimeObject.toDouble())
    }

    fun getGsrTime(): MutableList<Double> = gsrTime
}