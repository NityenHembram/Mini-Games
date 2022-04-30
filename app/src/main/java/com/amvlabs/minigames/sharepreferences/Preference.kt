package com.amvlabs.minigames.sharepreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class Preference(context: Context) {

    var preferences: SharedPreferences = context.getSharedPreferences("myShared", MODE_PRIVATE)

    fun addData(key: String, value: String) {
        preferences.edit().apply {
            this.putString(key, value)
            apply()
        }
    }

    fun getData(key: String):String? {
        if(preferences != null){
            return preferences.getString(key,"")
        }
       return ""
    }


}