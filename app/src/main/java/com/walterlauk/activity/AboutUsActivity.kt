package com.walterlauk.activity

import android.os.Bundle
import android.view.View
import com.walterlauk.R
import com.walterlauk.api.ApiCall
import com.walterlauk.databinding.ActivityAboutUsBinding
import com.walterlauk.models.ResponseAboutUs
import com.walterlauk.utils.AppPref
import com.walterlauk.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AboutUsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var aboutUsBinding: ActivityAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aboutUsBinding = ActivityAboutUsBinding.inflate(layoutInflater)
        val view = aboutUsBinding.root
        setContentView(view)

        aboutUsBinding.toolbar.imgBack.setOnClickListener(this)
        aboutUsBinding.toolbar.tvHeader.text = getString(R.string.about_us)

        getDataFromAboutAboutUsApi()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> onBackPressed()
        }
    }

    private fun getDataFromAboutAboutUsApi(){

        ApiCall.initApiCall(Constants.BASE_URL).aboutUs("Bearer " +
                AppPref.getValue(AppPref.TOKEN, "").toString()).enqueue(
            object :Callback<ResponseAboutUs>{
                override fun onResponse(
                    call: Call<ResponseAboutUs>,
                    response: Response<ResponseAboutUs>
                ) {
                  if (response.isSuccessful){
                      if (response.code() ==200){
                          println("About Us Response Code "+"200")
                          aboutUsBinding.aboutUsText.text = response.body()!!.data.about_us_text
                      }
                  }
                }
                override fun onFailure(call: Call<ResponseAboutUs>, t: Throwable) {

                }
            }
        )
    }

}