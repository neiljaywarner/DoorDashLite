package com.neiljaywarner.doordashlite;

import android.arch.lifecycle.Observer;

import com.neiljaywarner.doordashlite.model.Restaurant;
import com.neiljaywarner.doordashlite.network.Resource;
import com.neiljaywarner.doordashlite.network.ResourceState;
import com.neiljaywarner.doordashlite.viewmodel.RestaurantListViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.Mockito.mock;


public class RestaurantListViewModelTest {
    @Rule
    public MockitoRule mockitoRule  = MockitoJUnit.rule();

    // TODO: Test ViewModels loading, etc
    //https://medium.com/exploring-android/android-architecture-components-testing-your-viewmodel-livedata-70177af89c6e
    // Test with MockWebServer
    // https://android.jlelse.eu/unit-test-api-calls-with-mockwebserver-d4fab11de847

    @Mock
    //lateinit var observer: Observer<Resource<List<Restaurant>>>
    public Observer<Resource<List<Restaurant>>> observer;
    //private val viewModel : RestaurantListViewModel = RestaurantListViewModel()


    @Test
    public void fetchRestaurantsTriggersLoadingState() {
        RestaurantListViewModel viewModel = mock(RestaurantListViewModel.class);
        viewModel.getRestaurants().observeForever(observer);
        Mockito.when(viewModel.getRestaurantListSingle()).thenReturn(Single.just(getDummyRestaurantList()));

        Mockito.verify(observer).onChanged(new Resource(ResourceState.LOADING, null, null));
    }

    public List<Restaurant> getDummyRestaurantList() {
        ArrayList<Restaurant> list = new ArrayList<>();
        list.add(new Restaurant());
        return list;
    }

}

