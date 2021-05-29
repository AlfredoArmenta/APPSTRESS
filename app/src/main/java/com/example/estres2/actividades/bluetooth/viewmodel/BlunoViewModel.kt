package com.example.estres2.actividades.bluetooth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BlunoViewModel : ViewModel() {
    private var _updateWearables: MutableLiveData<Boolean> = MutableLiveData()
    val updateWearables: LiveData<Boolean> get() = _updateWearables

    private var _showNotificationCommunicationIsConnected: MutableLiveData<Boolean> = MutableLiveData()
    val showNotificationCommunicationIsConnected: LiveData<Boolean> get() = _showNotificationCommunicationIsConnected

    fun updateWearablesList(update: Boolean) {
        _updateWearables.value = update
    }

    fun showNotificationCommunicationIsConnected(stateConnection: Boolean) {
        if (_showNotificationCommunicationIsConnected.value != stateConnection) {
            _showNotificationCommunicationIsConnected.value = stateConnection
        }
    }
}
