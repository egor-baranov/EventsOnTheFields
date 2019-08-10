package com.usualteam.eventsonthefields

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import java.io.File


class DefaultActivity : Activity() {
    // activity без разметки, чекающая, есть ли вообще идентификатор
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = applicationContext.getFileStreamPath("id")
        val name = applicationContext.getFileStreamPath("name")
        if(id == null || !id.exists() || name == null || !name.exists()) {
            startActivity(Intent(this, StartActivity::class.java))
        }
        startActivity(Intent(this, MainActivity::class.java))
    }
}