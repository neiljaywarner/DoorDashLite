package com.neiljaywarner.doordashlite.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.neiljaywarner.doordashlite.model.Restaurant
import com.neiljaywarner.doordashlite.model.getDummyRestaurant
import com.neiljaywarner.doordashlite.network.Resource
import com.neiljaywarner.doordashlite.network.ResourceState
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toSingle
import io.reactivex.subscribers.DisposableSubscriber
import timber.log.Timber

class RestaurantListViewModel : ViewModel() {

        private val restaurantsLiveData: MutableLiveData<Resource<List<Restaurant>>> = MutableLiveData()

        lateinit var disposable: Disposable
        init {
            fetchRestaurants()
        }

        override fun onCleared() {
            disposable.dispose()
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
        fun fetchRestaurants() {
            restaurantsLiveData.postValue(Resource(ResourceState.LOADING, null, null))

            /*
            disposable = Observable.create(ObservableOnSubscribe<String>
            { emitter -> emitter.onNext(fetchFromServer()) })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{ result ->  displayResult(result) }
            */
            // TODO: https://medium.com/@biratkirat/8-rxjava-rxandroid-in-kotlin-e599509753c8
            listOf(getDummyRestaurant()).toSingle().subscribe(object : SingleObserver<List<Restaurant>> {
                override fun onError(e: Throwable) {
                    Timber.e(e)
                    // TODO: map to friendly error
                    restaurantsLiveData.postValue(Resource(ResourceState.SUCCESS, null, e.localizedMessage))
                }

                override fun onSuccess(list: List<Restaurant>) {
                    restaurantsLiveData.postValue(Resource(ResourceState.SUCCESS, list, null))

                }

                override fun onSubscribe(d: Disposable) {
                    Timber.d("OnSubscribe for getRestaurants")
                }
            })
            /*
            TODO: from retrofit callable
             */
            /*
            val tvShowSingle = Single.fromCallable(object : Callable<List<String>>() {

                @Throws(Exception::class)
                fun call(): List<String> {
                    return mRestClient.getFavoriteTvShows()
                }
            })
            */
            //todo: retrofit



        }


}

/*
Note: I would do more cleanup on production code but ran out of time.
 */