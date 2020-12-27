package com.example.asignacion4.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.asignacion4.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registro.*
import java.util.regex.Pattern

class RegistroActivity : AppCompatActivity() {
    private var sp_carrera:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)


        val adapter = ArrayAdapter.createFromResource(applicationContext,
            R.array.list_carreras,
            R.layout.spinner_style)


        sp_registo_carreras?.adapter = adapter
        sp_registo_carreras?.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sp_carrera = p0?.getItemAtPosition(p2).toString()
            }
        }

        but_registro_reg?.setOnClickListener {
            CreateNewAccount()
        }
        but_registro_regresar?.setOnClickListener {
            finish()
        }
    }
    private fun VerificarApellido(lastname:String):Boolean{
        var reg = "[a-zA-Z]*[\\s]{1}[a-zA-Z]{3,}"
        var ptern = Pattern.compile(reg)
        var matcher = ptern.matcher(lastname)
        Log.d("regrex2",matcher.matches().toString())
        return matcher.matches()
    }
    private fun VerificarNombre(name :String): Boolean {
        var reg = "(^[a-zA-Z]{3,}(?: [a-zA-Z]+){0,2}\$?)"
        var ptern = Pattern.compile(reg)
        var matcher = ptern.matcher(name)
        Log.d("regrex",matcher.matches().toString())
        return matcher.matches()
        /*
        ^ - start of string
        [a-zA-Z]{4,} - 4 or more ASCII letters
        (?: [a-zA-Z]+){0,2} - 0 to 2 occurrences of a space followed with one or more ASCII letters
        $ - end of string.
         */
    }
    private fun CreateNewAccount(){
        if(et_registro_apellido?.text!!.isNotEmpty() && et_registro_nombres.text!!.isNotEmpty()
            && et_registro_codigo.text!!.isNotEmpty() && et_registro_password.text!!.isNotEmpty()){
            if(!sp_carrera.equals("Seleccione su carrera")){
                if (VerificarApellido(et_registro_apellido.text.toString())){
                    if(VerificarNombre(et_registro_nombres.text.toString())){
                        progress_registro.visibility = View.VISIBLE
                        val authFirebase= FirebaseAuth.getInstance()
                        val nombres = et_registro_nombres.text.toString()
                        val apellidos = et_registro_apellido.text.toString()
                        val codigo = et_registro_codigo.text.toString()
                        val password = et_registro_password.text.toString()
                        val emailUlima = "$codigo@aloe.ulima.edu.pe"
                        val databasereference = FirebaseFirestore.getInstance()
                        val collection = databasereference.collection("Usuarios")
                        collection.whereEqualTo("codigo",codigo).get().addOnSuccessListener {
                            var codTest :String = ""
                            for (document in it) {
                                codTest = document["codigo"].toString()
                            }
                            if (codigo == codTest) {
                                Toast.makeText(this, "Tu código ya esta registrado", Toast.LENGTH_SHORT)
                                    .show()
                                progress_registro.visibility = View.GONE
                            } else {
                                authFirebase.createUserWithEmailAndPassword(emailUlima, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isComplete) {
                                            authFirebase.currentUser?.sendEmailVerification()
                                                ?.addOnCompleteListener(this) { task ->
                                                    if (task.isComplete) {
                                                        Toast.makeText(
                                                            this,
                                                            "Correo enviado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val HashMapRegistro = HashMap<String, String>()
                                                        HashMapRegistro["nombres"] = nombres
                                                        HashMapRegistro["apellidos"] = apellidos
                                                        HashMapRegistro["codigo"] = codigo
                                                        HashMapRegistro["emailUlima"] = emailUlima
                                                        HashMapRegistro["carrera"] = sp_carrera!!
                                                        collection.add(HashMapRegistro)
                                                            .addOnSuccessListener { documentReference ->
                                                                progress_registro.visibility = View.GONE
                                                                finish()
                                                                Toast.makeText(
                                                                    this,
                                                                    "Se ha registrado conrrectamente",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }.addOnFailureListener { exception ->
                                                                Toast.makeText(
                                                                    this,
                                                                    "Ocurrio el siguiente error $exception",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                progress_registro.visibility = View.GONE
                                                            }
                                                    } else {
                                                        Toast.makeText(
                                                            this,
                                                            "Error al enviar email",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        progress_registro.visibility = View.GONE
                                                    }
                                                }
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Error al enviar email",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            progress_registro.visibility = View.GONE
                                        }
                                    }
                            }
                        }
                    }else{
                        Toast.makeText(this,"Ingrese un nombre correcto", Toast.LENGTH_SHORT).show()
                        progress_registro.visibility = View.GONE
                    }
                }else{
                    Toast.makeText(this,"Escribe un apellido correcto", Toast.LENGTH_SHORT).show()
                    progress_registro.visibility = View.GONE
                }
            }else{
                Toast.makeText(this,"Seleccione un carrera", Toast.LENGTH_SHORT).show()
                progress_registro.visibility = View.GONE
            }
        }else{
            Toast.makeText(this,"Llene los campos correctamente", Toast.LENGTH_SHORT).show()
            progress_registro.visibility = View.GONE
        }
    }
}