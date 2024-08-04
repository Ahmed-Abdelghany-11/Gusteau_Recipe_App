package com.example.recipeapp.favorite.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.common.OnMealClickListener
import com.example.recipeapp.data.remote.dto.Meal

class FavAdapter(
    val mealList: MutableList<Meal>,
    private val onFavBtnClickListener: OnFavBtnClickListener,
    private val onMealClickListener: OnMealClickListener,
) : RecyclerView.Adapter<FavAdapter.FavViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favs_items, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val meal = mealList[position]
        holder.bind(meal)

        holder.btn.setOnClickListener {
            onFavBtnClickListener.onFavBtnClick(meal)
        }

        holder.itemView.setOnClickListener {
            onMealClickListener.onMealClick(meal)
        }
    }

    override fun getItemCount() = mealList.size

    inner class FavViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.meal_name)
        private val image: ImageView = view.findViewById(R.id.image)
        val btn: ImageView = view.findViewById(R.id.heart_button)


        fun bind(meal: Meal) {
            name.text = meal.strMeal
            Glide.with(view.context).load(meal.strMealThumb).into(image)
            btn.setImageResource(R.drawable.baseline_favorite_24)
        }


    }


}
