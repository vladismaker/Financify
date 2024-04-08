package com.my.application.financify.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.my.application.financify.R

class RecyclerViewAdapterTwo(private val name: ArrayList<String>) : RecyclerView.Adapter<RecyclerViewAdapterTwo.ViewHolder>() {
    private var listener: Listener? = null

    interface Listener {
        fun onClick(position: Int)
    }

    class ViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cv = LayoutInflater.from(parent.context).inflate(R.layout.card_two, parent, false) as CardView
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView

        val tvName: TextView = cardView.findViewById(R.id.id_tv_name)
        tvName.text = name[position]

        cardView.setOnClickListener {
            listener?.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return name.size
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }
}