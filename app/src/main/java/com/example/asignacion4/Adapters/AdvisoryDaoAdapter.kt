package com.example.asignacion4.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asignacion4.R
import com.example.asignacion4.data.local.DataStorageManager
import com.example.asignacion4.listeners.ClickButtonListener
import com.example.asignacion4.model.dao.AdvisoryDAO
import kotlinx.android.synthetic.main.item_advisory.view.*
import kotlinx.android.synthetic.main.template_list_profesores.view.*

class AdvisoryDaoAdapter(val context: Context, val advisoryDAOList: List<AdvisoryDAO>, val listener: ClickButtonListener, val dataStorageManager: DataStorageManager): RecyclerView.Adapter<AdvisoryDaoAdapter.ViewHolder>()
{
    private lateinit var viewHolder: ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        viewHolder = ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_advisory, parent, false),
            listener
        )
        return viewHolder
    }

    override fun getItemCount(): Int {
        return advisoryDAOList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        var advisory = advisoryDAOList[position]
        var currentAdvisor = dataStorageManager.getAdvisorDAO(advisory.teacher_id)!!
        Glide.with(context)
            .load(currentAdvisor.img)
            .fitCenter()
            .centerCrop()
            .error(R.drawable.asoeriaulima)
            .into(holder.itemView.ivAdvisorDAO)
        holder.itemView.tvTopic.text = "TEMA: ${advisory.topic}"
        holder.itemView.tvAdvisor.text = "ASESOR: ${currentAdvisor.full_name}"
    }

    class ViewHolder(view:View, listener: ClickButtonListener): RecyclerView.ViewHolder(view)
    {
        init {
            view.setOnClickListener {
                listener.OnItemButtonListener(it!!, adapterPosition)
            }
        }
    }


}