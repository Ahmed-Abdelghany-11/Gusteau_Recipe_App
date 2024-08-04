package com.example.recipeapp.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.common.ChangeFavBtn
import com.example.recipeapp.common.OnFavBtnClickListener
import com.example.recipeapp.common.OnMealClickListener
import com.example.recipeapp.data.remote.dto.Meal


class RecipeAdapter(private val meals: List<Meal?>?,
                    private val onMealClickListener: OnMealClickListener,
                    private val onFavBtnClickListener: OnFavBtnClickListener,
                    private val changeFavBtn: ChangeFavBtn
)
    : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals?.get(position)
        holder.getTextView().text = meal?.strMeal
        Glide.with(holder.itemView.context)
            .load(meal?.strMealThumb)
            .placeholder(R.drawable.baseline_image_24)
            .circleCrop()
            .into(holder.getImageView())

        if (meal != null) {
            changeFavBtn.changeFavBtn(meal,holder.getFavButton())
        }

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

    override fun getItemCount() = meals?.size ?: 0

    class ViewHolder(private var view: View) :
        RecyclerView.ViewHolder(view) {
        private var textView: TextView? = null
        private var imageView: ImageView? = null
        private var favbtn: ImageButton? = null

        fun getTextView(): TextView {
            return textView ?: view.findViewById(R.id.textRecipe)
        }

        fun getImageView(): ImageView {
            return imageView ?: view.findViewById(R.id.imageRecipe)
        }

        fun getFavButton(): ImageButton {
            return favbtn ?: view.findViewById(R.id.fav)
        }

    }


}
