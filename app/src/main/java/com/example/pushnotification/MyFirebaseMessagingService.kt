package com.example.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "MyFirebaseMessagingService"

    // private val sharedPreferences = SharedPreferences(this@MyFirebaseMessagingService)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e(TAG, "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e(TAG, "onMessageReceived: ${message?.from}")


//        if (message.notification.isNotEmpty()) {
//            // Handle data
//            val data = message.notification
//            val title: String? = data["title"]
//            val message: String? = data["body"]
//            Log.e(
//                TAG, "onMessageReceived: ${title?.toString() + " " + message.toString()}",
//            )
//
//            // pass the body of massage to mainActivity with help of intent
//            val intent = Intent(this@MyFirebaseMessagingService, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            intent.putExtra("Intent_message", message)
//            startActivity(intent)
//
//        }
        val notificatoinBody = message.notification?.body.toString()
        val intent = Intent(this@MyFirebaseMessagingService, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("Intent_message_new", notificatoinBody)
        startActivity(intent)

        sendNotification(message)


    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
        Log.e(TAG, "handleIntent: $intent")
        //  intent?.putExtra("Intent_message_new", notificatoinBody)
        val newMessage = intent?.extras?.getString("gcm.notification.body")
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mainIntent?.putExtra("Intent_message_newMessage", newMessage)
        startActivity(mainIntent)

        if (intent?.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!!.getString(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }
    }

    private fun sendNotification(message: RemoteMessage) {
        val channel_id: String = getString(R.string.default_notification_channel_id)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder =
            NotificationCompat.Builder(this, channel_id).setContentText(message.notification?.body)
                .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.notifications_active).setSound(defaultSound)
                .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(
            notificationChannel
        )
        notificationManager.notify(0, notificationBuilder.build())
    }

}