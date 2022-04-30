package com.amvlabs.minigames.details

import android.content.ContentValues.TAG
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.amvlabs.minigames.R
import com.amvlabs.minigames.adapter.DetailRecyclerViewAdapter
import com.amvlabs.minigames.adapter.OnDetailItemClickListener
import com.amvlabs.minigames.database.FireDataListener
import com.amvlabs.minigames.database.FireDatabase
import com.amvlabs.minigames.databinding.ActivityDetailsBinding
import com.amvlabs.minigames.objects.Constant
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Cap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.DocumentSnapshot

class DetailsActivity : AppCompatActivity(), OnMapReadyCallback,FireDataListener,OnDetailItemClickListener{

    private lateinit var map: GoogleMap
    private lateinit var db:FireDatabase
    private lateinit var adapter:DetailRecyclerViewAdapter

    private lateinit var bind:ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(bind.root)
        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        db = FireDatabase()
        db.getInstance(this)
        db.getData(Constant.FIREBASE_COLLECTION,this)




    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        

        val loc: LatLng = LatLng(12.958668, 77.703551)
        val markerPosition = MarkerOptions()
            .position(loc)
            .title("home")

        map.focusedBuilding
        map.addMarker(markerPosition)
//        map.addPolyline(
//            PolylineOptions().add(
//                LatLng(-35.016, 143.321),
//                LatLng(-34.747, 145.592),
//                LatLng(-34.364, 147.891),
//                LatLng(-33.501, 150.217),
//                LatLng(-32.306, 149.248),
//                LatLng(-32.491, 147.309)
//            )
//        )







    }

    override fun onComplete(list: ArrayList<String>) {
        bind.detailRecyclerView.layoutManager = LinearLayoutManager(this)
        bind.detailRecyclerView.adapter = DetailRecyclerViewAdapter(list,this)

    }

    override fun onDownLoadComplete(time: ArrayList<String>, latLng: ArrayList<LatLng>) {
        val option = PolylineOptions()
        latLng.forEach{
            Log.d(TAG, "onDownLoadComplete: ${it.latitude}  ${it.longitude}")
            option.add(it)
        }


        val arr = ArrayList<LatLng>()

//        arr.add(LatLng(-35.016, 143.321))

//        arr.add(LatLng(-34.747, 145.592))
//        arr.add(LatLng(-34.364, 147.891))
//        arr.add(LatLng(-33.501, 150.217))
//        arr.add(LatLng(-32.306, 149.248))
//        arr.add(LatLng(-32.491, 147.309))

//
        map.addPolyline(option)
    }


    override fun onFailure() {
    }

    override fun onItemClicked(position: Int, list: ArrayList<String>) {
        db.downLatLng(Constant.FIREBASE_COLLECTION,list[position],this)
    }
}