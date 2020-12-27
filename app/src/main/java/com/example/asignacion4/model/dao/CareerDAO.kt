package com.example.asignacion4.model.dao

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CareerDAO(
    @PrimaryKey var id: Int=0,
    var name: String=""
):RealmObject()