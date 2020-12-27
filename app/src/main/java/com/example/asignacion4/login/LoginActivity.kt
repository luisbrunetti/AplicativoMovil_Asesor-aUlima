package com.example.asignacion4.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alerts.AlertType
import com.example.alerts.StandardAlert
import com.example.asignacion4.DrawerActivity.DrawerActivity
import com.example.asignacion4.MainActivity.MainContract
import com.example.asignacion4.MainActivity.MainPresenter
import com.example.asignacion4.R
import com.example.asignacion4.data.local.DataStorageManager
import com.example.asignacion4.model.dao.AlumnoDAO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity(),
    MainContract.View
{
    //Inject presenter
    private val mPresenter: MainPresenter by inject()
    private val mDataStorageManager: DataStorageManager by inject()
    private var et_LoginCodigo: EditText? = null
    private var et_LodingPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter.setView(this)
        et_LodingPassword = findViewById(R.id.et_login_password)
        et_LoginCodigo = findViewById(R.id.et_login_username)
        checkIfIsLogged()
        but_login_iniciarsesion.setOnClickListener {
            InciarSesion()
            //startActivity(Intent(this, DrawerActivity::class.java))
        }
        tv_login_registrarse.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun checkIfIsLogged()
    {
        val currentUser = mDataStorageManager.getCurrentUser()
        if(currentUser!=null)
        {
            startActivity(Intent(this, DrawerActivity::class.java).apply {
                this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }
    }

    private fun InciarSesion(){
        progress_login.visibility = View.VISIBLE
        val user =  et_LoginCodigo?.text.toString()+"@aloe.ulima.edu.pe"
        val password = et_LodingPassword?.text.toString()
        Log.d("user",user.toString() + password.toString())
        val authFirebase = FirebaseAuth.getInstance()
        if(et_LoginCodigo?.text!!.isNotEmpty() && et_LodingPassword?.text!!.isNotEmpty()){
            authFirebase.signInWithEmailAndPassword(user,password).addOnCompleteListener {task ->
                if(task.isSuccessful){
                    retriveDataAlumno()
                }else{
                    progress_login.visibility = View.GONE
                    Toast.makeText(this, "Error en autotentificación", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            progress_login.visibility = View.GONE
            Toast.makeText(this, "Llene todos los campos correctamente", Toast.LENGTH_SHORT).show()
        }
    }


    private fun retriveDataAlumno(){
        val databasereference = FirebaseFirestore.getInstance()
        val colection  =databasereference.collection("Usuarios")
        colection.whereEqualTo("codigo",et_LoginCodigo?.text?.toString()).get().addOnSuccessListener {
            for(document in it ){
                val alumnoDAO = AlumnoDAO(
                    document.id,
                    document.data["nombres"].toString(),
                    document.data["apellidos"].toString(),
                    document.data["codigo"].toString(),
                    document.data["carrera"].toString())
                mDataStorageManager.createOrUpdateAlumnoDAO(alumnoDAO)
            }
            progress_login.visibility = View.GONE
            startActivity(Intent(this, DrawerActivity::class.java).apply {
                this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

    override fun showProgress()
    {
        Toast.makeText(applicationContext, "Cargando...", Toast.LENGTH_LONG).show()
    }

    override fun hideProgress()
    {
        TODO("Not yet implemented")
    }

    override fun showMessage(title: String, message: String)
    {
        StandardAlert(this, AlertType.Normal)
            .setTitleText(title)
            .setContentText(message)
            .setConfirmButton("Ok", Dialog::cancel)
            .show()
    }
}