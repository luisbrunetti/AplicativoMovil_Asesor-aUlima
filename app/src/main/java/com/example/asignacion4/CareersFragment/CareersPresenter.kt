package com.example.asignacion4.CareersFragment

import com.example.asignacion4.data.local.DataStorageManager
import com.example.asignacion4.data.remote.Career.CareerService
import com.example.asignacion4.model.remote.Career
import io.reactivex.disposables.CompositeDisposable

class CareersPresenter(val careerService: CareerService): CareersContract.Presenter
{
    private lateinit var view: CareersContract.View
    private var mCompositeDisposable = CompositeDisposable()

    override fun setView(view: CareersContract.View)
    {
        this.view = view
    }

    override fun getCareers(type: String)
    {
        view.showProgress()
        mCompositeDisposable.add(
            careerService.getAllCareers().subscribe(
                {careers ->
                view.setAdapter(getCareersByType(type, careers))
                view.hideProgress()
                },
                {
                    view.showMessage("Error", it.message!!)
                })
        )
    }

    private fun getCareersByType(type: String, careers: List<Career>): ArrayList<Career>
    {
        var newCareers= ArrayList<Career>()
        var specialties = ArrayList<Career>()
        var returnList = ArrayList<Career>()
        for(c in careers)
        {
            if(c.id > 13 ) { specialties.add(c) }
            else { newCareers.add(c) }
        }

        when(type)
        {
            "Especialidades" -> returnList = specialties
            "Carreras" -> returnList = newCareers
        }
        return returnList
    }

    override fun onDestroy()
    {
        mCompositeDisposable.clear()
    }

    override fun onResume()
    {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        TODO("Not yet implemented")
    }


}