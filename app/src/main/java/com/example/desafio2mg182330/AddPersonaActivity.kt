package com.example.desafio2mg182330

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio2mg182330.datos.Persona
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddPersonaActivity : AppCompatActivity() {

    private var txtNombre: EditText? = null
    private var txtApellido: EditText? = null
    private var txtMate: EditText? = null
    private var txtCiencias: EditText? = null
    private var txtSociales: EditText? = null
    private var txtLenguaje: EditText? = null
    private var spinnerGrado: Spinner? = null
    private var key = ""
    private var accion = ""
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_persona)

        // Configuración del spinner con los grados
        val spinner: Spinner = findViewById(R.id.spinnerGrado)
        val grados = arrayOf("1º", "2º", "3º", "4º", "5º", "6º", "7º", "8º", "9º", "1º Bac.", "2º Bac.", "3º Bac.")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, grados)
        spinner.adapter = adapter

        inicializar()
    }

    private fun inicializar() {
        txtNombre = findViewById(R.id.txtNombre)
        txtApellido = findViewById(R.id.txtApellido)
        txtMate = findViewById(R.id.txtMate)
        txtCiencias = findViewById(R.id.txtCiencias)
        txtSociales = findViewById(R.id.txtSociales)
        txtLenguaje = findViewById(R.id.txtLenguaje)
        spinnerGrado = findViewById(R.id.spinnerGrado)

        val datos: Bundle? = intent.extras
        if (datos != null) {
            key = datos.getString("key").toString()
            txtNombre?.setText(datos.getString("nombre").toString())
            txtApellido?.setText(datos.getString("apellido").toString())
            txtMate?.setText(datos.getDouble("mate").toString())
            txtCiencias?.setText(datos.getDouble("ciencias").toString())
            txtSociales?.setText(datos.getDouble("sociales").toString())
            txtLenguaje?.setText(datos.getDouble("lenguaje").toString())
            accion = datos.getString("accion").toString()

            // Inicializar el spinner con el grado adecuado
            val grado = datos.getString("grado")
            grado?.let {
                val grados = arrayOf("1º", "2º", "3º", "4º", "5º", "6º", "7º", "8º", "9º", "1º Bac.", "2º Bac.", "3º Bac.")
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, grados)
                spinnerGrado?.adapter = adapter
                val position = grados.indexOf(grado)
                if (position != -1) {
                    spinnerGrado?.setSelection(position)
                }
            }
        }
    }

    fun guardar(v: View?) {
        val nombre: String = txtNombre?.text.toString()
        val apellido: String = txtApellido?.text.toString()

        // Validación para las notas
        val mate: Double? = txtMate?.text.toString().toDoubleOrNull()
        val ciencias: Double? = txtCiencias?.text.toString().toDoubleOrNull()
        val sociales: Double? = txtSociales?.text.toString().toDoubleOrNull()
        val lenguaje: Double? = txtLenguaje?.text.toString().toDoubleOrNull()

        // Comprobar que las notas sean válidas (entre 0 y 10)
        if (mate == null || mate < 0 || mate > 10) {
            Toast.makeText(this, "La nota de Matemáticas debe estar entre 0 y 10", Toast.LENGTH_SHORT).show()
            return
        }

        if (ciencias == null || ciencias < 0 || ciencias > 10) {
            Toast.makeText(this, "La nota de Ciencias debe estar entre 0 y 10", Toast.LENGTH_SHORT).show()
            return
        }

        if (sociales == null || sociales < 0 || sociales > 10) {
            Toast.makeText(this, "La nota de Sociales debe estar entre 0 y 10", Toast.LENGTH_SHORT).show()
            return
        }

        if (lenguaje == null || lenguaje < 0 || lenguaje > 10) {
            Toast.makeText(this, "La nota de Lenguaje debe estar entre 0 y 10", Toast.LENGTH_SHORT).show()
            return
        }

        val grado: String? = spinnerGrado?.selectedItem.toString()
        database = FirebaseDatabase.getInstance().getReference("personas")

        // Se forma objeto persona
        val persona = Persona(nombre, apellido, grado, key, mate, ciencias, sociales, lenguaje)

        if (accion == "a") { // Agregar registro
            val newKey = database.push().key // Generar una nueva clave
            if (newKey != null) {
                database.child(newKey).setValue(persona).addOnSuccessListener {
                    Toast.makeText(this, "Se guardó con éxito", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No se pudo generar una clave", Toast.LENGTH_SHORT).show()
            }
        } else if (accion == "e") { // Editar registro
            if (key.isNotEmpty()) {
                val personaValues = persona.toMap()
                val childUpdates = hashMapOf<String, Any>(key to personaValues)
                database.updateChildren(childUpdates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Se actualizó con éxito", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "No se encontró la clave del registro", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        finish()
    }


    fun cancelar(v: View?) {
        finish()
    }
}
