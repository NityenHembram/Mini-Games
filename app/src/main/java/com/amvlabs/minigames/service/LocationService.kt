package com.amvlabs.minigames.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.amvlabs.minigames.LocationHelper
import com.amvlabs.minigames.MyLocationListener
import com.amvlabs.minigames.R
import com.amvlabs.minigames.database.FireDatabase
import java.text.SimpleDateFormat

import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "tag"
class LocationService:Service() {
    private val NOTIFICATION_CHANNEL_ID = "my_notification_location"

    private lateinit var calendar:Calendar
    private lateinit var dateFormat:SimpleDateFormat
    private lateinit var date:String
    private lateinit var time:String
    private lateinit var db:FireDatabase
    private var dateAndTime = listOf<String>()
    private var data = mutableMapOf<String,String>()


    companion object{
        var mLocation :Location? = null
        val isServiceStarted  = false
    }

    override fun onCreate() {
        super.onCreate()
        db = FireDatabase()
        db.getInstance()


        val builder:NotificationCompat.Builder = NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
            .setOngoing(false)
            .setSmallIcon(R.drawable.ic_baseline_settings_24)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val manager:NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,NOTIFICATION_CHANNEL_ID,NotificationManager.IMPORTANCE_LOW)
            channel.description = NOTIFICATION_CHANNEL_ID
            channel.setSound(null,null)
            manager.createNotificationChannel(channel)
            startForeground(1,builder.build())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        val timer = Timer()


        LocationHelper().startListeningUserLocation(this,object :MyLocationListener{
            override fun onLocationChanged(location: Location) {
                mLocation = location
                mLocation.let {
                    val lat  = it?.latitude
                    val lng = it?.longitude

                    dateAndTime = getDateAndTime()
                    date = dateAndTime[0]
                    time = dateAndTime[1]
                    data[time] = "$lat $lng"
                    db.setData("MyLocation",date,data)
                    Log.d(TAG, "onLocationChanged: ${it?.latitude}  ${it?.longitude}")
                }
            }

        })
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun getDateAndTime():List<String>{
        calendar = Calendar.getInstance(TimeZone.getDefault())
        dateFormat  = SimpleDateFormat("MM-dd-yyyy HH:mm:ss" )
        date = dateFormat.format(calendar.time)
        return date.split(" ")
    }
}