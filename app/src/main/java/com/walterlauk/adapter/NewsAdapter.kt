package com.walterlauk.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.walterlauk.R
import com.walterlauk.activity.NewsDetailsActivity
import com.walterlauk.models.NewsData
import com.walterlauk.utils.AppPref

class NewsAdapter(var context: Context, var newsList: ArrayList<com.walterlauk.utils.NewsData>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_news_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindData(position)
    }

    override fun getItemCount(): Int {
        return newsList!!.size
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var img_news = view.findViewById<ImageView>(R.id.img_news)
        var tv_newsTitle = view.findViewById<TextView>(R.id.tv_newsTitle)
        var tv_desc = view.findViewById<TextView>(R.id.tv_desc)
        var linearLayout_news_holder = view.findViewById<LinearLayout>(R.id.linearLayout_news_holder)

        fun bindData(position: Int) {
            println("DRIVER TYPE 1"+AppPref.getValue(AppPref.DRIVER_TYPE,""))
            if (newsList!![position].select_type ==AppPref.getValue(AppPref.DRIVER_TYPE,"")) {
                tv_newsTitle.text = newsList!![position].news_title
                tv_desc.text = newsList!![position].news_description
                Picasso.get().load(newsList!![position].news_img).into(img_news)

                linearLayout_news_holder.setOnClickListener {
                    context.startActivity(Intent(context,NewsDetailsActivity::class.java).
                    putExtra("title",newsList!![position].news_title)
                        .putExtra("description",newsList!![position].news_description)
                        .putExtra("image",newsList!![position].news_img))
                }
            }


        }
    }
}