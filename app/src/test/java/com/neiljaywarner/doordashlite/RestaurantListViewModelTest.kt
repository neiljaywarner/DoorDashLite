package com.neiljaywarner.doordashlite

import com.neiljaywarner.doordashlite.model.Restaurant
import com.neiljaywarner.doordashlite.network.DoorDashApi
import io.reactivex.observers.TestObserver
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class RestaurantListViewModelTest1 {

    lateinit var doorDashApi: DoorDashApi

    @Before
    @Throws fun setUp() {

        val okHttpClient = OkHttpClient.Builder()
                .build()

        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://api.doordash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        doorDashApi = retrofit.create(DoorDashApi::class.java)
    }

    @Test
    fun testLiveApiReturnsResults() {
        val testObserver = TestObserver<List<Restaurant>>()

        doorDashApi.getRestaurants().subscribe(testObserver)
        testObserver.awaitTerminalEvent(20, TimeUnit.SECONDS)
        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.assertNever(emptyList())

    }


}