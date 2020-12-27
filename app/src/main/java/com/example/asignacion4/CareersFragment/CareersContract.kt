package com.example.asignacion4.CareersFragment

import com.example.asignacion4.Base.BaseContract
import com.example.asignacion4.model.remote.Career

class CareersContract
{
    interface View: BaseContract.BaseView
    {
        fun setAdapter(careers: ArrayList<Career>)
    }

    interface Presenter: BaseContract.BasePresenter
    {
        fun setView(view: CareersContract.View )
        fun getCareers(type: String)
    }
}