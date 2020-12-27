package com.example.asignacion4.MainActivity

import com.example.asignacion4.Base.BaseContract

class MainContract
{
    interface View: BaseContract.BaseView
    {

    }

    interface Presenter: BaseContract.BasePresenter
    {
        fun greet(greet: String)
        fun loadCareers()
    }
}