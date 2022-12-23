package com.walterlauk.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.walterlauk.R
import com.walterlauk.utils.AppPref


class SplashActivity : AppCompatActivity() {
    var passedTheNotificationID =""
    var passedForTheChat =""
    var keyForMessage = false
    var keyForNews = false
    var handler: Handler? = null
    var runnable: Runnable? = null
    val SPLASH_TIME = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkThePendingIntent()
        AppPref.init(this)
        startIntroScreen()

    }

    private fun checkThePendingIntent() {
        println("Pending Intent Check called")
        val notificationData = intent.getStringExtra("notify")
        if (intent.extras != null){
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!![key]
                Toast.makeText(this, "Key: $key Value: $value", Toast.LENGTH_LONG).show()
                println("check -> " + value)
                println("check -> " + key)
                if (key == "news_id"){
                    println("Found ->"+value)
                    passedTheNotificationID = value.toString()
                    keyForNews = true
                }
                if (key =="notify"){
               passedForTheChat = value.toString()
                  keyForMessage = true
                }
            }
    }
        if (notificationData !=""){
            println("NOTIFICATION DATA IS -> $notificationData")
        }
    }

    private fun startIntroScreen() {
        handler = Handler()
        runnable = Runnable {

            if (AppPref.getValue(AppPref.TOKEN, "") != "") {
                Log.d("SPLASH", "CHECK")
                println(AppPref.getValue(AppPref.TOKEN, "")+ " -- TOKEN")
                val intent = Intent(this@SplashActivity, DashboardActivity::class.java)

                 if (keyForNews) {
                    intent.putExtra("newsID", passedTheNotificationID)
                         }
                 if (keyForMessage){
                 intent.putExtra("goToChat",passedForTheChat)
                                  }
                startActivity(intent)
                finish()

            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        handler!!.postDelayed(runnable!!, SPLASH_TIME.toLong())
    }






}