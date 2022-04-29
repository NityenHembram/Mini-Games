package com.amvlabs.minigames.receiver

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.amvlabs.minigames.MainActivity
import com.amvlabs.minigames.service.LocationService



class MyReceiver:BroadcastReceiver() {
    private lateinit var manager:LocationManager
    private var enabled = false
    override fun onReceive(context: Context?, p1: Intent?) {
         manager = context?.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
         enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val intent = Intent(context, LocationService::class.java)
        context.startForegroundService(intent)

        setGpsListener(MainActivity() as GpsActiveListener)
    }

    private fun setGpsListener(listener: GpsActiveListener){
            listener.onGpsActive(enabled)
    }
}

interface GpsActiveListener{
    fun onGpsActive(isActive:Boolean)
}