package com.example.recipeapp.category.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.category.viewModel.CategoryViewModel
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.favorite.viewmodel.FavViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CatRecipesAdapter(private val mealList : MutableList<Meal>, val viewmodel : CategoryViewModel) :RecyclerView.Adapter<CatRecipesAdapter.CategoryHolder>() {
    private var myListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favs_items, parent, false)
        return CategoryHolder(view, myListener)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val meal = mealList[position]
        holder.bind(meal)
    }

    override fun getItemCount() = mealList.size

    inner class CategoryHolder(private val view: View, listener: OnItemClickListener?) :
        RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.meal_name)
        private val image: ImageView = view.findViewById(R.id.image)
        private val btn: ImageView = view.findViewById(R.id.heart_button)

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }

        fun bind(meal: Meal) {
            name.text = meal.strMeal
            Glide.with(view.context).load(meal.strMealThumb).into(image)
            btn.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }




}
