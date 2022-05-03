package com.amvlabs.minigames.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.SyncStateContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.amvlabs.minigames.LocationHelper
import com.amvlabs.minigames.MyLocationListener
import com.amvlabs.minigames.R
import com.amvlabs.minigames.database.FireDatabase
import com.amvlabs.minigames.objects.Constant
import com.amvlabs.minigames.sharepreferences.Preference
import com.amvlabs.minigames.utils.DateAndTime
import com.google.android.gms.common.internal.Constants
import java.text.SimpleDateFormat

import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "tag"
class LocationService:Service() {
    private val NOTIFICATION_CHANNEL_ID = "my_notification_location"
    private lateinit var db:FireDatabase
    private var data = mutableMapOf<String,String>()
    private lateinit var preferences: Preference
    private lateinit var dateAndTime:DateAndTime

    private lateinit var time:String
    private lateinit var date:String


    companion object{
        var mLocation :Location? = null
        val isServiceStarted  = false
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate() {
        super.onCreate()
        dateAndTime = DateAndTime()
        db = FireDatabase()
        db.getInstance(this)
        preferences = Preference(this)
        showNotification()
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: pre")

        if(intent != null && Constant.STOP_FOREGROUND_SERVICE == intent.action){
            stopForeground(true)
            stopService(Intent(this,LocationService::class.java))
            return START_NOT_STICKY
        }


        LocationHelper().startListeningUserLocation(this,object :MyLocationListener{
            override fun onLocationChanged(location: Location) {
                mLocation = location
                mLocation.let {
                    val lat  = it?.latitude
                    val lng = it?.longitude

                    date = dateAndTime.date()
                    time = dateAndTime.time()
                    data[time] = "$lat $lng"
                    preferences.getData(Constant.TOKEN)?.let { it1 -> db.setData(it1,date,data) }
                    Log.d(TAG, "onLocationChanged: ${it?.latitude}  ${it?.longitude}")
                }
            }

        })
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }




    @RequiresApi(Build.VERSION_CODES.S)
    private fun showNotification() {
        val intent = Intent(this,LocationService::class.java)
        intent.action = Constant.STOP_FOREGROUND_SERVICE;
        val pendingIntent = PendingIntent.getForegroundService(this,0,intent,PendingIntent.FLAG_MUTABLE)
        val builder:NotificationCompat.Builder = NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
            .setOngoing(false)
            .setSmallIcon(R.drawable.ic_baseline_settings_24)
            .addAction(R.drawable.ic_baseline_close_24,"close",pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val manager:NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,NOTIFICATION_CHANNEL_ID,NotificationManager.IMPORTANCE_LOW)
            channel.description = NOTIFICATION_CHANNEL_ID
            channel.setSound(null,null)
            manager.createNotificationChannel(channel)
            startForeground(1,builder.build())
        }
    }
}