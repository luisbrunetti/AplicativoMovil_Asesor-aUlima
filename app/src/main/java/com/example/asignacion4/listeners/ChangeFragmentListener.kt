package com.example.asignacion4.listeners

import com.example.asignacion4.model.remote.Career

interface ChangeFragmentListener {

    fun changeFragment(fragment : String, career: Career)
}