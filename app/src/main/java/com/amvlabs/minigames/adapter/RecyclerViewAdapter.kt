package com.amvlabs.minigames.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amvlabs.minigames.R

class RecyclerViewAdapter(private val listener: OnCLickedGameListener): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>()
{
    class ViewHolder(itemView:View,private val listener: OnCLickedGameListener):RecyclerView.ViewHolder(itemView),View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
          listener.onClicked(adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.game_items,parent,false)
        return ViewHolder(v,listener)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}

interface OnCLickedGameListener{
    fun onClicked(position:Int)
}