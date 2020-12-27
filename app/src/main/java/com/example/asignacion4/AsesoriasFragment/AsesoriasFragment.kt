package com.example.asignacion4.AsesoriasFragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alerts.AlertType
import com.example.alerts.StandardAlert
import com.example.asignacion4.Adapters.AdvisoryDaoAdapter
import com.example.asignacion4.Adapters.EmptyAdapter
import com.example.asignacion4.R
import com.example.asignacion4.data.local.DataStorageManager
import com.example.asignacion4.listeners.CallbacksAsesoriasFragment
import com.example.asignacion4.listeners.ClickButtonListener
import com.example.asignacion4.model.dao.AdvisoryDAO
import kotlinx.android.synthetic.main.fragment_consultancies.*
import org.koin.android.ext.android.inject

class AsesoriasFragment : Fragment(), AsesoriasContract.View
{
    val mPresenter: AsesoriasPresenter by inject()
    private var emptyAdapter:EmptyAdapter?=null
    private var adapter: AdvisoryDaoAdapter?=null
    private var asesorias: List<AdvisoryDAO>?=null

    private lateinit var listener: CallbacksAsesoriasFragment

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mPresenter.setView(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as CallbacksAsesoriasFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_consultancies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerConsultancies.layoutManager = LinearLayoutManager(context)
        mRecyclerConsultancies.setHasFixedSize(true)
        mPresenter.getAdvisoryDaoFromLocal()
        mSwipeRefreshLayout.setOnRefreshListener {
            mPresenter.getAdvisoryDaoFromLocal()
        }
    }

    companion object
    {
        @JvmStatic
        fun newInstance() = AsesoriasFragment()
    }

    override fun setEmptyAdapter(message: String)
    {
        emptyAdapter = EmptyAdapter(context!!, message)
        mRecyclerConsultancies.adapter = emptyAdapter
    }

    override fun setAdapter(asesorias: List<AdvisoryDAO>, dataStorageManager: DataStorageManager)
    {
        this.asesorias = asesorias
        adapter = AdvisoryDaoAdapter(context!!,
            asesorias,
            object:ClickButtonListener
            {
                override fun OnItemButtonListener(view: View, index: Int)
                {
                    listener.goToAdvisoryDescriptionFragment(asesorias[index].id)
                }
            },
            dataStorageManager
        )
        mRecyclerConsultancies.adapter = adapter
    }

    override fun showProgress()
    {
        mSwipeRefreshLayout.isRefreshing = true
    }

    override fun hideProgress()
    {
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
}