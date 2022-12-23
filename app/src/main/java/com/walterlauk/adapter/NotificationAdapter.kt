package com.walterlauk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walterlauk.R
import com.walterlauk.models.TeamData

class NotificationAdapter(var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_notification, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_notification = view.findViewById<TextView>(R.id.tv_notification)
    }
}