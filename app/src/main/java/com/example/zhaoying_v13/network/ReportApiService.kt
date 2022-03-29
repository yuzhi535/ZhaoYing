package com.example.zhaoying_v13.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response


import retrofit2.http.Multipart





private const val BASE_URL = "http://110.40.185.43:8000/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface ReportApiService {
    @GET("realestate")
    fun getProperties():
            Deferred<List<Report>>


    @POST("api/user/login")
    @Multipart
    fun userLogin(
        @Part("phone_number")phonenumber:RequestBody,
        @Part("password")password:RequestBody
        ):Call<UserLoginResponse>


    //适用于数据量少的情况
    @POST("api/user/upload/{filename}")
    @Multipart
    fun upLoadFiles(
        //@Part("phonenumber") phonenumber:RequestBody,
        @Part file: MultipartBody.Part?,
        @Path("filename") filename:String
    ): Call<String>?
}

object ReportApi {
    val retrofitService : ReportApiService by lazy {
        retrofit.create(ReportApiService::class.java)
    }
}

