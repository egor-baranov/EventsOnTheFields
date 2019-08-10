package com.usualteam.eventsonthefields

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.start_activity.*
import kotlin.random.Random.Default.nextLong

class StartActivity : Activity() {
    // имя, приветствие, запрос разрешений и тд
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
        val id: Long = nextLong(1000000) + 1000000
        this.openFileOutput("id", Context.MODE_PRIVATE).write(id.toString().toByteArray())
        saveBtn.setOnClickListener{
            this.openFileOutput("name", Context.MODE_PRIVATE).write(editText.text.toString().toByteArray())
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}