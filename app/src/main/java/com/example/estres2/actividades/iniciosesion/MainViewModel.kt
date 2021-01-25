package com.example.estres2.actividades.iniciosesion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private var _changeBoleta: MutableLiveData<String> = MutableLiveData()
    val boleta : LiveData<String>
        get() = _changeBoleta

    fun updateCurrentBoleta(boleta: String) {
        _changeBoleta.value = boleta
    }
}