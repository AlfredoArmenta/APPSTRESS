package com.example.estres2.util

object FileCharacteristics {
    lateinit var boleta: String
    lateinit var materia: String
    lateinit var fecha: String
    private var fc = ArrayList<Float>()
    private var fcTime = ArrayList<Float>()
    private var gsr: ArrayList<Float> = ArrayList()
    private var gsrTime: ArrayList<Float> = ArrayList()

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
        fc.add(fcObject.toFloat())
    }

    fun getFc(): ArrayList<Float> = fc

    fun setFcTime(fcTimeObject: String) {
        fcTime.add(fcTimeObject.toFloat())
    }

    fun getFcTime(): ArrayList<Float> = fcTime

    fun setGsr(gsrObject: String) {
        gsr.add(gsrObject.toFloat())
    }

    fun getGsr(): ArrayList<Float> = gsr

    fun setGsrTime(gsrTimeObject: String) {
        gsrTime.add(gsrTimeObject.toFloat())
    }

    fun getGsrTime(): ArrayList<Float> = gsrTime

    fun resetVariables() {
        fc.clear()
        fcTime.clear()
        gsr.clear()
        gsrTime.clear()
    }
}