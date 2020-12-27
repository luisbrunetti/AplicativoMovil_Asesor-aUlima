package com.example.asignacion4.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asignacion4.R
import kotlinx.android.synthetic.main.item_empty.view.*

class EmptyAdapter(val context:Context, val title: String) : RecyclerView.Adapter<EmptyAdapter.ViewHolderEmpty>() {

    private lateinit var viewHolder: ViewHolderEmpty
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEmpty {
        val view = LayoutInflater.from(context).inflate(R.layout.item_empty, parent, false)
        viewHolder = ViewHolderEmpty(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolderEmpty, position: Int)
    {
        holder.itemView.tvTitle.text = title
    }


    override fun getItemCount(): Int
    {
        return 1
    }

    class ViewHolderEmpty(view: View): RecyclerView.ViewHolder(view)
    {

    }
}