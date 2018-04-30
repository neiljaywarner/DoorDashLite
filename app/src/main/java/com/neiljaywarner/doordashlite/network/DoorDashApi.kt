package com.neiljaywarner.doordashlite.network

import com.neiljaywarner.doordashlite.model.Restaurant
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

// e.g. https://api.doordash.com/v2/restaurant/?lat=37.422740&lng=-122.139956
interface DoorDashApi {
    @GET("v2/restaurant/?lat=37.422740&lng=-122.139956")
    fun getRestaurants() : Single<List<Restaurant>>
    //TODO: lat/lng as query parameters
}
