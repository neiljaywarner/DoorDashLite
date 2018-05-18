package com.neiljaywarner.doordashlite.ui

import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.neiljaywarner.doordashlite.R
import com.neiljaywarner.doordashlite.model.Restaurant
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_restaurant.*

class RestaurantsAdapter(val prefs: SharedPreferences, var restaurants: List<Restaurant> = emptyList(), private val favoriteListener: (Restaurant) -> Unit) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_restaurant))

    override fun getItemCount() = restaurants.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(prefs, restaurants[position], favoriteListener)

}

class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(prefs: SharedPreferences, restaurant: Restaurant, listener: (Restaurant) -> Unit) = with(restaurant) {
        textViewName.text = name
        imageViewLogo.loadUrl(cover_img_url)
        textViewDescription.text = description
        textViewStatus.text = status
       // starView.setOnClickListener { listener(restaurant) }
        val isFavorite = prefs.contains(restaurant.id.toString() )
        setStarred(imageViewFavorite, isFavorite)


        imageViewFavorite.setOnClickListener {
            //TODO: consider updating in teh acativity and calling notifyItemUpdated
            setStarred(imageViewFavorite, !isFavorite)
            listener(restaurant)
        }
    }
}

fun setStarred(imageView: ImageView, starred: Boolean) = with (imageView) {
    if (starred) {
        setImageResource(android.R.drawable.star_big_on)
    } else {
        setImageResource(android.R.drawable.star_big_off)
    }

}


// TODO: Move to ViewExtensions.kt
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ImageView.loadUrl(url: String) {
    // TODO: Error image and loading image eventually
    Picasso.get().load(url).into(this)
}

