package com.example.recipeapp.home.favorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.home.favorite.viewmodel.FavViewModel

class FavAdapter(val meal : List<Meal>,val viewmodel : FavViewModel) :RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favs_items, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.getName().text = meal[position].strMeal
        val image: ImageView = holder.getImage()
        Glide.with(holder.itemView.context).load(meal[position].strMealThumb).into(image)
        holder.getBtn().setImageResource(R.drawable.baseline_favorite_24)
        holder.getBtn().setOnClickListener {
            viewmodel.deleteFromFav(UserMealCrossRef(0,meal[position].idMeal))
        holder.getBtn().setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    override fun getItemCount(): Int {
        return meal.size
    }

     class FavViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
         private val name = null
         private val image = null
         private val btn = null

         fun getName(): TextView {
             return name ?: view.findViewById(R.id.meal_name)
         }

         fun getImage(): ImageView {
             return image ?: view.findViewById(R.id.image)
         }

        fun getBtn(): ImageView {
            return btn ?: view.findViewById(R.id.heart_button)
        }

    }
}