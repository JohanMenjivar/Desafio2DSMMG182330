package com.example.desafio2mg182330

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.widget.Toolbar
import com.example.desafio2mg182330.datos.Persona
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    var consultaOrdenada: Query = refPersonas.orderByChild("nombre")
    var personas: MutableList<Persona>? = null
    lateinit var listaPersonas: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Asegúrate de que el Toolbar tiene el tipo correcto
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        // Establecer el Toolbar como la barra de acción
        setSupportActionBar(toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        inicializar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sign_out -> {
                FirebaseAuth.getInstance()
                    .signOut()
                    .also {
                        Toast.makeText(
                            this,
                            "Sesión cerrada",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun inicializar() {
        val fab_agregar: FloatingActionButton = findViewById(R.id.fab_agregar)
        listaPersonas = findViewById(R.id.ListaPersonas)

        // Cuando el usuario haga clic en la lista (para editar registro)
        listaPersonas.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val intent = Intent(getBaseContext(), AddPersonaActivity::class.java)
                intent.putExtra("accion", "e") // Editar
                intent.putExtra("key", personas!![i].key)
                intent.putExtra("nombre", personas!![i].nombre)
                intent.putExtra("apellido", personas!![i].apellido) // Añadido apellido
                intent.putExtra("mate", personas!![i].mate)
                intent.putExtra("ciencias", personas!![i].ciencias)
                intent.putExtra("sociales", personas!![i].sociales)
                intent.putExtra("lenguaje", personas!![i].lenguaje)
                intent.putExtra("grado", personas!![i].grado) // Añadido grado
                startActivity(intent)
            }
        })

        // Cuando el usuario hace un LongClic (clic sin soltar elemento por más de 2 segundos)
        // Es por que el usuario quiere eliminar el registro
        listaPersonas.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                l: Long
            ): Boolean {
                // Preparando cuadro de dialogo para preguntar al usuario // St esta seguro de eliminar o no el registro
                val ad = AlertDialog.Builder(this@MainActivity)
                ad.setMessage("Está seguro de eliminar registro?")
                    .setTitle("Confirmación")
                ad.setPositiveButton(
                    "Si"
                ) { dialog, id ->
                    personas!![position].key?.let {
                        refPersonas.child(it).removeValue()
                    }
                    Toast.makeText(
                        this@MainActivity,
                        "Registro borrado!", Toast.LENGTH_SHORT
                    ).show()
                }
                ad.setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        Toast.makeText(
                            this@MainActivity,
                            "Operación de borrado cancelada!", Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                ad.show()
                return true
            }
        }

        fab_agregar.setOnClickListener {
            val i = Intent(getBaseContext(), AddPersonaActivity::class.java)
            i.putExtra("accion", "a") // Agregar
            i.putExtra("key", "")
            i.putExtra("nombre", "")
            i.putExtra("apellido", "") // Añadido campo apellido
            i.putExtra("mate", "")
            i.putExtra("ciencias", "")
            i.putExtra("sociales", "")
            i.putExtra("lenguaje", "")
            i.putExtra("grado", "") // Añadido campo grado
            startActivity(i)
        }

        personas = ArrayList<Persona>()

        // Cambiarlo refPersonas a consultaOrdenada para ordenar lista
        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                personas!!.clear()
                for (dato in dataSnapshot.children) {
                    val persona: Persona? = dato.getValue(Persona::class.java)
                    persona?.key = dato.key
                    if (persona != null) {
                        personas!!.add(persona)
                    }
                }
                val adapter = AdaptadorPersona(
                    this@MainActivity,
                    personas as ArrayList<Persona>
                )
                listaPersonas!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refPersonas: DatabaseReference = database.getReference("personas")
    }
}
