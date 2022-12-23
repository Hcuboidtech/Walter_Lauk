package com.walterlauk.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.walterlauk.R
import com.walterlauk.models.TeamData


class ContactUsAdapter(
    var context: Context,
    var teamList: ArrayList<TeamData>?,
    var isPermissionallowed: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_contactus_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindData(position)
    }

    override fun getItemCount(): Int {
        return teamList!!.size
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var img_profile = view.findViewById<ImageView>(R.id.img_profile)
        var tv_name = view.findViewById<TextView>(R.id.tv_name)
        var tv_mainrole = view.findViewById<TextView>(R.id.tv_mainrole)
        var tv_mail = view.findViewById<TextView>(R.id.tv_mail)
        var tv_phone1 = view.findViewById<TextView>(R.id.tv_phone1)
        var tv_phone2 = view.findViewById<TextView>(R.id.tv_phone2)

        fun bindData(position: Int) {
            Picasso.get().load(teamList!![position].team_image)
                .into(img_profile)
            tv_name.text = teamList!![position].name
            tv_mainrole.text = teamList!![position].role
            tv_mail.text = teamList!![position].email
            tv_phone1.text = teamList!![position].primary_no
            if (teamList!![position].secondary_no != null) {
                tv_phone2.text = teamList!![position].secondary_no
            } else {
                tv_phone2.visibility = GONE
            }

            tv_phone1.setOnClickListener {
                if (isPermissionallowed) {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:" + teamList!![position].primary_no)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }

            tv_phone2.setOnClickListener {
                if (isPermissionallowed) {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:" + teamList!![position].secondary_no)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }

            tv_mail.setOnClickListener {
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                emailIntent.type = "text/html"
                emailIntent.setPackage("com.google.android.gm")
                val recipients: Array<String>
                recipients = arrayOf(teamList!![position].email!!)
                emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                context.startActivity(emailIntent)
            }
        }
    }
}