package com.example.estres2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var _updateUserData = MutableLiveData<Boolean>()
    val updateUserData: LiveData<Boolean>
        get() = _updateUserData

    fun updateUserDataFunc(update: Boolean) {
        _updateUserData.value = update
    }

    init {
        _updateUserData.value = false
    }
}