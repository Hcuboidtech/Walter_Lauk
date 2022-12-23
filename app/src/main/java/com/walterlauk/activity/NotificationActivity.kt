package com.walterlauk.activity

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.walterlauk.R
import com.walterlauk.adapter.NotificationAdapter
import com.walterlauk.databinding.ActivityNotificationBinding

class NotificationActivity : BaseActivity(), View.OnClickListener {
    lateinit var notificationBinding: ActivityNotificationBinding
    lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationBinding = ActivityNotificationBinding.inflate(layoutInflater)
        val view = notificationBinding.root
        setContentView(view)

        notificationBinding.toolbar.imgBack.setOnClickListener(this)
        notificationBinding.toolbar.tvHeader.setText(getString(R.string.notification))
        notificationBinding.toolbar.tvDelete.visibility = VISIBLE
        notificationBinding.toolbar.tvDelete.setOnClickListener(this)

        setNotificationAdapter()

    }

    private fun setNotificationAdapter() {
        notificationAdapter = NotificationAdapter(this)
        var layoutManager = LinearLayoutManager(this)
        notificationBinding.rvNotification.layoutManager = layoutManager
        notificationBinding.rvNotification.adapter = notificationAdapter
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> {
                onBackPressed()
            }
        }
    }
}