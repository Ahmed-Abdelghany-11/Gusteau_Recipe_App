package com.example.recipeapp.home.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList

class SearchAdapter(
    private var mealList: MealList
): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    var myListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    class SearchViewHolder(private val view:View, listener: OnItemClickListener?): RecyclerView.ViewHolder(view){
        private var title: TextView?= null
        private var imageView: ImageView?= null
        private var catrgory: TextView?= null
        private var country: TextView?= null

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

        fun bind(meal:Meal?){
            getTitle().text= meal?.strMeal
            getCategory().text= meal?.strCategory
            getCountry().text= meal?.strArea
            Glide.with(view.context)
                .load(meal?.strMealThumb)
                .placeholder(R.drawable.baseline_image_24)
                .circleCrop()
                .into(getImageView())
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

    override fun getItemCount()= mealList.meals.size ?: 0

}