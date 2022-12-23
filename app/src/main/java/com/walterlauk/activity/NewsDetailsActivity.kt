package com.walterlauk.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import com.walterlauk.api.ApiCall
import com.walterlauk.databinding.ActivityNewsDetailsBinding
import com.walterlauk.models.ResponseSpecificNews
import com.walterlauk.utils.AppPref
import com.walterlauk.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsDetailsActivity : AppCompatActivity() {
    var newsDetailsBinding:ActivityNewsDetailsBinding? = null
    var newId:String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newsDetailsBinding  = ActivityNewsDetailsBinding.inflate(layoutInflater)
        val view = newsDetailsBinding!!.root
        setContentView(view)

      newId = intent.getStringExtra("newsID")
         println("news ID 11  ->"+newId)
        if (newId != null){
            println("news ID 12"+ newId)
            callNewsApi()
        }else{
            Picasso.get().load(intent.getStringExtra("image")).into(newsDetailsBinding!!.newsDetailImageview)
            newsDetailsBinding!!.include.imgBack.setOnClickListener { onBackPressed() }
//        newsDetailsBinding.newsTitle.setText(intent.getStringExtra("title"))
            newsDetailsBinding!!.newsDescription.setText(intent.getStringExtra("description"))
            newsDetailsBinding!!.include.circleImageView.visibility =View.INVISIBLE
            newsDetailsBinding!!.include.tvHeader.setText(intent.getStringExtra("title"))

        }

       newsDetailsBinding!!.include.imgBack.setOnClickListener {
           onBackPressed()
       }
    }

    private fun callNewsApi() {
        ApiCall.initApiCall(Constants.BASE_URL)
            .getSpecificNews("Bearer" + AppPref.getValue(AppPref.TOKEN, "")!!, newId!!)
            .enqueue(object :Callback<ResponseSpecificNews>{
                override fun onResponse(
                    call: Call<ResponseSpecificNews>,
                    response: Response<ResponseSpecificNews>)
                {
                  if(response.isSuccessful)
                  {
                      if (response.code() == 200){
                          println("Specific news response is 200")
                          newsDetailsBinding?.newsDescription?.text = response.body()?.data?.news_description
                          Picasso.get().load(response.body()?.data?.news_img).into(newsDetailsBinding!!.newsDetailImageview)
                            newsDetailsBinding!!.include.tvHeader.text =
                                response.body()?.data?.news_title
                      }
                  }
                }
                override fun onFailure(call: Call<ResponseSpecificNews>, t: Throwable) {
                   println("Specific new response failure "+ t.message)
                }
            })
    }


}