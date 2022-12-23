package com.walterlauk.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.walterlauk.R

class ImageFullScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full_screen_acitvity)
        var imageView = findViewById<ImageView>(R.id.imageFullView)
        Picasso.get().load(intent.getStringExtra("image")).into(imageView)

    }
}