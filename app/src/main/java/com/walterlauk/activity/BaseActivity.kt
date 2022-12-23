package com.walterlauk.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.walterlauk.databinding.ActivityBaseBinding

open class BaseActivity : AppCompatActivity() {

   private var activityBaseBinding:ActivityBaseBinding? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBaseBinding= ActivityBaseBinding.inflate(layoutInflater)
        val view = activityBaseBinding!!.root
        setContentView(view)
    }


}