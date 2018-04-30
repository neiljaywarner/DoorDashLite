package com.neiljaywarner.doordashlite

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.neiljaywarner.doordashlite.model.Restaurant
import com.neiljaywarner.doordashlite.network.Resource
import com.neiljaywarner.doordashlite.network.ResourceState
import com.neiljaywarner.doordashlite.viewmodel.RestaurantListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel : RestaurantListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(RestaurantListViewModel::class.java)
        //TODO: Loading, Empty, Error states with viewModels that can be tested w/o
    }

    override fun onStart() {
        super.onStart()
        viewModel.getRestaurants().observe(this,
                Observer<Resource<List<Restaurant>>> {
                    it?.let {   this.handleDataState(it.status, it.data, it.message) }})
    }

    private fun handleDataState(resourceState: ResourceState, data: List<Restaurant>?,
                                message: String?) {
        when (resourceState) {
            ResourceState.LOADING -> setupScreenForLoadingState()
            ResourceState.SUCCESS -> setupScreenForSuccess(data)
            ResourceState.ERROR -> setupScreenForError(message)
        }
    }

    private fun setupScreenForLoadingState() {
        progress.visibility = View.VISIBLE
        //recycler_browse.visibility = View.GONE
        //view_empty.visibility = View.GONE
        //view_error.visibility = View.GONE
    }

    private fun setupScreenForSuccess(data: List<Restaurant>?) {
        view_error.visibility = View.GONE
        progress.visibility = View.GONE
        if (data!= null && data.isNotEmpty()) {
            textHelloWorld.text = data[0].name
            //updateListView(data)
            //recyclerRestaurants.visibility = View.VISIBLE
        } else {
            view_empty.visibility = View.VISIBLE
        }
    }

    private fun updateListView(restaurants: List<Restaurant>) {
        //adapter.bufferoos = restaurants
        //adapter.notifyDataSetChanged()
    }

    private fun setupScreenForError(message: String?) {
        progress.visibility = View.GONE
        //recyclerResta.visibility = View.GONE
        view_empty.visibility = View.GONE
        view_error.visibility = View.VISIBLE
    }
}
