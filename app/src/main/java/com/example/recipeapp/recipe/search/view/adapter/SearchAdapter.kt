package com.example.recipeapp.recipe.search.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.recipe.common.ChangeFavBtn
import com.example.recipeapp.recipe.common.OnFavBtnClickListener
import com.example.recipeapp.recipe.common.OnMealClickListener
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList

class SearchAdapter(
    private var mealList: MealList,
    private val onMealClickListener: OnMealClickListener,
    private val changeFavBtn: ChangeFavBtn,
    private val onFavBtnClickListener: OnFavBtnClickListener

    ) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        private var title: TextView? = null
        private var imageView: ImageView? = null
        private var catrgory: TextView? = null
        private var country: TextView? = null
        private var favbtn: ImageButton? = null
        val userId = AuthSharedPref(itemView.context).getUserId()

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

        fun getFavButton(): ImageButton {
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

            if (meal != null) {
                changeFavBtn.changeFavBtn(meal,getFavButton())
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val meal = mealList.meals[position]
        holder.bind(meal)

        holder.itemView.setOnClickListener {
            if (meal != null) {
                onMealClickListener.onMealClick(meal)
            }
        }

        holder.getFavButton().setOnClickListener {
            if (meal != null) {
                onFavBtnClickListener.onFavBtnClick(meal, holder.getFavButton())
            }
        }

    }

    override fun getItemCount() = mealList.meals.size

}