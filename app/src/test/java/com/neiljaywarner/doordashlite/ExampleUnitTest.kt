package com.neiljaywarner.doordashlite

import android.arch.lifecycle.Observer
import com.neiljaywarner.doordashlite.model.Restaurant
import com.neiljaywarner.doordashlite.network.Resource
import com.neiljaywarner.doordashlite.network.ResourceState
import com.neiljaywarner.doordashlite.viewmodel.RestaurantListViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Rule
    val  mockitoRule : MockitoRule = MockitoJUnit.rule();

    // TODO: Test ViewModels loading, etc
    //https://medium.com/exploring-android/android-architecture-components-testing-your-viewmodel-livedata-70177af89c6e
    // Test with MockWebServer
    // https://android.jlelse.eu/unit-test-api-calls-with-mockwebserver-d4fab11de847

    @Mock
    lateinit var observer: Observer<Resource<List<Restaurant>>>
    private val viewModel : RestaurantListViewModel = RestaurantListViewModel()

    @Test
    fun fetchRestaurantsTriggersLoadingState() {
        viewModel.getRestaurants().observeForever(observer)
        viewModel.fetchRestaurants()

        Mockito.verify(observer).onChanged(Resource(ResourceState.LOADING, null, null))
    }
}
