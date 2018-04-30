package com.neiljaywarner.doordashlite.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.neiljaywarner.doordashlite.model.Restaurant
import com.neiljaywarner.doordashlite.network.Resource
import com.neiljaywarner.doordashlite.network.ResourceState
import com.neiljaywarner.doordashlite.network.ServiceGenerator
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RestaurantListViewModel : ViewModel() {

    private val restaurantsLiveData: MutableLiveData<Resource<List<Restaurant>>> = MutableLiveData()

    val compositeDisposable = CompositeDisposable()

    init {
        fetchRestaurants()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun getRestaurants(): LiveData<Resource<List<Restaurant>>> {
        return restaurantsLiveData
    }

    //https://medium.com/@biratkirat/8-rxjava-rxandroid-in-kotlin-e599509753c8
    //TODO: Note: if time was permitting, consider seriously making local repository source of truth
    // TODO: use this for mockwebserver unit tests.
    //https://android.jlelse.eu/unit-test-api-calls-with-mockwebserver-d4fab11de847
    // this guys' very good
    private fun fetchRestaurants() {
        restaurantsLiveData.postValue(Resource(ResourceState.LOADING, null, null))


        // TODO: consider adding some logic so if in airplane mode or not online, don't try to hit the network
        // etc

        // TODO: https://medium.com/@biratkirat/8-rxjava-rxandroid-in-kotlin-e599509753c8
        ServiceGenerator.getDoorDashService().getRestaurants()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<Restaurant>> {
                    override fun onError(throwable: Throwable) {
                        e { "Error in getRestaurants ${throwable.message}"}
                        // TODO: map to friendly error
                        restaurantsLiveData.postValue(Resource(ResourceState.ERROR, null, throwable.localizedMessage))
                    }

                    override fun onSuccess(list: List<Restaurant>) {
                        d { "Loaded ${list.size} restaurants"}
                        restaurantsLiveData.postValue(Resource(ResourceState.SUCCESS, list, null))

                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                        d { "onSubscribe or getRestaurants()"}
                    }
                })

        //TODO: Utilize repository pattern and local cache.

    }


}

/*
Note: I would do more cleanup on production code but ran out of time.
 */