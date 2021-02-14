package com.example.estres2.util

import com.example.estres2.almacenamiento.entidades.usuario.User

object UserObject {
    private lateinit var objetoUser: User

    fun setObjectBoleta(user: User) {
        objetoUser = user
    }

    fun getObjectBoleta(): User = objetoUser

    fun isInitialized(): Boolean {
        return UserObject::objetoUser.isInitialized
    }
}