package com.example.recipeapp.recipe.category.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.recipe.common.ChangeFavBtn
import com.example.recipeapp.recipe.common.OnFavBtnClickListener
import com.example.recipeapp.recipe.common.OnMealClickListener
import com.example.recipeapp.data.remote.dto.Meal

class CatRecipesAdapter(
    private val mealList: MutableList<Meal>,
    private val onMealClickListener: OnMealClickListener,
    private val onFavBtnClickListener: OnFavBtnClickListener,
    private val changeFavBtn: ChangeFavBtn,
) :
    RecyclerView.Adapter<CatRecipesAdapter.CategoryHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favs_items, parent, false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val meal = mealList[position]
        holder.bind(meal)

        holder.itemView.setOnClickListener {
            onMealClickListener.onMealClick(meal)
        }

        holder.btn.setOnClickListener {
            onFavBtnClickListener.onFavBtnClick(meal, holder.btn)
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

            changeFavBtn.changeFavBtn(meal,btn)

        }


    }
}
