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

class SearchAdapter(
    private val meals: List<Meal?>
): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(private val view:View): RecyclerView.ViewHolder(view){
        private var textView: TextView?= null
        private var imageView: ImageView?= null

        private fun getTextView(): TextView {
            return textView ?: view.findViewById(R.id.meal_title)
        }

        private fun getImageView(): ImageView {
            return imageView ?: view.findViewById(R.id.meal_image)
        }

        fun bind(meal:Meal?){
            getTextView().text= meal?.strMeal
            Glide.with(view.context)
                .load(meal?.strMealThumb)
                .placeholder(R.drawable.baseline_image_24)
                .circleCrop()
                .into(getImageView())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
       val meal = meals[position]
        holder.bind(meal)
    }

    override fun getItemCount()= meals.size

}