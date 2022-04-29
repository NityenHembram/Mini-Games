package com.amvlabs.minigames.database

import android.content.ContentValues.TAG
import android.util.Log

import com.google.firebase.firestore.FirebaseFirestore

class FireDatabase {

    lateinit var db :FirebaseFirestore

    fun getInstance(){
        db = FirebaseFirestore.getInstance()
    }

    fun setData(col:String,date:String,data:Map<String,String>){
        db.collection(col).document(date).set(data).addOnSuccessListener {
            Log.d(TAG, "onCreate: success")
        }.addOnFailureListener{
            Log.d(TAG, "onCreate: $it")
        }
    }

}