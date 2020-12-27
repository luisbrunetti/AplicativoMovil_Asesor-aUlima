package com.example.asignacion4.model.dao

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class AdvisoryDAO(
    @PrimaryKey var id: Int = 0,
    var teacher_id: Int = 0,
    var career_id: Int = 0,
    var topic: String = "",
    var comment: String = "",
    var day: String = "",
    var hour: String = ""
    ): RealmObject()