package com.example.asignacion4.FragmentPerfilAlumno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.asignacion4.DrawerActivity.DrawerActivity
import com.example.asignacion4.R
import com.example.asignacion4.data.local.DataStorageManager
import com.example.asignacion4.model.dao.AlumnoDAO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_editar_perfil.*
import org.koin.android.ext.android.inject


class FragmentEditarPerfil: Fragment() {
    private val dsm: DataStorageManager by inject()
    private var etName: EditText? = null
    private var etLastName: EditText? = null
    private var tvCode: TextView? = null
    private var spChangeCareer: String? = null
    private var butBack: Button? = null
    private var butUpdateInformation: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object
    {
        @JvmStatic
        fun newInstance() = FragmentEditarPerfil()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_editar_perfil,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataUser = dsm.getCurrentUser()
        etName = getView()?.findViewById(R.id.etProfileName)
        etLastName = getView()?.findViewById(R.id.etProfileLastName)
        tvCode = getView()?.findViewById(R.id.tvProfileCodeShow)
        butBack = getView()?.findViewById(R.id.butBackToProfile)
        butUpdateInformation = getView()?.findViewById(R.id.butUpdateProfile)

        etName?.setText(dataUser?.nombres)
        etLastName?.setText(dataUser?.apellidos)
        tvCode?.text = dataUser?.codigo

        //Spinner change career
        val adapter = ArrayAdapter.createFromResource((context as DrawerActivity),
            R.array.list_carreras,
            R.layout.spinner_style)
        sp_change_career?.adapter = adapter
        sp_change_career?.setSelection((sp_change_career.getAdapter() as ArrayAdapter<String?>).getPosition(dataUser?.carrera))
        sp_change_career?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                spChangeCareer = p0?.getItemAtPosition(p2).toString()
            }

        }
        //

        //Update data
        butUpdateInformation?.setOnClickListener { v: View? ->
            if(etName!!.text.isNotEmpty() && etLastName!!.text.isNotEmpty()){
                if(!spChangeCareer.equals("Seleccione su carrera")){
                    val databasereference = FirebaseFirestore.getInstance()
                    val collection = databasereference.collection("Usuarios")
                    collection.document(dataUser!!.id).update(
                        "nombres", etName!!.text.toString(),
                        "apellidos", etLastName!!.text.toString(),
                        "carrera", spChangeCareer!!.toString()
                    )
                    dsm.updateCurrentUser(etName!!.text.toString(),etLastName!!.text.toString(),spChangeCareer.toString())
                    fragmentManager!!.popBackStack()
                }else{
                    Toast.makeText((context as DrawerActivity), "Debe seleccionar una carrera",Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText((context as DrawerActivity), "Los campos no deben estar vacíos",Toast.LENGTH_SHORT).show()
            }
        }
        //

        butBack?.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
    }

}