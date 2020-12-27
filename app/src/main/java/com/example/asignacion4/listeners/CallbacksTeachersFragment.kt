package com.example.asignacion4.listeners

import com.example.asignacion4.model.remote.Career
import com.example.asignacion4.model.remote.Profesor

interface CallbacksTeachersFragment
{
    fun goToAdvisorFragment(profesor: Profesor, carrera: Career)
}