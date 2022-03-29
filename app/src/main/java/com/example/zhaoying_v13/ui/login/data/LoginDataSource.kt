package com.example.zhaoying_v13.ui.login.data

import android.util.Log
import com.example.zhaoying_v13.database.UserDatabase
import com.example.zhaoying_v13.network.FormdataApi
import com.example.zhaoying_v13.network.ReportApi
import com.example.zhaoying_v13.network.ReportApiService
import com.example.zhaoying_v13.ui.login.data.model.LoggedInUser
import com.squareup.moshi.JsonClass
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import okhttp3.RequestBody




/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(phonenumber: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication 发送网络请求
            val phonenumberBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), phonenumber)
            val passwordBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),password)

//            Log.i("TAGLogin",phonenumberBody.toString())
//            Log.i("TAGLogin",passwordBody.toString())
            ReportApi.retrofitService.userLogin(phonenumberBody,passwordBody)
                .enqueue(object : Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Log.i("TAGLogin","success:"+response.toString())
                        Log.i("TAGLogin","success:"+response.body().toString())
                    }
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.i("TAGLogin",t.message.toString())
                    }
                } )


                //UUID改为返回的userID
                if (phonenumber=="15603781240"&&password=="123456"){
                    val testUser = LoggedInUser(UUID.randomUUID().toString(), 200)
                    return Result.Success(testUser)
                }

            //Default设置
            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), 200)
            return Result.Success(fakeUser)

        } catch (e: Throwable) {
            Log.i("UserLogin",e.message.toString())
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}