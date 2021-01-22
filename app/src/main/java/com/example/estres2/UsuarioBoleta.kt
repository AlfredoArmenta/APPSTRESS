package com.example.estres2

import com.example.estres2.almacenamiento.entidades.usuario.Usuario

object UsuarioBoleta {
    private lateinit var objetoUsuario: Usuario

    fun setObjectBoleta(usuario: Usuario) {
        objetoUsuario = usuario
    }

    fun getObjectBoleta(): Usuario = objetoUsuario
}