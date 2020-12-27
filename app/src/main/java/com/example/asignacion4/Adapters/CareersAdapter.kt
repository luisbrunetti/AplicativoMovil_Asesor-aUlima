package com.example.asignacion4.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.asignacion4.R
import com.example.asignacion4.listeners.CallbackAdapter
import com.example.asignacion4.listeners.ClickButtonListener
import com.example.asignacion4.model.remote.Career
import kotlinx.android.synthetic.main.item_career.view.*
import java.util.ArrayList

class CareersAdapter(val context: Context, val careers: ArrayList<Career>, val listener: ClickButtonListener, val callback: CallbackAdapter):
    RecyclerView.Adapter<CareersAdapter.ViewHolder>(),
    Filterable
{
    private lateinit var viewHolder: ViewHolder
    private var careersFull: ArrayList<Career> = ArrayList(careers)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_career, parent, false),
            listener
        )
        return viewHolder
    }

    override fun getItemCount(): Int
    {
        return careers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val currentCareer = careers[position]
        holder.itemView.btnCareer.text = currentCareer.name
    }

    class ViewHolder(view: View, listener: ClickButtonListener): RecyclerView.ViewHolder(view)
    {
        init {
            view.btnCareer.setOnClickListener {
                listener.OnItemButtonListener(it!!, adapterPosition)
            }
        }
    }

    override fun getFilter(): Filter {
        return careerFilter
    }

    private var careerFilter: Filter = object:Filter()
    {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<Career>()
            if(constraint == null || constraint.isEmpty())
            {
                filteredList.addAll(careersFull)
            }
            else
            {
                val filterPattern = constraint.toString().toLowerCase().trim()
                for(c in careersFull)
                {
                    if(c.name.toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(c)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?)
        {
            val resultados = results?.values as Collection<Career>
            if(!resultados.isEmpty())
            {
                callback.setDefaultAdapter()
                careers.clear()
                careers.addAll(results?.values as Collection<Career>)
                notifyDataSetChanged()
            }
            else
            {
                callback.setEmptyAdapter("No se ha encontrado la carrera o especialidad solicitada")
            }
        }

    }
}