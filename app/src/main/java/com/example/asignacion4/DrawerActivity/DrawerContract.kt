package com.example.asignacion4.DrawerActivity

import com.example.asignacion4.Base.BaseContract

class DrawerContract
{
    interface View: BaseContract.BaseView
    {
        fun setToolbar(icon: Int)
        fun setHeader(name: String)
    }

    interface Presenter: BaseContract.BasePresenter
    {
        fun setView(view: DrawerContract.View)
        fun setComponents(iconToolbar: Int)
        fun logout(block: ()->Unit)
    }
}