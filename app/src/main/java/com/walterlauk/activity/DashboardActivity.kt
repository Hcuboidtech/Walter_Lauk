package com.walterlauk.activity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import com.walterlauk.R
import com.walterlauk.api.ApiCall
import com.walterlauk.databinding.ActivityDashboardBinding
import com.walterlauk.fragment.SettingsFragment
import com.walterlauk.fragment.DamageFragment
import com.walterlauk.fragment.DepartureFragment
import com.walterlauk.fragment.HomeFragment
import com.walterlauk.models.ResponseGetUserProfile
import com.walterlauk.utils.AppPref
import com.walterlauk.utils.Constants
import com.walterlauk.utils.LocaleHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity(), View.OnClickListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    var gotToChat =""
    var passedNotificationId =""
    var userId: String = ""
    lateinit var dashboardBinding: ActivityDashboardBinding
    lateinit var fragment: Fragment
    val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = dashboardBinding.root
        setContentView(view)

        println("Dashboard calling ->>>")

        AppPref.init(this)

        userId = AppPref.getValue(AppPref.LOGIN_ID, "")!!
        dashboardBinding.notificationImageView.setOnClickListener(this)
        fragment = HomeFragment()
        fragmentManager.beginTransaction().replace(R.id.container, HomeFragment(), "").commit()
        getUserProfileHandler()
        goToProfileActivity()

        dashboardBinding.imgChat.setOnClickListener(View.OnClickListener {
            handleChatClicked()
        })

        dashboardBinding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
        println("dashboard loaded")

//        if (intent.getStringExtra("newsID") != null){
//           passedNotificationId  = intent.getStringExtra("newsID")!!g
//        }
//        println("dashboard loaded 1 ->"+ passedNotificationId)
//      if (!TextUtils.isEmpty(passedNotificationId)){
//          openTheNewsDetailActivity()
//      }
        if(intent.hasExtra("newsID")){
            passedNotificationId =intent.getStringExtra("newsID")!!
            openTheNewsDetailActivity()

        }
        if (intent.hasExtra("goToChat")){
            gotToChat = intent.getStringExtra("goToChat")!!
             println("--    GOTO OPEN   --"+ gotToChat)
            startActivity(Intent(this@DashboardActivity,ChatActivity::class.java))
        }
    }
    private fun openTheNewsDetailActivity(){
        startActivity(Intent(this@DashboardActivity,NewsDetailsActivity::class.java).putExtra(
            "newsID",passedNotificationId
        ))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.home -> {
                fragmentManager.beginTransaction().replace(R.id.container, HomeFragment(), "")
                    .commit()
                return true
            }
            R.id.departure -> {
                /*dashboardBinding.imgFlag.visibility = GONE
                dashboardBinding.languageTv.visibility = GONE*/
                fragmentManager.beginTransaction().replace(R.id.container, DepartureFragment(), "")
                    .commit()
                return true
            }

            R.id.damage -> {
                fragmentManager.beginTransaction().replace(R.id.container, DamageFragment(), "")
                    .commit()
                return true
            }
            R.id.settings -> {
                println("clicked on the settings")
                fragmentManager.beginTransaction().replace(R.id.container, SettingsFragment(), "")
                    .commit()
                return true
            }
        }

        return true
    }

    private fun goToProfileActivity() {
        dashboardBinding.usernameTv.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, ProfileActivity::class.java))
            finish()
        }
    }

    private fun getUserProfileHandler() {
        ApiCall.initApiCall(Constants.BASE_URL)
            .getUserProfile("Bearer" + AppPref.getValue(AppPref.TOKEN, "")!!, userId!!)
            .enqueue(object : Callback<ResponseGetUserProfile> {
                override fun onResponse(
                    call: Call<ResponseGetUserProfile>,
                    response: Response<ResponseGetUserProfile>
                ) {
                    if (response.isSuccessful) {
                        dashboardBinding.usernameTv.text = AppPref.getValue(AppPref.USER_NAME, "")
                        Picasso.get().load(AppPref.getValue(AppPref.PROFILE_IMAGE, ""))
                            .into(dashboardBinding.userImageView)
                        /* Glide
                             .with(this@DashboardActivity)
                             .load(AppPref.getValue(AppPref.PROFILE_IMAGE, ""))
                             .placeholder(R.drawable.user_default_imge)
                             .into(dashboardBinding.userImageView);*/
                    }
                }

                override fun onFailure(call: Call<ResponseGetUserProfile>, t: Throwable) {

                }
            })

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.notification_image_view -> {
                startActivity(Intent(this, NotificationActivity::class.java))
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Config null", Toast.LENGTH_SHORT).show();
        }
        LocaleHelper.setLocale(this, "en")
    }

    private fun handleChatClicked(){
        println("Chat Button Clicked")
        startActivity(Intent(this@DashboardActivity,ChatActivity::class.java))
    }

}