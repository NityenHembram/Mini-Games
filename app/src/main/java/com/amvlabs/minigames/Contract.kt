package com.amvlabs.minigames

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

class Contract:ActivityResultContract<Any,Int>() {
    override fun createIntent(context: Context, input: Any?): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${context.packageName}")
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        Log.d(TAG, "parseResult: $resultCode")
        return 111
    }
}