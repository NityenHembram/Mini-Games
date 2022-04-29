package com.amvlabs.minigames

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.amvlabs.minigames.objects.Constant



class LocationHelper {
    @SuppressLint("MissingPermission")
    fun startListeningUserLocation(context: Context, listener: MyLocationListener){
        val locationManager:LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener: LocationListener = object :LocationListener{
            override fun onLocationChanged(location: Location) {
                listener.onLocationChanged(location)
            }


            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
            }

            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,Constant.LOCATION_REFRESH_TIME,Constant.LOCATION_REFRESH_DISTANCE
        ,locationListener,)
    }
}

interface MyLocationListener{
    fun onLocationChanged(location:Location)
}