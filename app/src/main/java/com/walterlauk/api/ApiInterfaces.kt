package com.walterlauk.api

import com.walterlauk.models.*
import com.walterlauk.utils.ResponseNews
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface ApiInterfaces {

    @FormUrlEncoded
    @POST("v1/auth/login")
    fun login(
        @Field("user_name") userName: String,
        @Field("password") password: String,
        @Field("device_type") device_type: String,
        @Field("device_token") device_token: String,
    ): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("v1/logout")
    fun logout(
        @Field("user_id") userId: String,
        @Field("token") token: String
    ): Call<ResponseLogout>

    @FormUrlEncoded
    @POST("v1/get/profile")
    fun getUserProfile(
        @Header("Authorization") auth: String,
        @Field("user_id") userId: String
    ): Call<ResponseGetUserProfile>

    @Multipart
    @POST("v1/edit/profile")
    fun editDriverProfile(
        @Header("Authorization") auth: String,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image_url: MultipartBody.Part?
    ): Call<ResponseLogout>

    // @Part("id") Id: String,
//          @Part("name") name:String,
//          @Part("user_name") username:String,

    @FormUrlEncoded
    @POST("v1/get/news")
    fun getNews(
        @Header("Authorization") auth: String,
        @Field("limit") limit: Int,
        @Field("offset") offset: Int,
    ): Call<ResponseNews>

    @FormUrlEncoded
    @POST("v1/get/trucktrailer")
    fun getTruckTrailerId(
        @Header("Authorization") auth: String,
        @Field("limit") limit: Int,
        @Field("offset") offset: Int,
        @Field("type") type: String,
    ): Call<ResponseTruckTrailerId>

    @FormUrlEncoded
    @POST("v1/edit/trucktrailer/details")
    fun editTruckTrailerDetails(
        @Header("Authorization") auth: String,
        @Field("trailer_no") trailer_no: String,
        @Field("truck_no") truck_no: String,
        @Field("service_date") service_date: String,
        @Field("safety_date") safety_date: String,
    ): Call<SuccessResponse>

    @FormUrlEncoded
    @POST("v1/get/trucktrailer/parts")
    fun getTruckTrailerDetails(@Header("Authorization") auth: String,
                              @Field("trailer_number")trailer_no: String,
                              @Field("truck_number")truck_no: String): Call<ResponseTruckTrailerParts>

    @Multipart
    @POST("v1/departure/store")
    fun storeDeparture(
        @Header("Authorization") auth: String,
        @PartMap map: HashMap<String, RequestBody>,
        @Part images: ArrayList<MultipartBody.Part>?
    ): Call<SuccessResponse>

    @Multipart
    @POST("v1/damage/store")
    fun storeDamage(
        @Header("Authorization") auth: String,
        @PartMap map: HashMap<String, RequestBody>,
        @Part images: ArrayList<MultipartBody.Part>?
    ): Call<SuccessResponse>

    @FormUrlEncoded
    @POST("v1/get/trucktrailer/details")
    fun getTruckTrailerDetailsForDate(
        @Header("Authorization") auth: String,
        @Field("number") number: String
    ): Call<ResponseTruckTrailerId>

    @POST("v1/get/team")
    fun getTeams(
        @Header("Authorization") auth: String
    ): Call<ResponseTeams>

    @Multipart
    @POST("v1/store/chatimgvideo")
    fun saveImage(@Header("Authorization") auth: String,
                  @Part imgvideo: MultipartBody.Part?
                  ):Call<ResponseSavedImage>


    @FormUrlEncoded
    @POST("v1/chatnotification")
    fun sendNotificationToAdmin(
        @Field("sender_email")sender_email:String,
        @Field("username")username:String,
        @Field("message")messgae:String
    ):Call<SendNotificatioToAdminResponse>


    @POST("v1/get/aboutus")
    fun aboutUs(
        @Header("Authorization") auth: String
    ):Call<ResponseAboutUs>


    @FormUrlEncoded
    @POST("v1/get/specificnews")
    fun getSpecificNews(
        @Header("Authorization") auth: String,
        @Field("news_id")newsId:String
    ):Call<ResponseSpecificNews>


}