package com.amvlabs.minigames.api

import com.amvlabs.minigames.model.PushNotificationData
import com.amvlabs.minigames.notification.PushNotification
import com.amvlabs.minigames.objects.Constant
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key=${Constant.SERVER_KEY}","Content-Type:${Constant.CONTENT_TYPE}")
    @POST("fcm/send")
    suspend fun pushNotification(@Body notification:PushNotificationData):Response<ResponseBody>
}