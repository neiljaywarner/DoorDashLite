package com.neiljaywarner.doordashlite.ui


import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import com.neiljaywarner.doordashlite.network.ServiceGenerator
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTestMock {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    lateinit var mockServer : MockWebServer

    @Before @Throws fun setUp() {
        // Initialize mock webserver

        mockServer = MockWebServer()
        // Start the local server
        mockServer.start()
        ServiceGenerator.apiBaseUrl = mockServer.url("/").toString()


    }

    @After
    @Throws fun tearDown() {
        // We're done with tests, shut it down
        mockServer.shutdown()
    }

    @Test
    fun testNetworkError() {

        val mockResponse = MockResponse()
                .setResponseCode(500) // Simulate a 500 HTTP Code


        mockServer.enqueue(mockResponse)

        mActivityTestRule.launchActivity(Intent())


        onView(withText("Error; please close app and try again."))
                .check(matches(isDisplayed()))


    }

}
