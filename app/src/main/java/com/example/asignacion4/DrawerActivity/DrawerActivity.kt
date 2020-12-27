package com.example.asignacion4.DrawerActivity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.alerts.AlertType
import com.example.alerts.StandardAlert
import com.example.asignacion4.AdvisorFragment.AdvisorFragment
import com.example.asignacion4.AdvisoryDescriptionFragment.AdvisoryDescriptionFragment
import com.example.asignacion4.AsesoriasFragment.AsesoriasFragment
import com.example.asignacion4.CareersFragment.CareersFragment
import com.example.asignacion4.FragmentPerfilAlumno.FragmentPerfilAlumno
import com.example.asignacion4.FragmentlistProfesores.FragmentProfesoresActivity
import com.example.asignacion4.R
import com.example.asignacion4.WelcomeFragment.WelcomeFragment
import com.example.asignacion4.listeners.CallbacksAdvisoryDescriptionFragment
import com.example.asignacion4.listeners.CallbacksAsesoriasFragment
import com.example.asignacion4.listeners.CallbacksTeachersFragment
import com.example.asignacion4.listeners.ChangeFragmentListener
import com.example.asignacion4.login.LoginActivity
import com.example.asignacion4.model.remote.Career
import com.example.asignacion4.model.remote.Profesor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject

class DrawerActivity : AppCompatActivity(),
    DrawerContract.View ,
    ChangeFragmentListener,
    CallbacksTeachersFragment,
    CallbacksAsesoriasFragment,
    CallbacksAdvisoryDescriptionFragment
{
    private val mPresenter: DrawerPresenter by inject()
    private lateinit var alert: StandardAlert
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        mPresenter.setView(this)
        mPresenter.setComponents(R.drawable.ic_navigation)
        alert = StandardAlert(this, AlertType.Progress)
        mNavigationView.setNavigationItemSelectedListener { item: MenuItem ->
            when(item.itemId)
            {
                R.id.search_adviser ->
                {
                    setTitle("Busca a a tu asesor")
                    changeFragment(CareersFragment.newInstance("Especialidades"))
                }
                R.id.schedule_advisory ->
                {
                    setTitle("Programar una asesoría")
                    changeFragment(CareersFragment.newInstance("Carreras"))
                }
                R.id.misAsesorias ->
                {
                    setTitle("Mis Asesorías")
                    changeFragment(AsesoriasFragment.newInstance())
                }
                R.id.profile ->
                {
                    setTitle("Mi Perfil")
                    changeFragment(FragmentPerfilAlumno.newInstance())
                }
                R.id.logout ->
                {
                    StandardAlert(this, AlertType.Normal)
                        .setContentText("¿Deseas cerrar sesión?")
                        .setCancelButton("No", Dialog::cancel)
                        .setConfirmButton("Sí")
                        {alert ->
                            mPresenter.logout{
                                finish()
                                startActivity(Intent(this, LoginActivity::class.java).apply {
                                    this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                })
                            }
                        }.show()
                }
            }
            mDrawerLayout.closeDrawers()
            true
        }

        //Cargando el Fragment de Bienvenida
        supportFragmentManager
            .beginTransaction()
            .add(R.id.content_drawer, WelcomeFragment.newInstance())
            .commit()
    }

    override fun setToolbar(icon: Int) {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(icon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun setHeader(name: String)
    {
        var v = mNavigationView.getHeaderView(0)
        v.tvWelcomeHeader.text = "Hola $name"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home -> mDrawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun showProgress()
    {
        alert.setTitleText("Cerrando sesión")
        alert.show()
    }

    override fun hideProgress()
    {
        alert.dismiss()
    }

    override fun showMessage(title: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun changeFragment(fragment: String, career:Career) {
        if (fragment.equals("fragment_list_profesores")){
            changeFragment(FragmentProfesoresActivity.newInstance(career))
        }
    }

    override fun goToAdvisorFragment(profesor: Profesor, carrera: Career) {
        changeFragment(AdvisorFragment.newInstance(profesor, carrera))
    }

    override fun goToAdvisoryDescriptionFragment(id: Int)
    {
        changeFragment(AdvisoryDescriptionFragment.newInstance(id))
    }

    private fun changeFragment(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_drawer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun returnToAsesoriesFragment(currentFragment: Fragment)
    {
        setTitle("Mis Asesorías")
        supportFragmentManager.popBackStack()
    }

}