package com.walterlauk.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.walterlauk.R
import com.walterlauk.activity.SplashActivity

class FirebaseMessageReceiver : FirebaseMessagingService() {




    var news_ID =""
    var messageBody =""
    override fun onMessageReceived(message: RemoteMessage) {
       if(message.data["news_id"] != null)
       {
        news_ID = message.data["news_id"]!!
       }
        println("message ->"+message.data["news_id"])
        println("!!!body = ${message.data["body"]}")
        println("!!!title = ${message.data["title"]}")
       message.notification?.let {
        Log.d("TAG ->", "Message Notification Body: ${it.body}")
         messageBody = it.body.toString()
        }
        println("message body"+ messageBody)
        if(message.data["news_id"] != null){
//
            sendNotification(this, message.data["type"], message.data["title"])
                  println("Send Notification called") }

        if (messageBody != null){
            sendNotification(this, messageBody, message.data["title"])
        }

    }

    private fun sendNotification(context: Context, body: String?, title: String?) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val CHANNEL_ID = "121"
        if (Build.VERSION.SDK_INT >= 26) {
            val mChannel =
                NotificationChannel(CHANNEL_ID, "CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(mChannel)
        }

        val intent = Intent(context, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("notify",body)
        var pendingIntent:PendingIntent? =null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent =
                PendingIntent.getActivity(this,
                    0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
        }else{
         pendingIntent =  PendingIntent.getActivity(
                context,
                0 ,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setColor(Color.parseColor("#41B0DD"))
                /*.setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.getResources(),
                        R.mipmap.ic_launcher
                    )
                )*/
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)

        notificationBuilder.setContentText(body)
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }



    fun getMessage(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FIREBASE", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
                       Log.d("FIREBASE", token)
            Toast.makeText(baseContext, "FIREBASE", Toast.LENGTH_SHORT).show()
        })
    }


}