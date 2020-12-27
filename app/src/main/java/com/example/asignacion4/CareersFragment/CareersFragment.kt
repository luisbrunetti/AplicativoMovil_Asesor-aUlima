package com.example.asignacion4.CareersFragment

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
import com.example.asignacion4.Adapters.CareersAdapter
import com.example.asignacion4.Adapters.EmptyAdapter
import com.example.asignacion4.R
import com.example.asignacion4.listeners.CallbackAdapter
import com.example.asignacion4.listeners.ChangeFragmentListener
import com.example.asignacion4.listeners.ClickButtonListener
import com.example.asignacion4.model.remote.Career
import kotlinx.android.synthetic.main.fragment_careers.*
import org.koin.android.ext.android.inject

private const val KEY_TYPE_FRAGMENT = "advisory"

class CareersFragment : Fragment(), CareersContract.View
{
    val mPresenter:CareersPresenter by inject()
    private var fragListenerProfesores : ChangeFragmentListener? = null
    private var careers: List<Career> ?= null
    private var adapter: CareersAdapter ?= null
    private var emptyAdapter: EmptyAdapter?=null
    private var typeFragment: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mPresenter.setView(this)
        arguments?.let {
            typeFragment = it.getString(KEY_TYPE_FRAGMENT)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragListenerProfesores = activity as ChangeFragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_careers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        tv_tittle.text = typeFragment!!
        mRecyclerCareers.layoutManager = layoutManager
        mRecyclerCareers.setHasFixedSize(true)
        mPresenter.getCareers(typeFragment!!)
        mSwipeRefreshLayout.setOnRefreshListener {
            mPresenter.getCareers(typeFragment!!)
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getCareers(typeFragment!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

    companion object
    {
        @JvmStatic
        fun newInstance(type: String) = CareersFragment().apply {
            arguments = Bundle().apply{
                putString(KEY_TYPE_FRAGMENT, type)
            }
        }
    }

    override fun setAdapter(careers: ArrayList<Career>)
    {
        this.careers = careers
        adapter = CareersAdapter(context!!,
        careers,
        object:ClickButtonListener{
            override fun OnItemButtonListener(view: View, index: Int)
            {
                val currentCareer = careers[index]
                fragListenerProfesores?.changeFragment("fragment_list_profesores",currentCareer)
            }
        },
        object: CallbackAdapter
        {
            override fun setEmptyAdapter(message: String)
            {
                emptyAdapter = EmptyAdapter(context!!, message)
                mRecyclerCareers.adapter = emptyAdapter
            }

            override fun setDefaultAdapter()
            {
                mRecyclerCareers.adapter = adapter
            }
        })
        mRecyclerCareers.adapter = adapter
    }

    override fun showProgress() {
        mSwipeRefreshLayout.isRefreshing = true
    }

    override fun hideProgress() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun showMessage(title: String, message: String)
    {
        StandardAlert(activity, AlertType.Normal)
            .setTitleText(title)
            .setContentText(message)
            .setConfirmButton("Ok", Dialog::cancel)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_fragmentprofesores, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView
        searchView.imeOptions= EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean
            {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean
            {
                adapter?.filter?.filter(newText)
                return true
            }

        })
    }
}