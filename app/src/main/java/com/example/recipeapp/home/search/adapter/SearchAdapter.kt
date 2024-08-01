package com.example.recipeapp.home.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.home.search.viewmodel.SearchViewModel

class SearchAdapter(
    private var mealList: MealList, private val viewModel: SearchViewModel
): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private var myListener: OnItemClickListener? = null

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
        private var favbtn: ImageButton?= null

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


        fun bind(meal:Meal?, viewModel: SearchViewModel){
            getTitle().text= meal?.strMeal
            getCategory().text= meal?.strCategory
            getCountry().text= meal?.strArea
            Glide.with(view.context)
                .load(meal?.strMealThumb)
                .placeholder(R.drawable.baseline_image_24)
                .circleCrop()
                .into(getImageView())

            getFavButton().setOnClickListener {
                viewModel.insertMeal(meal!!)
                    viewModel.insertIntoFav(
                        userMealCrossRef = UserMealCrossRef(
                            1,
                            meal.idMeal
                        )
                    )
               getFavButton().setImageResource(R.drawable.baseline_favorite_24)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view, myListener)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
       val meal = mealList.meals[position]
        holder.bind(meal, viewModel)
    }

    override fun getItemCount()= mealList.meals.size ?: 0

}