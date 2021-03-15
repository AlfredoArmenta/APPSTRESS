package com.example.estres2.util

object FileObject {
    private lateinit var nameFile: String

    fun setNameFile(file: String) {
        nameFile = file
    }

    fun getNameFile(): String = nameFile

    fun isInitialized(): Boolean {
        return FileObject::nameFile.isInitialized
    }
}