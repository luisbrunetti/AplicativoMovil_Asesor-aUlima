package com.example.asignacion4.FragmentPerfilAlumno

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.asignacion4.AsesoriasFragment.AsesoriasFragment
import com.example.asignacion4.DrawerActivity.DrawerActivity
import com.example.asignacion4.MainActivity.MainContract
import com.example.asignacion4.R
import com.example.asignacion4.data.local.DataStorageManager
import com.example.asignacion4.model.dao.AlumnoDAO
import org.koin.android.ext.android.inject

class FragmentPerfilAlumno : Fragment(){


    private val dsm: DataStorageManager by inject()
    private var tvName: TextView? = null
    private var tvLastName: TextView? = null
    private var tvCode: TextView? = null
    private var tvCareer: TextView? = null
    private var butEditProfile: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object
    {
        @JvmStatic
        fun newInstance() = FragmentPerfilAlumno()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil_alumno,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userInformation = getCurrentUserInformation()
        tvName = getView()?.findViewById(R.id.tvProfileName)
        tvLastName = getView()?.findViewById(R.id.tvProfileLastName)
        tvCode = getView()?.findViewById(R.id.tvProfileCode)
        tvCareer = getView()?.findViewById(R.id.tvProfileCareer)

        //
        butEditProfile = getView()?.findViewById(R.id.butEditProfile)
        butEditProfile?.setOnClickListener { v: View? ->
            (context as DrawerActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_drawer,FragmentEditarPerfil.newInstance())
                .addToBackStack(null)
                .commit()

        }
        //

        tvName?.text = userInformation.nombres
        tvLastName?.text = userInformation.apellidos
        tvCode?.text = userInformation.codigo
        tvCareer?.text = userInformation.carrera

    }

    private fun getCurrentUserInformation(): AlumnoDAO{
        return dsm.getCurrentUser() as AlumnoDAO
        //Toast.makeText(this.context,dsm.getCurrentUser())
        //{nombres:pepe},{apellidos:pepito},{codigo:123456},{carrera:Ingeniería de Sistemas}]
        
    }

}