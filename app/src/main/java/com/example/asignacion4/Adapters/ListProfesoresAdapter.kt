package com.example.asignacion4.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asignacion4.R
import com.example.asignacion4.listeners.CallbackAdapter
import com.example.asignacion4.listeners.ClickButtonListener
import com.example.asignacion4.model.remote.Profesor
import kotlinx.android.synthetic.main.template_list_profesores.view.*


class ListProfesoresAdapter(val context: Context, ListP: List<Profesor>, val listener: ClickButtonListener, val callback: CallbackAdapter,val carrera : String) :
    RecyclerView.Adapter<ListProfesoresAdapter.ViewHolder>(),Filterable{
    private lateinit var viewHolder: ViewHolder
    private var ListProfesoresCopy : MutableList<Profesor>? = null
    private var ListProfesores:MutableList<Profesor>? = null
    init {
        this.ListProfesores = ArrayList(ListP)
        this.ListProfesoresCopy= ListP as MutableList<Profesor>
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v : View = LayoutInflater.from(context).inflate(R.layout.template_list_profesores,parent,false)
        viewHolder = ViewHolder(v,listener)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return ListProfesoresCopy!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTeacher = ListProfesoresCopy?.get(position)
        if (currentTeacher != null) {
            holder.itemView.tv_templateRV_name.text = "${currentTeacher.last_names} ${currentTeacher.names}"
        }
        holder.itemView.tv_templateRV_carrer.text = carrera
        Glide.with(context)
            .load(currentTeacher!!.img)
            .fitCenter()
            .centerCrop()
            .error(R.drawable.asoeriaulima)
            .into(holder.itemView.iv_templateRV_imgprofesor)
    }
    override fun getFilter(): Filter {
        return ListProfesoresFilter
    }
    private var ListProfesoresFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            Log.d("constraint",constraint.toString())
            val filteredList:MutableList<Profesor> = ArrayList()
            if (constraint == null || constraint.isEmpty()){
                Log.d("test",toString())
                filteredList.addAll(ListProfesores!!)
            }else{
                val filterPattern = constraint.toString().toLowerCase().trim()
                for (item in  ListProfesores!!){
                    if((item.last_names.toLowerCase()).contains(filterPattern)){
                        filteredList.add(item)
                    }
                }
            }
            val result = FilterResults()
            result.values = filteredList
            Log.d("Listresult",result.values.toString())
            return result

        }
        override fun publishResults(constraint: CharSequence?, results: FilterResults?)
        {
            val resultados = results?.values as Collection<Profesor>
            if(!resultados.isEmpty())
            {
                callback.setDefaultAdapter()
                ListProfesoresCopy!!.clear()
                ListProfesoresCopy!!.addAll(resultados)
                Log.d("ListProfesoresopy",ListProfesoresCopy!!.toString())
                notifyDataSetChanged()
            }
            else
            {
                callback.setEmptyAdapter("No se ha encontrado el profesor solicitado.")
            }
        }

    }
    class ViewHolder(view: View, listener: ClickButtonListener): RecyclerView.ViewHolder(view)
    {
        init {
            view.setOnClickListener {
                listener.OnItemButtonListener(it!!, adapterPosition)
            }
        }
    }
}

