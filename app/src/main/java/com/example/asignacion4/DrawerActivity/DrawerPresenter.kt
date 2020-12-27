package com.example.asignacion4.DrawerActivity

import com.example.asignacion4.data.local.DataStorageManager
import com.google.firebase.auth.FirebaseAuth

class DrawerPresenter(val mDataStorageManager: DataStorageManager): DrawerContract.Presenter
{
    private lateinit var view:DrawerContract.View
    override fun setView(view: DrawerContract.View)
    {
        this.view = view
    }

    override fun setComponents(iconToolbar: Int)
    {
        view.setToolbar(iconToolbar)
        var user = mDataStorageManager.getCurrentUser()!!
        view.setHeader(user.nombres)
    }

    override fun logout(block: ()->Unit)
    {
        view.showProgress()
        mDataStorageManager.clearAll()
        FirebaseAuth.getInstance().signOut()
        view.hideProgress()
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