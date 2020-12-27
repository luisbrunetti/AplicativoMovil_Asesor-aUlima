package com.example.asignacion4.model.dao

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class AdvisorDAO(
    @PrimaryKey var id: Int = 0,
    var full_name: String = "",
    var img: String = ""
): RealmObject()