package com.walterlauk.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.walterlauk.R

class ForgetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        val signInButton = findViewById<TextView>(R.id.sign_in_btn)
        signInButton.setOnClickListener {
            startActivity(Intent(this@ForgetPasswordActivity,LoginActivity::class.java))
            finish()
        }
    }
}