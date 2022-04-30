package com.amvlabs.minigames.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amvlabs.minigames.R

private const val TAG = "detail"
class DetailRecyclerViewAdapter(private val list:ArrayList<String>, private val listener: OnDetailItemClickListener):
    RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View,val listener: OnDetailItemClickListener,private val list:ArrayList<String>): RecyclerView.ViewHolder(itemView),View.OnClickListener {
        val textView: TextView = itemView.findViewById<TextView>(R.id.detail_date_txt)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            listener.onItemClicked(adapterPosition,list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.detail_item,parent,false)
        return ViewHolder(v, listener,list)
    }

    override fun onBindViewHolder(holder: DetailRecyclerViewAdapter.ViewHolder, position: Int) {
        val a = list[position]
        Log.d(TAG, "onBindViewHolder:  $a")
        holder.textView.text = a.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}
interface OnDetailItemClickListener{
    fun onItemClicked(position:Int,list:ArrayList<String>)
}