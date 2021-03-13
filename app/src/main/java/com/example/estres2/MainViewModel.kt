package com.example.estres2

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.estres2.util.UserObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel : ViewModel() {

    private var _updateUserData = MutableLiveData<Boolean>()
    val updateUserData: LiveData<Boolean>
        get() = _updateUserData

    private var _updateRegisters: MutableLiveData<Boolean> = MutableLiveData()
    val updateRegisters: LiveData<Boolean>
        get() = _updateRegisters

    fun updateRegisterList(update: Boolean) {
        _updateRegisters.value = update
    }

    fun updateUserDataFunc(update: Boolean) {
        _updateUserData.value = update
    }

    fun starProgress() {
        val fileName = Environment.getExternalStorageDirectory().toString() + "/Monitoreo" + UserObject.getObjectBoleta().boleta + "/ACC.csv"
        println(" ********** $fileName ********** ")
        val file = File(fileName)
        lateinit var lines: List<String>
        viewModelScope.launch(Dispatchers.IO) {
            try {
                lines = file.readLines()
                lines.forEach { line ->
                    println(line)
                }
                println(lines.size)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                println(" ____________ CSV Read Finished ____________ ")
            }
            println(lines.first())
            println(lines.last())
        }
    }

    init {
        _updateUserData.value = false
    }
}