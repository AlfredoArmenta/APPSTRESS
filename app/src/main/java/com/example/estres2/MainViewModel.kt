package com.example.estres2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var _updateUserData = MutableLiveData<Boolean>()
    val updateUserData: LiveData<Boolean>
        get() = _updateUserData

    private var _updateRegisters: MutableLiveData<Boolean> = MutableLiveData()
    val updateRegisters: LiveData<Boolean>
        get() = _updateRegisters

    private var _updateNotification = MutableLiveData<Boolean>()
    val updateNotification: LiveData<Boolean>
        get() = _updateNotification

    private var _updateStateNotification: MutableLiveData<Boolean> = MutableLiveData()
    val updateStateNotification: LiveData<Boolean>
        get() = _updateStateNotification

    fun updateUserDataFunc(update: Boolean) {
        _updateUserData.value = update
    }

    fun updateRegisterList(update: Boolean) {
        _updateRegisters.value = update
    }

    fun updateNotification(update: Boolean) {
        _updateNotification.value = update
    }

    fun updateStateNotification(update: Boolean) {
        _updateStateNotification.value = update
    }

    init {
        _updateUserData.value = false
        _updateNotification.value = false
    }
}