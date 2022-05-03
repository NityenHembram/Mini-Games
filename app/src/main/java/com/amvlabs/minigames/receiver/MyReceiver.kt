package com.amvlabs.minigames.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity

import com.amvlabs.minigames.MainActivity
import com.amvlabs.minigames.service.LocationService


class MyReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, p1: Intent?) {

        val intent = Intent(context, LocationService::class.java)
        context?.startForegroundService(intent)

        setGpsListener(MainActivity() as GpsActiveListener,context!!)
    }

    private fun setGpsListener(listener: GpsActiveListener,context: Context) {
        listener.onGpsActive(isGpsEnable(context))
    }

     fun isGpsEnable(context: Context): Boolean {
        val manager: LocationManager = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}

interface GpsActiveListener {
    fun onGpsActive(isActive: Boolean)
}