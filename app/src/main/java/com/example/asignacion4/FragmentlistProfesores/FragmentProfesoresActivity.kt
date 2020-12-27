package com.example.asignacion4.FragmentlistProfesores

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alerts.AlertType
import com.example.alerts.StandardAlert
import com.example.asignacion4.Adapters.EmptyAdapter
import com.example.asignacion4.Adapters.ListProfesoresAdapter
import com.example.asignacion4.R
import com.example.asignacion4.data.remote.Career.CareerService
import com.example.asignacion4.listeners.CallbackAdapter
import com.example.asignacion4.listeners.CallbacksTeachersFragment
import com.example.asignacion4.listeners.ClickButtonListener
import com.example.asignacion4.model.remote.Career
import com.example.asignacion4.model.remote.Profesor
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_list_profesores.*
import org.koin.android.ext.android.inject

private const val KEY_CAREER_PASSED="career"

class FragmentProfesoresActivity :Fragment()/*,ClickButtonListener*/{
    val mCareerService: CareerService by inject()
    private var fragmentprofeorView : View? = null
    private var Composite = CompositeDisposable()
    private var listaprofesor:ArrayList<Profesor>? = null
    private var listProfesorbySession:ArrayList<Profesor>? = null
    private var adapter: ListProfesoresAdapter?=null
    private var emptyAdapter: EmptyAdapter?=null
    private var career: Career?=null
    private lateinit var listener: CallbacksTeachersFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            career = it.getSerializable(KEY_CAREER_PASSED) as Career

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as CallbacksTeachersFragment
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_list_profesores,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingRecycle()
    }
    private fun settingRecycle(){
        val layoutManager = LinearLayoutManager(context)
        rv_fragmentlistprofeores.layoutManager = layoutManager
        rv_fragmentlistprofeores.setHasFixedSize(true)
        getAdvisors(career!!)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_fragmentprofesores,menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.imeOptions=EditorInfo.IME_ACTION_DONE
        tvi_fragmentprofesoresCarrera.text = career!!.name.toString()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.filter?.filter(newText)
                return true
            }
        })
        }

    private fun getAdvisors(career: Career)
    {
        Composite.add(
            mCareerService.getAllProfesoresbyCarrer(career.id).subscribe(
                {
                    adapter = ListProfesoresAdapter(context!!,
                        it,
                        object:ClickButtonListener
                        {
                            override fun OnItemButtonListener(view: View, index: Int)
                            {
                                listener.goToAdvisorFragment(it[index], career)
                            }
                        },
                        object:CallbackAdapter
                        {
                            override fun setEmptyAdapter(message: String) {
                                emptyAdapter = EmptyAdapter(context!!, message)
                                rv_fragmentlistprofeores.adapter = emptyAdapter
                            }

                            override fun setDefaultAdapter()
                            {
                                rv_fragmentlistprofeores.adapter = adapter
                            }

                        }
                        ,
                        career.name
                        )
                    rv_fragmentlistprofeores.adapter = adapter
                },
                {
                    StandardAlert(context!!, AlertType.Normal)
                        .setTitleText("Error")
                        .setContentText(it.message!!)
                        .setConfirmButton("Ok", Dialog::cancel)
                        .show()
                }
            )
        )
    }

    override fun onResume() {
        super.onResume()
        getAdvisors(career!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        Composite.clear()
    }

    companion object
    {
        fun newInstance(career: Career) = FragmentProfesoresActivity().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_CAREER_PASSED, career)
            }
        }
    }
}

