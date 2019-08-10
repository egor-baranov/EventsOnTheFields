package com.usualteam.eventsonthefields

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.start_activity.*
import kotlin.random.Random.Default.nextLong

class StartActivity : Activity() {
    // имя, приветствие, запрос разрешений и тд
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
        val id: Long = nextLong(1000000) + 1000000
        this.openFileOutput("id", Context.MODE_PRIVATE).write(id.toString().toByteArray())
        saveBtn.setOnClickListener{

            if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    1)
            }
            else if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    1)
            }
            else {
                this.openFileOutput("name", Context.MODE_PRIVATE).write(editText.text.toString().toByteArray())
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}