package com.example.recipeapp.home.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.home.home.adapter.Adapter
import com.example.recipeapp.home.home.adapter.CategoryAdapter
import com.example.recipeapp.home.home.repo.RetrofitRepoImp
import com.example.recipeapp.home.home.viewModel.HomeViewModel
import com.example.recipeapp.home.home.viewModel.ViewModelFactory
import kotlin.random.Random

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        gettingViewModelReady()
        //var myMeal: List<Meal>? = null
        //Random Recipe
        val textView = view.findViewById<TextView>(R.id.RandomTextView)
        val imageView = view.findViewById<ImageView>(R.id.RandomImageView)

        viewModel.getMyResponse()
        viewModel.getMyResponse.observe(viewLifecycleOwner) { getMyResponse ->
            val randomMeal = getMyResponse.meals
            //myMeal = randomMeal
            textView.text = randomMeal[0].strMeal
            Glide.with(requireContext())
                .load(randomMeal[0].strMealThumb)
                .placeholder(R.drawable.baseline_image_24)
                .centerCrop()
                .into(imageView)
        }

        //Categories
        val recyclerView2 = view.findViewById<RecyclerView>(R.id.recyclerViewCategories)
        recyclerView2.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.HORIZONTAL, false)

        viewModel.getAllCategories()
        viewModel.getAllCategoriesResponse.observe(viewLifecycleOwner) { getMyResponse ->
            val category = getMyResponse.categories
            recyclerView2.adapter = CategoryAdapter(category)
        }

        //Recipes
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewRecipes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.HORIZONTAL, false)

        viewModel.getMealsByRandomLetter()
        viewModel.getMealsByLetterResponse.observe(viewLifecycleOwner) { getMyResponse ->
            val meal = getMyResponse.meals
            recyclerView.adapter = Adapter(meal)
        }
        imageView.setOnClickListener {

            val meal = viewModel.getMyResponse.value
            if (meal != null) {
                val myMea = meal.meals[0]
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment2(myMea)
                findNavController().navigate(action)
            }


        }

            }





    private fun gettingViewModelReady(){
        val factory = ViewModelFactory(
            repo = RetrofitRepoImp(
                remoteDataSource = APIClient
            )
        )
        viewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)
    }

}