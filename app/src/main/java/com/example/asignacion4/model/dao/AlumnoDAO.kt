package com.example.asignacion4.model.dao

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class AlumnoDAO(
    @PrimaryKey var id: String = "",
    var nombres: String = "",
    var apellidos: String = "",
    var codigo: String = "",
    var carrera: String = ""
) : RealmObject()