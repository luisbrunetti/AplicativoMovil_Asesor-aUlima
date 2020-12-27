package com.example.asignacion4.data.local

import com.example.asignacion4.model.dao.AdvisorDAO
import com.example.asignacion4.model.dao.AdvisoryDAO
import com.example.asignacion4.model.dao.AlumnoDAO
import com.example.asignacion4.model.dao.CareerDAO
import com.example.asignacion4.model.remote.Career
import com.example.asignacion4.model.remote.Profesor
import io.realm.Realm

class DataStorageManager(val realm: Realm)
{
    fun getAllCareers(): List<CareerDAO>
    {
        return realm.where(CareerDAO::class.java).findAll()
    }

    fun createCareerDAO(career: Career)
    {
        realm.executeTransaction { realm ->
            var newCareer = getCareer(career.id)
            if(newCareer==null)
            {
                newCareer = CareerDAO()
                newCareer.id = career.id
            }
            newCareer.name = career.name
            realm.copyToRealmOrUpdate(newCareer)
        }
    }

    fun getCareer(id: Int): CareerDAO?
    {
        return realm.where(CareerDAO::class.java).equalTo("id", id).findFirst()
    }

    fun createAdvisorDAO(profesor: Profesor)
    {
        realm.executeTransaction { realm ->
            var advisorDAO = getAdvisorDAO(profesor.id)
            if(advisorDAO==null)
            {
                advisorDAO = AdvisorDAO()
                advisorDAO.id = profesor.id
            }
            var full_name = "${profesor.names} ${profesor.last_names}"
            advisorDAO.full_name = full_name
            advisorDAO.img = profesor.img
            realm.copyToRealmOrUpdate(advisorDAO)
        }
    }

    fun getAllAdvisors(): List<AdvisorDAO>
    {
        return realm.where(AdvisorDAO::class.java).findAll()
    }

    fun getAdvisorDAO(id: Int): AdvisorDAO?
    {
        return realm.where(AdvisorDAO::class.java).equalTo("id", id).findFirst()
    }

    fun createAdvisory(teacher_id: Int, career_id: Int, topic: String, comments: String, day: String, hour: String)
    {
        realm.executeTransaction {realm ->
            var advisory = AdvisoryDAO()
            advisory.id = getAutoIncrementPrimaryKey()
            advisory.teacher_id = teacher_id
            advisory.career_id = career_id
            advisory.topic = topic
            advisory.comment = comments
            advisory.day = day
            advisory.hour = hour
            realm.copyToRealmOrUpdate(advisory)
        }
    }

    fun getAdvisoryDAO(id: Int): AdvisoryDAO?
    {
        return realm.where(AdvisoryDAO::class.java).equalTo("id", id).findFirst()
    }

    fun updateAdvisoryDAO(id: Int, topic: String, day: String, hour:String, comments: String)
    {
        realm.executeTransaction { realm ->
            var advisory = getAdvisoryDAO(id)
            if(advisory!=null)
            {
                advisory.topic = topic
                advisory.day = day
                advisory.hour = hour
                advisory.comment = comments
                realm.copyToRealmOrUpdate(advisory)
            }
        }
    }

    fun deleteAdvisoryDAO(id: Int)
    {
        realm.executeTransaction { realm ->
            getAdvisoryDAO(id)?.deleteFromRealm()
        }
    }

    fun getAllAdvisoryDAO(): List<AdvisoryDAO>
    {
        return realm.where(AdvisoryDAO::class.java).findAll()
    }

    fun getAutoIncrementPrimaryKey(): Int
    {
        var currentId = realm.where(AdvisoryDAO::class.java).max("id")
        var nextId = 0
        if(currentId==null)
        {
            nextId = 1
        }
        else
        {
            nextId = currentId.toInt() + 1
        }
        return nextId
    }

    fun createOrUpdateAlumnoDAO(alumno: AlumnoDAO)
    {
        realm.executeTransaction { realm->
            realm.copyToRealmOrUpdate(alumno)
        }
    }

    fun getCurrentUser(): AlumnoDAO?
    {
        return realm.where(AlumnoDAO::class.java).findFirst()
    }

    fun updateCurrentUser(nombres: String, apellidos: String, carrera: String){
        realm.executeTransaction { realm: Realm ->
            var alumnoActualizado = getCurrentUser()
            if(alumnoActualizado != null){
                alumnoActualizado.nombres = nombres
                alumnoActualizado.apellidos = apellidos
                alumnoActualizado.carrera = carrera
            }
        }
    }

    fun clearAll()
    {
        deleteCareers()
        deleteAdvisors()
        deleteAllAdvisoryDAO()
        deleteCurrentUser()
    }

    fun deleteCurrentUser()
    {
        realm.executeTransaction {
            val user = getCurrentUser()
            user?.deleteFromRealm()
        }
    }

    fun deleteAllAdvisoryDAO()
    {
        realm.executeTransaction {
            val asesorias = getAllAdvisoryDAO()
            if(!asesorias.isEmpty())
            {
                for(a in asesorias)
                {
                    a.deleteFromRealm()
                }
            }
        }
    }

    fun deleteAdvisors()
    {
        realm.executeTransaction {
            val advisors = getAllAdvisors()
            if(!advisors.isEmpty())
            {
                for(a in advisors)
                {
                    a.deleteFromRealm()
                }
            }
        }
    }

    fun deleteCareers()
    {
        realm.executeTransaction {
            val careers = getAllCareers()
            if(!careers.isEmpty())
            {
                for(c in careers)
                {
                    c.deleteFromRealm()
                }
            }
        }
    }

}