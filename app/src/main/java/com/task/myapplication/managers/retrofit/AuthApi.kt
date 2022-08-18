package com.task.myapplication.managers.retrofit

import com.task.myapplication.models.response.LoginApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @GET("login")
    fun login(@Query("email") email: String,@Query("password") password: String,): Deferred<Response<LoginApiResponse>>

}