package com.example.asignacion4

import com.example.asignacion4.AdvisorFragment.AdvisorPresenter
import com.example.asignacion4.AdvisoryDescriptionFragment.AdvisoryDescriptionPresenter
import com.example.asignacion4.AsesoriasFragment.AsesoriasPresenter
import com.example.asignacion4.CareersFragment.CareersPresenter
import com.example.asignacion4.DrawerActivity.DrawerPresenter
import com.example.asignacion4.MainActivity.MainPresenter
import com.example.asignacion4.data.local.DataStorageManager
import com.example.asignacion4.data.remote.Career.CareerService
import com.example.asignacion4.data.remote.ProvideRetrofit
import io.realm.Realm
import org.koin.dsl.module
import retrofit2.Retrofit

val presenterModule = module {
    factory {MainPresenter(get()) }
    factory { DrawerPresenter(get()) }
    factory { CareersPresenter(get()) }
    factory { AdvisorPresenter(get()) }
    factory { AsesoriasPresenter(get()) }
    factory { AdvisoryDescriptionPresenter(get()) }
}

val remoteModule = module {
    single { provideRetrofit() }
    single { provideCareerService(get())}
    factory{ CareerService(get()) }
}

val dataModule = module{
    single{ provideRealm()}
    factory { DataStorageManager(get()) }
}

fun provideRealm(): Realm
{
    return Realm.getDefaultInstance()
}

fun provideCareerService(retrofit: Retrofit): CareerService.Service
{
    return retrofit.create(CareerService.Service::class.java)
}

fun provideRetrofit() = ProvideRetrofit.getInstance(BuildConfig.BASE_URL)

