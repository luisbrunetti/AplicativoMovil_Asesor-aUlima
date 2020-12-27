package com.example.asignacion4.AdvisoryDescriptionFragment

import com.example.asignacion4.data.local.DataStorageManager
import com.example.asignacion4.model.dao.AdvisoryDAO
import java.text.SimpleDateFormat

class AdvisoryDescriptionPresenter(val mDataStorageManager: DataStorageManager): AdvisoryDescriptionContract.Presenter
{
    private lateinit var view: AdvisoryDescriptionContract.View
    override fun setView(view: AdvisoryDescriptionContract.View)
    {
        this.view = view
    }

    override fun getData(advisory_id: Int)
    {
        view.showProgress()
        val currentAdvisoryDAO = mDataStorageManager.getAdvisoryDAO(advisory_id)!!
        val currentTeacher = mDataStorageManager.getAdvisorDAO(currentAdvisoryDAO.teacher_id)!!
        view.fetchData(currentTeacher, currentAdvisoryDAO) {
            view.hideProgress()
            view.showMessage("Aviso", "Haga tap en el campo que desea editar.")
        }
    }

    override fun updateData(id:Int, topic:String, day:String, hour:String, comment:String)
    {
        view.showProgress()
        mDataStorageManager.updateAdvisoryDAO(id,
            topic,
            day,
            hour,
            comment)
        view.hideProgress()
        view.showMessage("Aviso", "Datos actualizados con éxito")
    }

    override fun createReminder(id:Int, topic:String, day:String, hour:String, comment:String)
    {
        var date = "$day $hour"
        var formatter= SimpleDateFormat("dd/MM/yyyy HH:mm")
        var localDate = formatter.parse(date)
        var timeInMiliseconds = localDate.time
        view.goToReminder(topic, timeInMiliseconds){
            updateData(id, topic, day, hour, comment)
        }

    }

    override fun deleteAdvisory(id_advisory: Int, block: () -> Unit)
    {
        mDataStorageManager.deleteAdvisoryDAO(id_advisory)
        view.showMessage("Aviso", "Asesría Eliminada")
        block()
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        TODO("Not yet implemented")
    }

}