package com.walterlauk.api

import com.google.gson.GsonBuilder
import com.walterlauk.BuildConfig
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ApiCall {
    companion object {

        // var callBackInterface: CallBackInterface? = null
        //private var apiCall: ApiCall? = null

        /*fun getInstance(callBackInterface: CallBackInterface?): ApiCall {
            Companion.callBackInterface = callBackInterface
            if (apiCall == null) {
                apiCall =
                    ApiCall()
            }
            return apiCall!!
        }*/

        fun initApiCall(baseUrl: String): ApiInterfaces {

            val dispatcher = Dispatcher()
            dispatcher.maxRequests = 1
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                okHttpClient.addInterceptor(interceptor)
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(ApiInterfaces::class.java)
        }
    }

    /*fun <T> callApi(t: Call<T>, resulCode: Int) {
        t.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                try {
                    Log.e("Response", response?.body().toString())
                    callBackInterface!!.handleResponse(response?.body(), resulCode)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callBackInterface!!.handleError(Throwable(), resulCode)
                }
            }

            override fun onFailure(call: Call<T>?, t: Throwable?) {
                try {
                    callBackInterface!!.handleError(t!!, resulCode)
                } catch (e: Exception) {
                }
            }
        })
    }*/
}