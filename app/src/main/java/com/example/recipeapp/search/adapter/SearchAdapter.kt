package com.example.recipeapp.search.adapter

import android.content.Context
import android.util.Log
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
import com.example.recipeapp.common.OnMealClickListener
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.search.viewmodel.SearchViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SearchAdapter(
    private var mealList: MealList,
    private val onMealClickListener: OnMealClickListener,
    private val changeFavBtn: ChangeFavBtn,
    private val onFavBtnClickListener: OnFavBtnClickListener

    ) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    //private var myListener: OnItemClickListener? = null

//    interface OnItemClickListener {
//        fun onItemClick(position: Int)
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        myListener = listener
//    }

    inner class SearchViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        private var title: TextView? = null
        private var imageView: ImageView? = null
        private var catrgory: TextView? = null
        private var country: TextView? = null
        var favbtn: ImageButton? = null
        val userId = AuthSharedPref(itemView.context).getUserId()

//        init {
//            itemView.setOnClickListener {
//                listener?.onItemClick(adapterPosition)
//            }
//        }

        fun getTitle(): TextView {
            return title ?: view.findViewById(R.id.meal_title)
        }

        fun getCategory(): TextView {
            return catrgory ?: view.findViewById(R.id.meal_category)
        }

        fun getCountry(): TextView {
            return country ?: view.findViewById(R.id.meal_country)
        }


        fun getImageView(): ImageView {
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


        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val meal = mealList.meals[position]
        holder.bind(meal)

        val userId = AuthSharedPref(holder.itemView.context).getUserId()

        holder.itemView.setOnClickListener {
            if (meal != null) {
                onMealClickListener.onMealClick(meal)
            }
        }
        holder.favbtn?.setOnClickListener {
            onFavBtnClickListener.onFavBtnClick(meal, holder.favbtn)
        }
//        viewModel.isFavoriteMeal(userId,meal!!.idMeal).observe(holder.itemView.context as LifecycleOwner) { isFav ->
//            Log.d("isfav",isFav.toString())
//            holder.getFavButton().setImageResource(
//                if (isFav == true) R.drawable.baseline_favorite_24
//                else R.drawable.baseline_favorite_border_24
//            )
//        }
//
//        holder.getFavButton().setOnClickListener {
//            viewModel.isFavoriteMeal(userId,meal!!.idMeal).observe(holder.itemView.context as LifecycleOwner) { isFav ->
//                if (isFav == true) {
//                    holder.showAlertDialog(holder.itemView.context, userId, meal)
//                } else {
//                    addMealToFav(userId, meal)
//                    holder.getFavButton().setImageResource(R.drawable.baseline_favorite_24)
//                }
//            }
//        }
    }

    override fun getItemCount() = mealList.meals.size ?: 0






}