package com.example.dailyexpensetracker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val placeForPig = findViewById<ImageView>(R.id.placePig)

        Glide.with(this)
            .asGif()
            .load(R.raw.money_pig)
            .into(placeForPig)

        placeForPig.postDelayed(
            { startActivity(Intent(this, MainActivity::class.java)) },
            3000
        )
    }
}
