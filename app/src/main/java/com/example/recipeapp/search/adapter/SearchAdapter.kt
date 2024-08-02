package com.example.recipeapp.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.search.viewmodel.SearchViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SearchAdapter(
    private var mealList: MealList, private val viewModel: SearchViewModel,
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private var myListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    inner class SearchViewHolder(private val view: View, listener: OnItemClickListener?) :
        RecyclerView.ViewHolder(view) {
        private var title: TextView? = null
        private var imageView: ImageView? = null
        private var catrgory: TextView? = null
        private var country: TextView? = null
        private var favbtn: ImageButton? = null
        val userId = AuthSharedPref(itemView.context).getUserId()

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }

        private fun getTitle(): TextView {
            return title ?: view.findViewById(R.id.meal_title)
        }

        private fun getCategory(): TextView {
            return catrgory ?: view.findViewById(R.id.meal_category)
        }

        private fun getCountry(): TextView {
            return country ?: view.findViewById(R.id.meal_country)
        }


        private fun getImageView(): ImageView {
            return imageView ?: view.findViewById(R.id.meal_image)
        }

        private fun getFavButton(): ImageButton {
            return favbtn ?: view.findViewById(R.id.imageButton3)
        }


        fun bind(meal: Meal?) {
            getTitle().text = meal?.strMeal
            getCategory().text = meal?.strCategory
            getCountry().text = meal?.strArea
            Glide.with(view.context)
                .load(meal?.strMealThumb)
                .placeholder(R.drawable.baseline_image_24)
                .circleCrop()
                .into(getImageView())

            viewModel.isFavouriteMeal(userId, meal!!.idMeal)
            viewModel.isFavMeal.observe(itemView.context as LifecycleOwner) { isFav ->
                if (isFav) getFavButton().setImageResource(
                    R.drawable.baseline_favorite_24
                )
            }

            getFavButton().setOnClickListener {
                viewModel.isFavMeal.observe(itemView.context as LifecycleOwner) { isFav ->
                    if (isFav) {
                        MaterialAlertDialogBuilder(itemView.context)
                            .setTitle("Remove Meal From Favorites")
                            .setMessage("Are you sure you want to remove this meal from favorites?")
                            .setPositiveButton("Remove") { dialog, _ ->
                                deleteFromFav(userId, meal)
                                getFavButton().setImageResource(R.drawable.baseline_favorite_border_24)
                                dialog.dismiss()
                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    } else {
                        addMealToFav(userId, meal)
                        getFavButton().setImageResource(R.drawable.baseline_favorite_24)

                    }


                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view, myListener)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val meal = mealList.meals[position]
        holder.bind(meal)
    }

    override fun getItemCount() = mealList.meals.size ?: 0

    private fun addMealToFav(userId: Int, meal: Meal) {
        viewModel.insertMeal(meal)
        viewModel.insertIntoFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )
    }

    private fun deleteFromFav(userId: Int, meal: Meal) {
        viewModel.deleteMeal(meal)
        viewModel.deleteFromFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )
    }
}