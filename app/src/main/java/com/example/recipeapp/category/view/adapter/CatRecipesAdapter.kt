package com.example.recipeapp.category.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.category.viewModel.CategoryViewModel
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CatRecipesAdapter(private val mealList: MutableList<Meal>, val viewmodel: CategoryViewModel) :
    RecyclerView.Adapter<CatRecipesAdapter.CategoryHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favs_items, parent, false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val meal = mealList[position]
        holder.bind(meal)

        val userId = AuthSharedPref(holder.itemView.context).getUserId()
        viewmodel.isFavoriteMeal(userId, meal.idMeal)
            .observe(holder.itemView.context as LifecycleOwner) { isFav ->
                holder.btn.setImageResource(
                    if (isFav == true) R.drawable.baseline_favorite_24
                    else R.drawable.baseline_favorite_border_24
                )
            }

        holder.btn.setOnClickListener {
            viewmodel.isFavoriteMeal(userId, meal.idMeal)
                .observe(holder.itemView.context as LifecycleOwner) { isFav ->
                    if (isFav == true) {
                        holder.showAlertDialog(holder.itemView.context, userId, meal)
                    } else {
                        addMealToFav(userId, meal)
                        holder.btn.setImageResource(R.drawable.baseline_favorite_24)
                    }
                }
        }
    }

    override fun getItemCount() = mealList.size

    inner class CategoryHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.meal_name)
        private val image: ImageView = view.findViewById(R.id.image)
        val btn: ImageView = view.findViewById(R.id.heart_button)


        fun bind(meal: Meal) {
            name.text = meal.strMeal
            Glide.with(view.context).load(meal.strMealThumb).into(image)
        }

        fun showAlertDialog(context: Context, userId: Int, meal: Meal) {
            MaterialAlertDialogBuilder(context)
                .setTitle("Remove Meal From Favorites")
                .setMessage("Are you sure you want to remove this meal from favorites?")
                .setPositiveButton("Remove") { dialog, _ ->
                    deleteFromFav(userId, meal)
                    dialog.dismiss()
                    btn.setImageResource(R.drawable.baseline_favorite_border_24)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun addMealToFav(userId: Int, meal: Meal) {
        viewmodel.insertMeal(meal)
        viewmodel.insertIntoFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )
    }

    private fun deleteFromFav(userId: Int, meal: Meal) {
        viewmodel.deleteMeal(meal)
        viewmodel.deleteFromFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )
    }


}
