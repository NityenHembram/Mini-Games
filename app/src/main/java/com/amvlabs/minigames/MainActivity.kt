package com.amvlabs.minigames

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.amvlabs.minigames.adapter.RecyclerViewAdapter
import com.amvlabs.minigames.databinding.ActivityMainBinding
import com.amvlabs.minigames.objects.Constant
import com.amvlabs.minigames.receiver.GpsActiveListener
import com.amvlabs.minigames.receiver.MyReceiver
import com.amvlabs.minigames.service.LocationService
import com.amvlabs.minigames.settings.MainSettings
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

private const val TAG = "mainActivity"
class MainActivity : AppCompatActivity(), GpsActiveListener {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var receiver: BroadcastReceiver
    private lateinit var bind: ActivityMainBinding
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private var isGpsActive = false


//    val contract = registerForActivityResult(Contract()){
//        Log.d(TAG, "$it: ")
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        requestPermission()



        receiver = MyReceiver()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        bind.recyclerView.layoutManager = LinearLayoutManager(this)
        bind.recyclerView.adapter = RecyclerViewAdapter()

    }




    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, permissions, Constant.REQUEST_CODE)
        } else {
//                startMyService()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult: $requestCode")
        if (requestCode == Constant.REQUEST_CODE) {
            if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                && shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)){
                startMyService()
            }else{
                requestPermission()
            }
        }
    }


    private fun startMyService() {
        try{
            val intent = Intent(this, LocationService::class.java)
            startForegroundService(intent)
        }catch (e:Exception){
            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

    override fun onGpsActive(isActive: Boolean) {
        isGpsActive = isActive
        Log.d(TAG, "onGpsActive: $isActive")
    }
}