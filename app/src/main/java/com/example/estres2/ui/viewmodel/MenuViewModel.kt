package com.example.estres2.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuViewModel: ViewModel() {
        private var _changeBoleta: MutableLiveData<String> = MutableLiveData()
        val boleta : LiveData<String>
            get() = _changeBoleta

        fun updateCurrentBoleta(boleta: String) {
            _changeBoleta.value = boleta
        }

    private var _updateRegisters: MutableLiveData<Boolean> = MutableLiveData()
    val updateRegisters: LiveData<Boolean>
        get() = _updateRegisters

    fun updateRegisterList(update: Boolean) {
        _updateRegisters.value = update
    }
}