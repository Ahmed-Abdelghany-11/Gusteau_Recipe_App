package com.example.recipeapp.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.home.adapter.RecipeAdapter
import com.example.recipeapp.home.adapter.CategoryAdapter
import com.example.recipeapp.home.adapter.RandomAdapter
import com.example.recipeapp.home.repo.RetrofitRepoImp
import com.example.recipeapp.home.viewModel.HomeViewModel
import com.example.recipeapp.home.viewModel.ViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var mealId: String? = null
        super.onViewCreated(view, savedInstanceState)
        gettingViewModelReady()

        //Random Recipe
        val recyclerViewRandom = view.findViewById<RecyclerView>(R.id.recyclerViewRandom)
        val processBarMeal: ProgressBar = view.findViewById(R.id.progressBar_random)

        recyclerViewRandom.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getMyResponse()
        viewModel.getMyResponse.observe(viewLifecycleOwner) { getMyResponse ->
            val randomMeal = getMyResponse?.meals
            val randomAdapter = RandomAdapter(randomMeal, viewModel)
            recyclerViewRandom.adapter = randomAdapter
            processBarMeal.visibility = View.GONE

            randomAdapter.setOnItemClickListener(object : RandomAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val meal = viewModel.getMyResponse.value?.meals?.get(position)
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment2(meal!!)
                    findNavController().navigate(action)
                }

            })
        }

        //Categories
        val recyclerViewCategory = view.findViewById<RecyclerView>(R.id.recyclerViewCategories)
        val processBarCategory: ProgressBar = view.findViewById(R.id.progressBar_category)

        recyclerViewCategory.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL, false
        )

        viewModel.getAllCategories()
        viewModel.getAllCategoriesResponse.observe(viewLifecycleOwner) { getMyResponse ->
            val category = getMyResponse.categories
            recyclerViewCategory.adapter = CategoryAdapter(category)
            processBarCategory.visibility = View.GONE
        }

        //Recipes
        val recyclerViewRecipe = view.findViewById<RecyclerView>(R.id.recyclerViewRecipes)
        val progressBarRecipe = view.findViewById<ProgressBar>(R.id.progressBar_recipe)

        recyclerViewRecipe.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL, false
        )

        viewModel.getMealsByRandomLetter()
        viewModel.getMealsByLetterResponse.observe(viewLifecycleOwner) { getMyResponse ->
            val meals = getMyResponse?.meals
            val adapter = RecipeAdapter(meals, viewModel)
            recyclerViewRecipe.adapter = adapter
            progressBarRecipe.visibility = View.GONE

            adapter.setOnItemClickListener(object : RecipeAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val meal = viewModel.getMealsByLetterResponse.value?.meals?.get(position)
                    Log.d("aaaaaaaaaaaaaaaaaaaaa", "onItemClick: ${meal}")
                    val action = meal?.let {
                        HomeFragmentDirections.actionHomeFragmentToDetailsFragment2(
                            it
                        )
                    }
                    if (action != null) {
                        findNavController().navigate(action)
                    }
                }

            })
        }

        /* cardView.setOnClickListener {
             val myMeal = viewModel.getMyResponse.value?.meals?.get(0)
             Log.d("aaaaaaaaaaaaaaaaaaaaa", "onItemClick: ${myMeal}")
             val action = myMeal?.let { it1 ->
                 HomeFragmentDirections.actionHomeFragmentToDetailsFragment2(
                     it1
                 )
             }
             if (action != null) {
                 findNavController().navigate(action)
             }

             val meal = viewModel.getMyResponse.value?.meals
             if (!meal.isNullOrEmpty()) {
                 recyclerViewRecipe.adapter = RecipeAdapter(meal,viewModel)
                 progressBarRecipe.visibility = View.GONE
             }
         }
         Log.d("userId","${AuthSharedPref(requireContext()).getUserId()}")*/

    }

    private fun gettingViewModelReady() {
        val factory = ViewModelFactory(
            repo = RetrofitRepoImp(
                remoteDataSource = APIClient,
                localDataSource = LocalDataSourceImpl(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
    }

}