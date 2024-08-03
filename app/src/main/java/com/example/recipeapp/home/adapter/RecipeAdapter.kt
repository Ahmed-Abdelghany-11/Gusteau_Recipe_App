package com.example.recipeapp.home.adapter

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
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.home.viewModel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class RecipeAdapter(private val meals: List<Meal?>?, private val viewModel : HomeViewModel) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {
    var myListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_recipe, parent, false)
        return ViewHolder(view, myListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals?.get(position)
        holder.getTextView().text = meal?.strMeal
        Glide.with(holder.itemView.context)
            .load(meal?.strMealThumb)
            .placeholder(R.drawable.baseline_image_24)
            .circleCrop()
            .into(holder.getImageView())

        val userId = AuthSharedPref(holder.itemView.context).getUserId()

        viewModel.isFavoriteMeal(userId, meal!!.idMeal).observe(holder.itemView.context as LifecycleOwner) { isFavorite ->
            if (isFavorite) {
                holder.getFavButton().setImageResource(R.drawable.baseline_favorite_24)
            } else {
                holder.getFavButton().setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }

        holder.getFavButton().setOnClickListener {
            viewModel.isFavoriteMeal(userId, meal.idMeal).observe(holder.itemView.context as LifecycleOwner) { isFavorite ->
                if (isFavorite == true) {
                    showFunAlertDialog(holder.itemView.context, meal, position)
                    holder.getFavButton().setImageResource(R.drawable.baseline_favorite_border_24)
                } else {
                    addFavMeal(holder.itemView.context, meal)
                    holder.getFavButton().setImageResource(R.drawable.baseline_favorite_24)
                }
            }
        }
    }

    override fun getItemCount() = meals?.size ?: 0

    class ViewHolder(private var view: View, listener: OnItemClickListener?) :
        RecyclerView.ViewHolder(view) {
        private var textView: TextView? = null
        private var imageView: ImageView? = null
        private var favbtn: ImageButton? = null


        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }

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

    private fun addFavMeal(context: Context,meal: Meal){
        val userId= AuthSharedPref(context).getUserId()
        viewModel.insertMeal(meal)
        viewModel.insertIntoFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )
    }

    private fun showFunAlertDialog(context: Context, meal: Meal, position: Int) {
        Log.d("Adapter", "showFunAlertDialog called with meal: ${meal.strMeal} at position: $position")
        MaterialAlertDialogBuilder(context)
            .setTitle("Remove Meal From Favorites")
            .setMessage("Are you sure you want to remove this meal from favorites?")
            .setPositiveButton("Remove") { dialog, _ ->
                removeMeal(context,meal)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                notifyItemChanged(position)
                dialog.dismiss()
            }
            .show()
    }

    private fun removeMeal(context: Context,meal: Meal) {
        val userId= AuthSharedPref(context).getUserId()
        viewModel.deleteMeal(meal)
        viewModel.deleteFromFav(UserMealCrossRef(
            id= userId,
            idMeal = meal.idMeal
        ))
    }

}
