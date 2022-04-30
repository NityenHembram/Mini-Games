package com.amvlabs.minigames.database

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.amvlabs.minigames.sharepreferences.Preference
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class FireDatabase {

    private lateinit var db: FirebaseFirestore
    private val timeList = ArrayList<String>()
    private val latLng = ArrayList<LatLng>()
    private lateinit var preference:Preference

    fun getInstance(context:Context) {
        db = FirebaseFirestore.getInstance()
        preference = Preference(context)

    }

    fun setData(col: String, date: String, data: Map<String, String>) {
        db.collection(col).document(date).set(data).addOnSuccessListener {
            Log.d(TAG, "onCreate: success")
        }.addOnFailureListener {
            Log.d(TAG, "onCreate: $it")
        }
    }

    fun getData(col: String, listener: FireDataListener) {
        val list = ArrayList<String>()
        db.collection(col).get().addOnSuccessListener {
            it?.documents?.map { o ->
                list.add(o.id)
            }
            listener.onComplete(list)
        }
    }

    fun downLatLng(col: String, doc: String, listener: FireDataListener) {
        db.collection(col).document(doc).get().addOnSuccessListener {
            val map = it.data

            GlobalScope.launch {
                val asy = async {
                    map?.forEach { k, v ->
                        timeList.add(k)
                        latLng.add(convertToLatLang(v))
                    }
                }
                asy.await()
                withContext(Dispatchers.Main){
                    listener.onDownLoadComplete(timeList, latLng)
                }

            }


        }.addOnFailureListener {
            Log.d(TAG, "downLatLng: $it")
        }
    }

    private fun convertToLatLang(v: Any?):LatLng {
        val list: String = v as String
        val latlngArr = v.split(" ")
        return LatLng(latlngArr[0].toDouble(),latlngArr[1].toDouble())
    }
}


interface FireDataListener {
    fun onComplete(list: ArrayList<String>)
    fun onDownLoadComplete(time: ArrayList<String>, latLng: ArrayList<LatLng>)
    fun onFailure()
}
