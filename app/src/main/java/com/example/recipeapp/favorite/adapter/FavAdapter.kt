package com.example.recipeapp.favorite.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.favorite.viewmodel.FavViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FavAdapter(val mealList : MutableList<Meal>, val viewmodel : FavViewModel) :RecyclerView.Adapter<FavAdapter.FavViewHolder>() {
    var myListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favs_items, parent, false)
        return FavViewHolder(view, myListener)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val meal = mealList[position]
        holder.bind(meal, viewmodel)
    }

    override fun getItemCount() = mealList.size

   inner class FavViewHolder(private val view: View, listener: OnItemClickListener?) :
        RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.meal_name)
        private val image: ImageView = view.findViewById(R.id.image)
        private val btn: ImageView = view.findViewById(R.id.heart_button)

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }

        fun bind(meal: Meal, viewModel: FavViewModel) {
            name.text = meal.strMeal
            Log.d("strmeal", "bind: ${meal.strMeal}")
            Glide.with(view.context).load(meal.strMealThumb).into(image)
            btn.setImageResource(R.drawable.baseline_favorite_24)
            btn.setOnClickListener {
                showFunAlertDialog(itemView.context,meal,adapterPosition)
            }
        }




    }

    fun showFunAlertDialog(context: Context,meal: Meal,position: Int) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Remove Meal From Favorites")
            .setMessage("Are you sure you want to remove this meal from favorites?")
            .setPositiveButton("Remove") { dialog, _ ->
                removeMeal(context,meal,position)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                notifyItemChanged(mealList.indexOf(meal))
                dialog.dismiss()
            }
            .show()
    }

    private fun removeMeal(context: Context,meal: Meal,position: Int) {
        val userId= AuthSharedPref(context).getUserId()
        viewmodel.deleteMeal(meal)
        viewmodel.deleteFromFav(UserMealCrossRef(
            id= userId,
            idMeal = meal.idMeal
        ))

        // delete from adapter
        mealList.removeAt(position)
        notifyItemRemoved(position)

        viewmodel.gerUserWithMeals(userId)

    }


}
