package com.example.asignacion4.MainActivity

import android.util.Log
import com.example.asignacion4.data.remote.Career.CareerService
import io.reactivex.disposables.CompositeDisposable

class MainPresenter(val careerService: CareerService): MainContract.Presenter
{
    private lateinit var view: MainContract.View
    private var mCompositeDisposable:CompositeDisposable = CompositeDisposable()

    fun setView(view: MainContract.View)
    {
        this.view = view
    }

    override fun onDestroy()
    {
        mCompositeDisposable.clear()
    }

    override fun onResume() {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        TODO("Not yet implemented")
    }

    override fun greet(greet: String)
    {
        view.showMessage("Aviso", greet)
    }

    override fun loadCareers() {
        mCompositeDisposable.add(careerService.getAllCareers()
            .subscribe(
                {careers ->
                    view.showProgress()
                    for(c in careers)
                    {
                        Log.d("Career", c.name)
                    }
                    view.showMessage("Aviso","Finalizó, revise los logs")
            },
                {throwable ->
                    view.showMessage("Aviso", throwable.message!!)
            })
        )
    }

}