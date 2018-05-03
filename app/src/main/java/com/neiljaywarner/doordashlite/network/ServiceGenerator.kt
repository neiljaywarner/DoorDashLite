package com.neiljaywarner.doordashlite.network

import com.neiljaywarner.doordashlite.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object ServiceGenerator {
    // e.g. https://api.doordash.com/v2/restaurant/?lat=37.422740&lng=-122.139956
    var apiBaseUrl = "https://api.doordash.com/"

    private val httpClient = OkHttpClient.Builder()

    private val builder by lazy {
        Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }


    fun <S> createService(serviceClass: Class<S>): S {

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }

        val client = httpClient.build()
        val retrofit = builder.client(client).build()
        return retrofit.create(serviceClass)
    }

    fun getDoorDashService() = createService(DoorDashApi::class.java)


}