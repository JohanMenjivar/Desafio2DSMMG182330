package com.example.desafio2mg182330

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.desafio2mg182330.datos.Persona

class AdaptadorPersona(private val context: Activity, var personas: List<Persona>) :
    ArrayAdapter<Persona?>(context, R.layout.activity_main, personas) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val layoutInflater = context.layoutInflater
        val rowView = view ?: layoutInflater.inflate(R.layout.persona_layout, null)

        val tvNombre = rowView.findViewById<TextView>(R.id.tvNombre)
        val tvMaterias = rowView.findViewById<TextView>(R.id.tvMaterias)
        val tvGrado= rowView.findViewById<TextView>(R.id.tvGrado)// Nuevo campo para mostrar las notas

        val persona = personas[position]
        tvNombre.text = "Nombre: ${persona.nombre} ${persona.apellido}"
tvGrado.text=  "Grado: ${persona.grado}"
        // Mostramos las materias y sus notas
        tvMaterias.text = "Lenguaje: ${persona.lenguaje}, Sociales: ${persona.sociales}, " +
                "Ciencias: ${persona.ciencias}, Matemáticas: ${persona.mate}"

        return rowView
    }
}
