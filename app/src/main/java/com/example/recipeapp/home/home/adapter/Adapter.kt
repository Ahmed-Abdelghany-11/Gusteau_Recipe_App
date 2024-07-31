package com.example.recipeapp.home.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.dto.Meal

class Adapter(private val meal: List<Meal>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    var myListener: OnItemClickListener? = null

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_recipe, parent, false)
        return ViewHolder(view, myListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getTextView().text = meal[position].strMeal
        Glide.with(holder.itemView.context)
            .load(meal[position].strMealThumb)
            .placeholder(R.drawable.baseline_image_24)
            .circleCrop()
            .into(holder.getImageView())
    }

    override fun getItemCount() = meal.size

    class ViewHolder(private var view: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(view) {
        private var textView: TextView?= null
        private var imageView: ImageView?= null

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

    }
}