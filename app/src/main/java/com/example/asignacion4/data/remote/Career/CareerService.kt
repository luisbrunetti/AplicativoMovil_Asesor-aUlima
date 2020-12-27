package com.example.asignacion4.data.remote.Career

import android.util.Log
import com.example.asignacion4.model.remote.Career
import com.example.asignacion4.model.remote.Profesor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.GET
import retrofit2.http.Path

class CareerService(val service: Service)
{
    fun getAllCareers(): Observable<List<Career>>
    {
        return service.getCareers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun getAllProfesoresbyCarrer(id:Int) : Observable<List<Profesor>>
    {
        return service.getProfesoresByIDCarrer(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    interface Service
    {
        @GET("carrers")
        fun getCareers(): Observable<List<Career>>

        @GET("carrers/{id}")
        fun getProfesoresByIDCarrer(@Path("id") id: Int) :Observable<List<Profesor>>
    }

}