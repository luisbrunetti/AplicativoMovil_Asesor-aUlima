package com.example.asignacion4.AdvisorFragment

import com.example.asignacion4.Base.BaseContract
import com.example.asignacion4.model.dao.AlumnoDAO
import com.example.asignacion4.model.remote.Career
import com.example.asignacion4.model.remote.Profesor

class AdvisorContract
{
    interface View: BaseContract.BaseView
    {
        fun fetchData(name: String, career: String, email: String, user: AlumnoDAO)
        fun goToEmail(block: (advisor:Profesor, career:Career, topic: String)->Unit, blockError: (message: String)->Unit)
    }

    interface Presenter: BaseContract.BasePresenter
    {
        fun setView(view: AdvisorContract.View)
        fun getData(advisor: Profesor, carrera: Career)
        fun sendEmail(topic: String, date: String, hour: String)
    }
}