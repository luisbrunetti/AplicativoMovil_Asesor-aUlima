package com.example.asignacion4.AsesoriasFragment

import com.example.asignacion4.data.local.DataStorageManager

class AsesoriasPresenter(val dataStorageManager: DataStorageManager): AsesoriasContract.Presenter
{
    private lateinit var view: AsesoriasContract.View

    override fun setView(view: AsesoriasContract.View)
    {
        this.view = view
    }

    override fun getAdvisoryDaoFromLocal()
    {
        view.showProgress()
        var listAdvisories = dataStorageManager.getAllAdvisoryDAO()
        if(listAdvisories.isEmpty())
        {
            view.setEmptyAdapter("No tienes asesorías guardadas.")
        }
        else
        {
            view.setAdapter(listAdvisories, dataStorageManager)
        }
        view.hideProgress()
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