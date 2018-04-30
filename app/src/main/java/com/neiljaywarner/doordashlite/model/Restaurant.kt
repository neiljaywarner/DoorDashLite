package com.neiljaywarner.doordashlite.model

data class Restaurant(val name: String = "")

// see https://api.doordash.com/v2/restaurant/?lat=37.422740&lng=-122.139956
// use GSON, etc

fun getDummyRestaurant() = Restaurant("testName")