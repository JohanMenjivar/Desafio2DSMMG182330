package com.example.desafio2mg182330

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var buttonRegister:Button
    private lateinit var textViewLogin: TextView

    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth=FirebaseAuth.getInstance()


        buttonRegister = findViewById(R.id.btnRegister)
        buttonRegister.setOnClickListener {

        val email = findViewById<EditText>(R.id.txtEmail).text.toString()
        val password = findViewById<EditText>(R.id.txtPass).text.toString()
        this.register(email, password)
        }
        textViewLogin = findViewById(R.id.textViewLogin)
        textViewLogin.setOnClickListener {
            this.goToLogin()
        }

        this.checkUser()
    }

    private fun register (email: String, password: String) {
        auth.createUserWithEmailAndPassword (email, password)
            .addOnCompleteListener{ task ->
            if (task.isSuccessful){
                val intent = Intent( this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    private fun goToLogin(){
        val intent=Intent(this,LoginActivity::class.java)
        startActivity(intent);
    }


    override fun onResume() {
        super.onResume()
        auth.addAuthStateListener(authStateListener)
    }
    override fun onPause() {
        super.onPause()
        auth.removeAuthStateListener (authStateListener)
    }


    private fun checkUser() {
// Verificacion del ususario
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser != null) {
// Cambiando la vista
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    }

