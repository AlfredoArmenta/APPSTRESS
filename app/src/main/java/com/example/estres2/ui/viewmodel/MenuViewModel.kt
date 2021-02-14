package com.example.estres2.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel() {
    private var _updateRegisters: MutableLiveData<Boolean> = MutableLiveData()
    val updateRegisters: LiveData<Boolean>
        get() = _updateRegisters

    fun updateRegisterList(update: Boolean) {
        _updateRegisters.value = update
    }
}