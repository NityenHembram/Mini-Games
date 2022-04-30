package com.amvlabs.minigames.notification

import android.util.Log
import com.amvlabs.minigames.objects.Constant
import com.amvlabs.minigames.sharepreferences.Preference
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG = "notification"
class MyNotificationService:FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val preference = Preference(this)
        Log.d(TAG, "onNewToken: $token")
        preference.addData(Constant.TOKEN,token)

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "onMessageReceived: ${message.messageId}  ${message.data}")
    }

    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}