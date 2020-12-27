package com.example.asignacion4.AdvisoryDescriptionFragment

import com.example.asignacion4.Base.BaseContract
import com.example.asignacion4.model.dao.AdvisorDAO
import com.example.asignacion4.model.dao.AdvisoryDAO

class AdvisoryDescriptionContract
{
    interface View: BaseContract.BaseView
    {
        fun fetchData(teacher: AdvisorDAO, advisory: AdvisoryDAO, block: ()->Unit)
        fun goToReminder(topic: String, timeInMilis: Long, block: () -> Unit)
    }

    interface Presenter: BaseContract.BasePresenter
    {
        fun setView(view: AdvisoryDescriptionContract.View)
        fun getData(advisory_id: Int)
        fun updateData(id:Int, topic:String, day:String, hour:String, comment:String)
        fun createReminder(id:Int, topic:String, day:String, hour:String, comment:String)
        fun deleteAdvisory(id_advisory: Int, block: () -> Unit)
    }
}