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

    private var _updateNotificationAnalysis = MutableLiveData<Boolean>()
    val updateNotificationAnalysis: LiveData<Boolean>
        get() = _updateNotificationAnalysis

    private var _updateNotificationGraph: MutableLiveData<Boolean> = MutableLiveData()
    val updateNotificationGraph: LiveData<Boolean>
        get() = _updateNotificationGraph

    private var _updateNotificationAnalysisAndGraph: MutableLiveData<Boolean> = MutableLiveData()
    val updateNotificationAnalysisAndGraph: LiveData<Boolean>
        get() = _updateNotificationAnalysisAndGraph

    private var _test: MutableLiveData<Boolean> = MutableLiveData()
    val test: LiveData<Boolean>
        get() = _test

    fun updateUserDataFunc(update: Boolean) {
        _updateUserData.value = update
    }

    fun updateRegisterList(update: Boolean) {
        _updateRegisters.value = update
    }

    fun updateNotificationAnalysis(update: Boolean) {
        _updateNotificationAnalysis.value = update
    }

    fun updateNotificationGraph(update: Boolean) {
        _updateNotificationGraph.value = update
    }

    fun updateNotificationAnalysisAndGraph(update: Boolean) {
        _updateNotificationAnalysisAndGraph.value = update
    }

    fun updateTest(update: Boolean) {
        _test.value = update
    }

    init {
        _updateUserData.value = false
        _updateNotificationAnalysis.value = false
        _updateNotificationGraph.value = false
        _updateNotificationAnalysisAndGraph.value = false
    }
}