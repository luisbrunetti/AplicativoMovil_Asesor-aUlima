package com.example.asignacion4

import android.app.Application
import io.realm.Realm
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application()
{
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(
                presenterModule,
                dataModule,
                remoteModule
            ))
            androidContext(this@MyApplication)
        }
        Realm.init(this)
    }
}