package com.amvlabs.minigames.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.amvlabs.minigames.R
import com.amvlabs.minigames.databinding.ActivityMainSettingsBinding
import com.amvlabs.minigames.details.DetailsActivity

class MainSettings : AppCompatActivity(),View.OnClickListener {

    private lateinit var bind:ActivityMainSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainSettingsBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.detailsOpt.setOnClickListener(this)
        bind.gameSettingOpt.setOnClickListener(this)
        bind.resetGameOpt.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v?.id){
            bind.gameSettingOpt.id ->{}
            bind.resetGameOpt.id ->{}
            bind.detailsOpt.id -> {startActivity(Intent(this,DetailsActivity::class.java))}
        }
    }
}