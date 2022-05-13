package com.kashif.realtimedatabase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

private const val CHANNEL_NAME = "notificationChannelName"

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        generateNotification(message)
    }

    private fun generateNotification(message: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java)
        val notificationManger =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManger)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(applicationContext, getString(R.string.channel_id))
            .setContentTitle(message.notification!!.title)
            .setContentText(message.notification!!.body)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManger.notify(Random.nextInt(), notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            getString(R.string.channel_id),
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Description for notification channel"
            enableLights(true)
            lightColor = Color.CYAN
        }
        notificationManager.createNotificationChannel(channel)
    }

}