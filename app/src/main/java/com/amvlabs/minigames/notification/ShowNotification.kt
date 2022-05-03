package com.amvlabs.minigames.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.amvlabs.minigames.MainActivity
import com.amvlabs.minigames.R
import com.amvlabs.minigames.objects.Constant
import kotlin.random.Random

class ShowNotification(val context:Context) {
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationID = Random.nextInt()

    @RequiresApi(Build.VERSION_CODES.S)
    fun createNotification(message:String, description:String){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel()
        }
        val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_MUTABLE)
        val notification = NotificationCompat.Builder(context,Constant.CHANNEL_ID)
            .setContentTitle(message)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_settings_24)
            .build()
        manager.notify(notificationID,notification)
    }

    private fun createNotificationChannel(){
        val channelName = "my_channel"

        val channel = NotificationChannel(Constant.CHANNEL_ID,channelName,IMPORTANCE_HIGH).apply {
            description = "My channel discription"
        }
        manager.createNotificationChannel(channel)
    }
}