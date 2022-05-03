package com.amvlabs.minigames

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.amvlabs.minigames.adapter.OnCLickedGameListener
import com.amvlabs.minigames.adapter.RecyclerViewAdapter
import com.amvlabs.minigames.api.RetrofitInstance
import com.amvlabs.minigames.databinding.ActivityMainBinding
import com.amvlabs.minigames.dialog.MyDialog
import com.amvlabs.minigames.games.TicTacToe
import com.amvlabs.minigames.model.NotificationData
import com.amvlabs.minigames.model.PushNotificationData
import com.amvlabs.minigames.notification.PushNotification
import com.amvlabs.minigames.objects.Constant
import com.amvlabs.minigames.receiver.GpsActiveListener
import com.amvlabs.minigames.receiver.MyReceiver
import com.amvlabs.minigames.service.LocationService
import com.amvlabs.minigames.settings.MainSettings
import com.amvlabs.minigames.sharepreferences.Preference
import com.amvlabs.minigames.utils.DateAndTime
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.api.LogDescriptor
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "mainActivity"
class MainActivity : AppCompatActivity(),OnCLickedGameListener {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var receiver: MyReceiver
    private lateinit var bind: ActivityMainBinding
    private var isDenial = false
    private val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    private var isGpsActive = false
    private lateinit var dialog:MyDialog
    private lateinit var preferences: Preference
    private lateinit var dateAndTime: DateAndTime


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        preferences = Preference(this)
        dateAndTime = DateAndTime()

        dialog = MyDialog(this)

        receiver = MyReceiver()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        isGpsActive = receiver.isGpsEnable(this)


        //requesting Permission
       if(requestPermission()){
           startMyService()
       }else{
           checkDenialDate()
           ActivityCompat.requestPermissions(this, permissions, Constant.REQUEST_CODE)
       }

        bind.recyclerView.layoutManager = LinearLayoutManager(this)
        bind.recyclerView.adapter = RecyclerViewAdapter(this)

    }

    private fun checkDenialDate(){
        val denialDate =preferences.getData(Constant.FIRST_DENIAL_TIME)
        val currentDate = dateAndTime.date()
        Log.d(TAG, "checkDenialDate: $denialDate  $currentDate")

        if (denialDate != null && currentDate != null) {
            if(denialDate != currentDate){
                PushNotificationData(NotificationData("Permission Alert","Please Allow location permission"),
                preferences.getData(Constant.TOKEN)!!).also {
                    sendNotification(it)
                }
            }

        }
    }


    private fun requestPermission():Boolean = !(ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED)



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult: ${grantResults.size}")
        if (requestCode == Constant.REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            startMyService()
        }else{
            preferences.addData(Constant.FIRST_DENIAL_TIME,dateAndTime.date())

        }
    }


    private fun startMyService() {
        if(isGpsActive){
            startForegroundService(Intent(this, LocationService::class.java))
        }else {
            dialog.setDialog("GPS Required!","Enable GPS otherwise location tracking won't work").apply {
                setPositiveButton("Settings", DialogInterface.OnClickListener{i,t ->
                    startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                })
                show()
            }
            
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting_menu -> {
                startActivity(Intent(this, MainSettings::class.java))
                true
            }
            R.id.tc_menu -> {
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
    }
    
    private fun sendNotification(notification:PushNotificationData) = CoroutineScope(Dispatchers.Main).launch {
        try{
            val response = RetrofitInstance.api.pushNotification(notification)
            if(response.isSuccessful){
//                Log.d(TAG, "sendNotification: ${Gson().toJson(response)}")
                Log.d(TAG, "sendNotification:successful ")
            }else{
//                Log.d(TAG, "sendNotification: ${response.errorBody().toString()}")
                Log.d(TAG, "sendNotification: ")
            }
        }catch (e:Exception){
            Log.d(TAG, "sendNotification: $e")
        }
    }

    override fun onClicked(position: Int) {
       startActivity(Intent(this,TicTacToe::class.java))
    }
}