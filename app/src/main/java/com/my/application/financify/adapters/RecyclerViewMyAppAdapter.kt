package com.my.application.financify.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.my.application.financify.R

class RecyclerViewMyAppAdapter(private val allDataList: MutableList<Array<String>>) : RecyclerView.Adapter<RecyclerViewMyAppAdapter.ViewHolder>() {
    class ViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun getItemCount(): Int {
        return allDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cv = LayoutInflater.from(parent.context).inflate(R.layout.card_two_my_app, parent, false) as CardView
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView

        val tvNumber: TextView = cardView.findViewById(R.id.id_number)
        val tvName: TextView = cardView.findViewById(R.id.id_name)
        val layout: CardView = cardView.findViewById(R.id.card_view)
        val tvPercent: TextView = cardView.findViewById(R.id.id_percent)
        val tvPrice: TextView = cardView.findViewById(R.id.id_price)


        if(checkEvenOrOdd(position)){
            //layout.setBackgroundColor(R.color.grey)
            layout.setBackgroundResource(R.color.grey)
        }else{
            //layout.setBackgroundColor(ContextCompat.getColor(R.color.white))
            layout.setBackgroundResource(R.color.white)
        }

        val number = position+1
        tvNumber.text = number.toString()
        when (position) {
            0 -> {
                tvNumber.text = "1"
                tvNumber.setBackgroundResource(R.drawable.sircle1)
            }
            1 -> {
                tvNumber.text = "2"
                tvNumber.setBackgroundResource(R.drawable.sircle2)
            }
            2 -> {
                tvNumber.text = "3"
                tvNumber.setBackgroundResource(R.drawable.sircle3)
            }
        }

        tvName.text = allDataList[position][0]
        val doublePrice = allDataList[position][1].toDouble()
        val stringPrice = "%.3f".format(doublePrice)
        tvPrice.text = stringPrice
        val dPercent = allDataList[position][2].toDouble()
        val stringOne = "%.2f".format(dPercent)
        val stringTwo = if(dPercent<0) {
            "$stringOne %"
        }else {
            "+$stringOne %"
        }
        tvPercent.text = stringTwo
    }

    private fun checkEvenOrOdd(number: Int): Boolean {
        return number % 2 == 0
    }
}