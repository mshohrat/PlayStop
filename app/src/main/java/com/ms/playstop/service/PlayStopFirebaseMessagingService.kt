package com.ms.playstop.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ms.playstop.R
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.RegisterFbTokenRequest

class PlayStopFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Glide.with(this)
            .asBitmap()
            .load(p0.notification?.imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    showNotification(p0.notification,resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    showNotification(p0.notification,null)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun showNotification(notification: RemoteMessage.Notification?, resource: Bitmap?) {
        createNotificationChannel()
        val notif = NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(notification?.title)
            .setContentText(notification?.body)
            .setLargeIcon(resource)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(resource)
                .bigLargeIcon(null))
            .setColor(ContextCompat.getColor(this,R.color.colorAccent))
            .build()
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.notify(Math.random().toInt(),notif)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.default_notification_channel_name)
            val descriptionText = getString(R.string.default_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(getString(R.string.default_notification_channel_id), name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    @SuppressLint("CheckResult")
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        ApiServiceGenerator.getApiService.registerFbToken(RegisterFbTokenRequest(p0))
            ?.initSchedulers()
            ?.subscribe({},{})
    }
}