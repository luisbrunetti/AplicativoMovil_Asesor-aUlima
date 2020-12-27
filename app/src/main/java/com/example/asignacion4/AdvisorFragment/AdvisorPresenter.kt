package com.example.asignacion4.AdvisorFragment

import android.util.Log
import com.example.asignacion4.data.local.DataStorageManager
import com.example.asignacion4.model.remote.Career
import com.example.asignacion4.model.remote.Profesor

class AdvisorPresenter(val dataStorageManager: DataStorageManager): AdvisorContract.Presenter
{
    private lateinit var view: AdvisorContract.View
    override fun setView(view: AdvisorContract.View)
    {
        this.view = view
    }

    override fun getData(advisor: Profesor, carrera: Career)
    {
        view.showProgress()
        var email = getAdvisorEmail(advisor)
        var fullName = "${advisor.names} ${advisor.last_names}"
        var career = carrera.name
        var currentUser = dataStorageManager.getCurrentUser()
        view.fetchData(fullName, career, email, currentUser!!)
        view.hideProgress()
    }

    override fun sendEmail(topic: String, date: String, hour: String)
    {
        var message = isFieldsOK(topic, date, hour)
        if(message == "ok")
        {
            view.goToEmail(
                {advisor, career, topic ->
                    dataStorageManager.createCareerDAO(career)
                    dataStorageManager.createAdvisorDAO(advisor)
                    dataStorageManager.createAdvisory(advisor.id, career.id, topic, "", date, hour)
                    view.showMessage("Aviso","¡Se guardó la asesoría! Revise Mis Asesorías")
                },
                {
                    view.showMessage("Error", it)
                }
            )
        }
        else
        {
            view.showMessage("Error",message)
        }
    }

    private fun isFieldsOK(topic: String, date: String, hour: String): String
    {
        var message = "ok"
        if(topic=="")
        {
            message = "Por favor, escriba un tema a tratar"
        }
        else if(date=="")
        {
            message = "Por favor, complete una fecha de posible asesoría."
        }
        else if(hour=="")
        {
            message = "Por favor, complete la hora de la posible hora de asesoría"
        }
        return message
    }

    private fun getAdvisorEmail(advisor: Profesor): String
    {
        var start = advisor.names[0]
        var lastNames = getConcatString(advisor.last_names)
        var end = lastNames.substring(0..6)
        return start + end + "@ulima.edu.pe"
    }

    private fun getConcatString(string: String): String
    {
        var strings = string.split(" ")
        var concat = ""
        for(s in strings)
        {
            concat += s
        }
        return concat
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