package com.neiljaywarner.doordashlite

import com.neiljaywarner.doordashlite.model.Restaurant
import com.neiljaywarner.doordashlite.network.DoorDashApi
import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertEquals
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class MockNetworkTests {


        lateinit var mockServer : MockWebServer
        lateinit var doorDashApi: DoorDashApi

        @Before @Throws fun setUp() {
            // Initialize mock webserver
            mockServer = MockWebServer()
            // Start the local server
            mockServer.start()

            // Get an okhttp client
            val okHttpClient = OkHttpClient.Builder()
                    .build()

            // Get an instance of Retrofit
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(mockServer.url("/"))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()

            // Get an instance of blogService
            doorDashApi = retrofit.create(DoorDashApi::class.java)
            // Initialized repository
        }

        @After @Throws fun tearDown() {
            // We're done with tests, shut it down
            mockServer.shutdown()
        }



    @Test
    fun testErrorCondition() {
        val testObserver = TestObserver<List<Restaurant>>()

        // Mock a response with status 200 and sample JSON output
        val mockResponse = MockResponse()
                .setResponseCode(500) // Simulate a 500 HTTP Code

        // Enqueue request
        mockServer.enqueue(mockResponse)
        doorDashApi.getRestaurants().subscribe(testObserver)
        testObserver.awaitTerminalEvent(20, TimeUnit.SECONDS)
        // No values
        testObserver.assertNoValues()
        // One error recorded
        assertEquals(1, testObserver.errorCount())

    }


    //TODO: Clean these up into a better file structure more clearly testing classes not just scenarios, etc
}



