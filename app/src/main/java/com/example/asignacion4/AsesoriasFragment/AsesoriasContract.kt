package com.example.asignacion4.AsesoriasFragment

import com.example.asignacion4.Base.BaseContract
import com.example.asignacion4.data.local.DataStorageManager
import com.example.asignacion4.model.dao.AdvisoryDAO

class AsesoriasContract
{
    interface View: BaseContract.BaseView
    {
        fun setEmptyAdapter(message: String)
        fun setAdapter(asesorias: List<AdvisoryDAO>, dataStorageManager: DataStorageManager)
    }

    interface  Presenter: BaseContract.BasePresenter
    {
        fun setView(view: AsesoriasContract.View)
        fun getAdvisoryDaoFromLocal()
    }
}