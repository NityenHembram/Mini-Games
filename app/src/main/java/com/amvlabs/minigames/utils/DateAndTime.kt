package com.amvlabs.minigames.utils

import android.icu.util.Calendar
import android.icu.util.TimeZone
import java.text.SimpleDateFormat

class DateAndTime {
    private lateinit var calendar:Calendar
    private lateinit var dateFormat:SimpleDateFormat
    private fun getDateAndTime():List<String>{
          var date:String = ""
        calendar = Calendar.getInstance(TimeZone.getDefault())
        dateFormat  = SimpleDateFormat("MM-dd-yyyy HH:mm:ss" )
        date = dateFormat.format(calendar.time)
        return date.split(" ")
    }

    fun date():String{ return getDateAndTime()[0] }
    fun time():String{return getDateAndTime()[1]}
}