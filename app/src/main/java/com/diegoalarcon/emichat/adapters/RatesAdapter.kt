package com.diegoalarcon.emichat.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diegoalarcon.emichat.R
import com.diegoalarcon.emichat.inflate
import com.diegoalarcon.emichat.models.Rate
import com.diegoalarcon.emichat.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_rates_item.view.*
import java.text.SimpleDateFormat

class RatesAdapter(private val items: List<Rate>) : RecyclerView.Adapter<RatesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.fragment_rates))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(rate:Rate) = with(itemView){
            textViewRate.text = rate.text
            textViewStar.text = rate.rate.toString()
            textViewCalendar.text = SimpleDateFormat("dd MMM, yyyy").format(rate.createdAt)
            if(rate.profileImgUrl.isEmpty()) {
                Picasso.get().load(R.drawable.ic_person).resize(100, 100)
                    .centerCrop().transform(CircleTransform()).into(imageViewProfile)
            } else {
                Picasso.get().load(rate.profileImgUrl).resize(100, 100)
                    .centerCrop().transform(CircleTransform()).into(imageViewProfile)
            }
        }
    }
}