package com.example.desafio2mg182330.datos;


class Persona {
    fun key(key: String?) {
    }
    var mate: Double? = null
    var ciencias: Double ? = null
    var sociales: Double ? = null
    var lenguaje: Double ? = null
    var apellido: String? = null
    var grado: String? = null
    var nombre: String? = null
    var key: String? = null
    var per: MutableMap<String, Boolean> = HashMap()
    constructor() {}
    constructor(dui: String?, nombre: String?) {
                this.nombre = nombre
    }
    constructor(
        nombre: String?,
        apellido: String?,
        grado: String?,
        key: String?,
        mate: Double?,
        ciencias: Double?,
        sociales: Double?,
        lenguaje: Double?
    ) {
        this.nombre = nombre
        this.apellido=apellido
        this.grado=grado
        this.key = key
        this.mate = mate
        this.ciencias = ciencias
        this.sociales = sociales
        this.lenguaje = lenguaje
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "lenguaje" to lenguaje,
            "sociales" to sociales,
            "ciencias" to ciencias,
            "mate" to mate,
            "nombre" to nombre,
            "apellido" to apellido,
            "grado" to grado,
            "key" to key,
            "per" to per
        )
    }
}