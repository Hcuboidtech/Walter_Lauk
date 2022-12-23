package com.walterlauk.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.walterlauk.R
import com.walterlauk.databinding.ActivitySelectLanguageBinding
import com.walterlauk.utils.LocaleHelper


var selectedLanguage = ""

class SelectLanguageActivity : BaseActivity(), View.OnClickListener {
    lateinit var selectLanguageBinding: ActivitySelectLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectLanguageBinding = ActivitySelectLanguageBinding.inflate(layoutInflater)
        val view = selectLanguageBinding.root
        setContentView(view)

        selectLanguageBinding.toolbar.imgBack.setOnClickListener(this)
        selectLanguageBinding.toolbar.tvHeader.setText(getString(R.string.language))
        selectLanguageBinding.btnSubmit.setOnClickListener(this)
        selectLanguageBinding.clGerman.setOnClickListener(this)
        selectLanguageBinding.clEnglish.setOnClickListener(this)
        selectLanguageBinding.clRussian.setOnClickListener(this)
        selectLanguageBinding.clSerbian.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> onBackPressed()
            R.id.cl_german -> {
                selectedLanguage = "de"
                selectLanguageBinding.imgGerman.visibility = VISIBLE
                selectLanguageBinding.imgEnglish.visibility = GONE
                selectLanguageBinding.imgRussian.visibility = GONE
                selectLanguageBinding.imgSerbian.visibility = GONE

                LocaleHelper.setLocale(this, "de")

            }
            R.id.cl_english -> {
                selectedLanguage = "en"
                selectLanguageBinding.imgGerman.visibility = GONE
                selectLanguageBinding.imgEnglish.visibility = VISIBLE
                selectLanguageBinding.imgRussian.visibility = GONE
                selectLanguageBinding.imgSerbian.visibility = GONE
                LocaleHelper.setLocale(this, "en")
            }
            R.id.cl_russian -> {
                selectedLanguage = "ru"
                selectLanguageBinding.imgGerman.visibility = GONE
                selectLanguageBinding.imgEnglish.visibility = GONE
                selectLanguageBinding.imgRussian.visibility = VISIBLE
                selectLanguageBinding.imgSerbian.visibility = GONE
                LocaleHelper.setLocale(this, "ru")
            }
            R.id.cl_serbian -> {
                selectedLanguage = "sr"
                selectLanguageBinding.imgGerman.visibility = GONE
                selectLanguageBinding.imgEnglish.visibility = GONE
                selectLanguageBinding.imgRussian.visibility = GONE
                selectLanguageBinding.imgSerbian.visibility = VISIBLE
                LocaleHelper.setLocale(this, "sr")
            }
            R.id.btn_submit -> {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }
}