package com.example.estres2.util

object FileCharacteristics {
    lateinit var boleta: String
    lateinit var materia: String
    lateinit var fecha: String
    lateinit var frecuencia: String
    private var fc = ArrayList<Float>()
    var fcMax = 0F
    private var gsr = ArrayList<Float>()
    var gsrMax = 0F


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

    fun setFrequency(frecuenciaObject: String) {
        frecuencia = frecuenciaObject
    }

    fun getFrequency(): String = frecuencia

    fun isFrequencyInitialized(): Boolean {
        return  FileCharacteristics::frecuencia.isInitialized
    }

    fun setFc(fcObject: String) {
        fc.add(fcObject.toFloat())
    }

    fun getFc(): ArrayList<Float> = fc

    fun getMaxFc(): Float = fc.maxOrNull()!!

    fun setGsr(gsrObject: String) {
        gsr.add(gsrObject.toFloat())
        gsrMax = if (gsrObject.toFloat() > fcMax) gsrObject.toFloat() else gsrMax
    }

    fun getGsr(): ArrayList<Float> = gsr

    fun getMaxGsr(): Float = gsr.maxOrNull()!!

    fun resetVariables() {
        fc.clear()
        gsr.clear()
    }
}
