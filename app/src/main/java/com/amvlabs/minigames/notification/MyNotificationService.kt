package com.amvlabs.minigames.notification

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.amvlabs.minigames.objects.Constant
import com.amvlabs.minigames.service.LocationService
import com.amvlabs.minigames.sharepreferences.Preference
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG = "notification"
class MyNotificationService:FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
        val preference = Preference(this)
        preference.addData(Constant.TOKEN,token)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "onMessageReceived: ${message.messageId}  ${message.data}")
        val data = message.data
        val showNotification = ShowNotification(this)
        if(data.isNotEmpty()){
            showNotification.createNotification(data["title"]!!,data["message"]!!)
        }


    }

    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}