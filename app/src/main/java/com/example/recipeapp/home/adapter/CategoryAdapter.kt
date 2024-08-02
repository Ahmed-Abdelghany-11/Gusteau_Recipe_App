package com.example.recipeapp.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.dto.Category

class CategoryAdapter (private val category: List<Category>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getTextView().text = category[position].strCategory
        Glide.with(holder.itemView.context)
            .load(category[position].strCategoryThumb)
            .placeholder(R.drawable.baseline_image_24)
            .centerCrop().circleCrop()
            .into(holder.getImageView())
    }

    override fun getItemCount() = category.size

    class ViewHolder (private var view: View) : RecyclerView.ViewHolder(view) {
        private var textView: TextView?= null
        private var imageView: ImageView?= null

        fun getTextView(): TextView {
            return textView ?: view.findViewById(R.id.CategoryTextView)
        }

        fun getImageView(): ImageView {
            return imageView ?: view.findViewById(R.id.CategoryImageView)
        }

    }
}