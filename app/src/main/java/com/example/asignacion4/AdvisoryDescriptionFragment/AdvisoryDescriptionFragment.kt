package com.example.asignacion4.AdvisoryDescriptionFragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.alerts.AlertType
import com.example.alerts.StandardAlert
import com.example.asignacion4.R
import com.example.asignacion4.listeners.CallbacksAdvisoryDescriptionFragment
import com.example.asignacion4.model.dao.AdvisorDAO
import com.example.asignacion4.model.dao.AdvisoryDAO
import kotlinx.android.synthetic.main.fragment_advisory_description.*
import kotlinx.android.synthetic.main.fragment_advisory_description.cardProgress
import kotlinx.android.synthetic.main.fragment_advisory_description.ivAdvisorImage
import org.koin.android.ext.android.inject
import java.util.*

private const val ID_ADVISORY_PASSED = "advisory"

class AdvisoryDescriptionFragment : Fragment(), AdvisoryDescriptionContract.View {
    private val mPresenter: AdvisoryDescriptionPresenter by inject()
    private var id_advisory: Int? = 0
    private lateinit var advisory: AdvisoryDAO
    private lateinit var teacher: AdvisorDAO
    private var topic:String?=""
    private var day:String?=""
    private var hour:String?=""
    private var comments:String?=""
    private lateinit var listener: CallbacksAdvisoryDescriptionFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mPresenter.setView(this)
        arguments?.let {
            id_advisory = it.getInt(ID_ADVISORY_PASSED)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as CallbacksAdvisoryDescriptionFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advisory_description, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.getData(id_advisory!!)
        tvTopic.setOnClickListener {
            StandardAlert(context, AlertType.InputText)
                .setTitleText("Ingrese tema a tratar:")
                .setCancelButton("CANCELAR", Dialog::cancel)
                .setConfirmButtonWithText(
                    "OK"
                ) { alert, text ->
                    topic = text
                    tvTopic.text = topic
                    alert.dismiss()
                }.show()
        }
        tvDay.setOnClickListener {
            displayCalendar()
        }
        tvHour.setOnClickListener {
            displayHour()
        }
        btnUpdate.setOnClickListener {
            var commentarios = etComments.text.toString()
            comments = commentarios
            mPresenter.updateData(id_advisory!!,topic!!, day!!, hour!!, comments!!)
        }
        btnCreate.setOnClickListener {
            mPresenter.createReminder(id_advisory!!,topic!!, day!!, hour!!, comments!!)
        }
    }

    companion object {
        fun newInstance(id_advisory: Int) = AdvisoryDescriptionFragment().apply {
            arguments = Bundle().apply {
                putInt(ID_ADVISORY_PASSED, id_advisory)
            }
        }
    }

    override fun fetchData(teacher: AdvisorDAO, advisory: AdvisoryDAO, block: () -> Unit) {
        Glide.with(context!!)
            .load(teacher.img)
            .fitCenter()
            .centerCrop()
            .error(R.drawable.asoeriaulima)
            .into(ivAdvisorImage)
        this.teacher = teacher
        tvTeacherName.text = teacher.full_name

        this.advisory = advisory
        tvTopic.text = advisory.topic
        tvDay.text = advisory.day
        tvHour.text = advisory.hour
        etComments.setText(advisory.comment)

        topic = advisory.topic
        day = advisory.day
        hour= advisory.hour
        comments = advisory.comment
        block()
    }

    override fun goToReminder(topic: String, timeInMilis: Long, block: () -> Unit)
    {
        StandardAlert(activity, AlertType.Normal)
            .setContentText("¿Desea crear un recordatorio?")
            .setCancelButton("No", Dialog::cancel)
            .setConfirmButton("Sí")
            {
                val insertCalendarIntent = Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, "Asesoría: ${topic}")
                    .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, timeInMilis)
                    .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE)
                startActivity(insertCalendarIntent)
                it.dismiss()
                block()
                listener.returnToAsesoriesFragment(this)
            }.show()
    }

    private fun displayCalendar() {
        val cal = Calendar.getInstance()
        val datepicker = DatePickerDialog(requireContext(),
            DatePickerDialog.OnDateSetListener { p0, p1, p2, p3 ->
                val monthSelected = p2+1
                var month = if(monthSelected<10) {"0$monthSelected"} else {"$monthSelected"}
                day = """$p3/${month}/$p1"""
                tvDay.text = day
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
        datepicker.datePicker.minDate = (cal.timeInMillis + (1000 * 60 * 60 * 24))
        datepicker.datePicker.maxDate = (cal.timeInMillis + (1000 * 60 * 60 * 24 * 7))
        datepicker.show()
    }

    @SuppressLint("SetTextI18n")
    private fun displayHour() {
        val cal = Calendar.getInstance()
        val timpicker = TimePickerDialog(requireContext(),
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val minutes = if (minute < 10) { "0${minute}" } else { "$minute" }
                hour = "$hourOfDay:$minutes"
                tvHour.text =hour
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true
        )
        timpicker.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu_adfragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.delete ->
            {
                StandardAlert(activity, AlertType.Normal)
                    .setContentText("¿Desea eliminar esta asesoría?")
                    .setCancelButton("No", Dialog::cancel)
                    .setConfirmButton("Sí"){
                        mPresenter.deleteAdvisory(id_advisory!!){listener.returnToAsesoriesFragment(this)}
                        it.dismiss()
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProgress() {
        mScrollView.visibility = View.GONE
        cardProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mScrollView.visibility = View.VISIBLE
        cardProgress.visibility = View.GONE
    }

    override fun showMessage(title: String, message: String) {
        StandardAlert(activity, AlertType.Normal)
            .setTitleText(title)
            .setContentText(message)
            .setConfirmButton("Ok", Dialog::cancel)
            .show()
    }
}