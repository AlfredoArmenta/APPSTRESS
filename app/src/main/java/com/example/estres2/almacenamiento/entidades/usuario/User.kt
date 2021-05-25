package com.example.estres2.almacenamiento.entidades.usuario

data class User(
    var boleta: String = "",
    var nombre: String = "",
    var edad: String = "",
    var genero: String = "",
    var semestre: String = "",
    var password: String = "",
    var imagen: String = "",
    var basalHR: String = "",
    var basalGSR: String = ""
)
