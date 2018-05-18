package com.neiljaywarner.doordashlite.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.VERTICAL
import android.view.View
import com.github.ajalt.timberkt.d
import com.neiljaywarner.doordashlite.R
import com.neiljaywarner.doordashlite.model.Restaurant
import com.neiljaywarner.doordashlite.network.Resource
import com.neiljaywarner.doordashlite.network.ResourceState
import com.neiljaywarner.doordashlite.viewmodel.RestaurantListViewModel
import kotlinx.android.synthetic.main.activity_main.*


const val PREFS_FILENAME = "com.doordashlite.prefs"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel : RestaurantListViewModel
    private val restaurantsAdapter by lazy { RestaurantsAdapter(prefs, emptyList()) { onFavoriteClicked(it) } }
    private val prefs by lazy { this.getSharedPreferences(PREFS_FILENAME, 0)}

    private fun onFavoriteClicked(restaurant: Restaurant) {
        d { "user clicked on restaurant: ${restaurant.name}"}
        if (restaurant.id == null) {
            return
        }

        if (isFavorite(restaurant)) {
            removeFavorite(restaurant.id)
        } else {
            saveFavorite(restaurant.id)
        }

    }

    // TODO: Think if we really want to do this
    // when I don't have such a headache
    // We probably don't, but it is unique hashmap.
    // Feel free to lash me with wet noodle for it.
    private fun isFavorite(id : Int) : Boolean = prefs.getBoolean(id.toString(), false)

    private fun isFavorite(restaurant : Restaurant) : Boolean = isFavorite(restaurant.id as Int)

    private fun saveFavorite(id : Int) {
        prefs.edit().putBoolean(id.toString(), true).apply()
    }

    private fun removeFavorite(id : Int) {
        prefs.edit().remove(id.toString()).apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(RestaurantListViewModel::class.java)
        recyclerViewRestaurants.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, VERTICAL, false)
            setHasFixedSize(true)
            adapter = restaurantsAdapter
            addItemDecoration(DividerItemDecoration(this@MainActivity, VERTICAL))
        }
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
        recyclerViewRestaurants.visibility = View.GONE
        view_empty.visibility = View.GONE
        view_error.visibility = View.GONE
    }

    private fun setupScreenForSuccess(list: List<Restaurant>?) {
        view_error.visibility = View.GONE
        progress.visibility = View.GONE
        if (list != null && list.isNotEmpty()) {
            updateListView(list)
            recyclerViewRestaurants.visibility = View.VISIBLE
        } else {
            view_empty.visibility = View.VISIBLE
        }
    }

    private fun updateListView(list: List<Restaurant>) = restaurantsAdapter.apply {
        restaurants = list
        notifyDataSetChanged()
    }

    private fun setupScreenForError(message: String?) {
        progress.visibility = View.GONE
        recyclerViewRestaurants.visibility = View.GONE
        view_empty.visibility = View.GONE
        view_error.visibility = View.VISIBLE
    }
}
