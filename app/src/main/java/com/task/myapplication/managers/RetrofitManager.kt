package com.task.myapplication.managers

import android.content.Context
import android.content.ContextWrapper
import com.google.gson.GsonBuilder
import com.task.myapplication.models.response.LoginApiResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.task.myapplication.R
import com.task.myapplication.managers.retrofit.AuthApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class RetrofitManager(context: Context) : ContextWrapper(context) {

    companion object {
        private var INSTANCE: RetrofitManager? = null
        private lateinit var defaultRetrofit: Retrofit
        private var userRetrofit: Retrofit? = null

        fun getInstance(context: Context): RetrofitManager {
            if (INSTANCE == null) {
                initManager(context)
            }
            return INSTANCE!!
        }

        private fun initManager(context: Context) {
            INSTANCE = RetrofitManager(context)

            val interceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Accept", "*/*")
                    .header("Content-Type", "application/json")
                    .build()

                chain.proceed(request)
            }

            val httpClient: OkHttpClient.Builder =
                OkHttpClient.Builder().addInterceptor(interceptor)
                   // .addInterceptor(ConnectivityInterceptor(context))
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(200, TimeUnit.SECONDS);

            val gson = GsonBuilder()
                .registerTypeAdapter(
                    LoginApiResponse::class.java,
                    LoginApiResponse.LoginApiDeserializer()
                )
                .create()

            defaultRetrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(httpClient.build())
                .build()

        }
    }

    val NullOnEmptyConverterFactory = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ) = object : Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter =
                retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

            override fun convert(value: ResponseBody) =
                if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
        }
    }

    fun getAuthApi(): AuthApi {
        return defaultRetrofit.create(AuthApi::class.java)
    }

    fun clearAll() {
        INSTANCE = null
        userRetrofit == null
    }

    fun logout() {
        userRetrofit = null
    }

}