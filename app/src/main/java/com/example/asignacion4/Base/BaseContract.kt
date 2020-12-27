package com.example.asignacion4.Base

import com.example.asignacion4.MainActivity.MainContract

class BaseContract
{
    interface BaseView
    {
        fun showProgress()
        fun hideProgress()
        fun showMessage(title: String, message: String)
    }

    interface BasePresenter
    {
        fun onDestroy()
        fun onResume()
        fun onPause()
    }
}