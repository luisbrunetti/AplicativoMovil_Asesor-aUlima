package com.example.asignacion4.AdvisorFragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.alerts.AlertType
import com.example.alerts.StandardAlert
import com.example.asignacion4.R
import com.example.asignacion4.model.dao.AlumnoDAO
import com.example.asignacion4.model.remote.Career
import com.example.asignacion4.model.remote.Profesor
import kotlinx.android.synthetic.main.fragment_advisor.*
import org.koin.android.ext.android.inject
import java.util.*

private const val KEY_ADVISOR_PASSED = "advisor"
private const val KEY_CAREER_PASSED = "career"

class AdvisorFragment : Fragment(), AdvisorContract.View
{
    val mPresenter: AdvisorPresenter by inject()
    private var advisor: Profesor?=null
    private var career: Career?=null
    private var contact_email = ""
    private var first_name = ""
    private var contact_day : String ? = ""
    private var contact_hour : String ? = ""
    private var currentUser: AlumnoDAO?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.setView(this)
        arguments?.let {
            advisor = it.getSerializable(KEY_ADVISOR_PASSED) as Profesor
            career = it.getSerializable(KEY_CAREER_PASSED) as Career
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advisor, container, false)
    }

    private fun displayCalendar()
    {
        val cal = Calendar.getInstance()
        val datepicker = DatePickerDialog(requireContext(),
            DatePickerDialog.OnDateSetListener { p0, p1, p2, p3 ->
                val monthSelected = p2+1
                var month = if(monthSelected<10) {"0$monthSelected"} else {"$monthSelected"}
                contact_day = """$p3/${month}/$p1"""
                etDay.setText(contact_day)
            },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))
        datepicker.datePicker.minDate = (cal.timeInMillis+(1000*60*60*24))
        datepicker.datePicker.maxDate = (cal.timeInMillis+(1000*60*60*24*7))
        datepicker.show()

    }
    @SuppressLint("SetTextI18n")
    private fun displayHour(){
        val cal = Calendar.getInstance()
        val timpicker = TimePickerDialog(requireContext(),
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val minutes = if (minute < 10) { "0${minute}" } else { "$minute" }
                contact_hour = "$hourOfDay:$minutes"
                etHour.setText(contact_hour)
            },cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true)
        timpicker.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.getData(advisor!!, career!!)
        sendEmailAdvisor.setOnClickListener {
            var topic = etTopic.text.toString()
            mPresenter.sendEmail(topic, contact_day!!, contact_hour!!)
        }
        etDay.setOnClickListener {
            displayCalendar()
        }
        etHour.setOnClickListener{
            displayHour()
        }
    }

    companion object
    {
        @JvmStatic
        fun newInstance(asesor: Profesor, carrera: Career) =
            AdvisorFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_ADVISOR_PASSED, asesor)
                    putSerializable(KEY_CAREER_PASSED, carrera)
                }
            }
    }

    override fun fetchData(name: String, career: String, email: String, user: AlumnoDAO)
    {
        Glide.with(context!!)
            .load(advisor?.img)
            .fitCenter()
            .centerCrop()
            .error(R.drawable.asoeriaulima)
            .into(ivAdvisorImage)
        currentUser = user
        tvAdvisorName.text = name
        tvAdvisorTopic.text = career
        tvAdvisorEmail.text = email
        first_name = advisor?.names!!.split(" ")[0]
        contact_email = email
        contactEmail.text = email
    }

    override fun goToEmail(block: (advisor: Profesor, career:Career, topic: String) -> Unit, blockError: (message: String)->Unit)
    {
        StandardAlert(activity, AlertType.Normal)
            .setContentText("¿Desea mandar un correo a $contact_email?")
            .setCancelButton("No", Dialog::cancel)
            .setConfirmButton("Sí"
            ) {
                var message = "Hola profesor ${first_name.capitalize()}, quería saber si me podría ayudar con el tema ${etTopic.text} el dia $contact_day a la hora $contact_hour, espero su respuesta."+
                        "\n Saludos, " +
                        "\n ${currentUser?.nombres} ${currentUser?.apellidos}"
                var intent = Intent(Intent.ACTION_SEND)
                intent.type = "message/rfc822"
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(contact_email))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Asesoría Ulima: Duda con ${etTopic.text}")
                intent.putExtra(Intent.EXTRA_TEXT, message)
                try {
                    startActivity(Intent.createChooser(intent, "Enviar correo"))
                    it.dismiss()
                    block(advisor!!, career!!, etTopic.text.toString())
                }
                catch (e: android.content.ActivityNotFoundException)
                {
                    blockError("No tiene ninguna app de correo instalado")
                }
            }.show()
    }


    override fun showProgress() {
        cardProgress.visibility = View.VISIBLE
        content_teacher.visibility = View.GONE
    }

    override fun hideProgress() {
        cardProgress.visibility = View.GONE
        content_teacher.visibility = View.VISIBLE
    }

    override fun showMessage(title: String, message: String)
    {
        StandardAlert(activity, AlertType.Normal)
            .setTitleText(title)
            .setContentText(message)
            .setConfirmButton("Ok", Dialog::cancel)
            .show()
    }
}