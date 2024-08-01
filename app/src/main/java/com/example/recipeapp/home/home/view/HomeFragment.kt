package com.example.recipeapp.home.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
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
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.home.home.adapter.Adapter
import com.example.recipeapp.home.home.adapter.CategoryAdapter
import com.example.recipeapp.home.home.repo.RetrofitRepoImp
import com.example.recipeapp.home.home.viewModel.HomeViewModel
import com.example.recipeapp.home.home.viewModel.ViewModelFactory
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory
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
        var mealId: String? = null
        super.onViewCreated(view, savedInstanceState)
        gettingViewModelReady()
        //var myMeal: List<Meal>? = null
        //Random Recipe
        val textView = view.findViewById<TextView>(R.id.RandomTextView)
        val imageView = view.findViewById<ImageView>(R.id.RandomImageView)
        val processBarMeal: ProgressBar = view.findViewById(R.id.progressBar_random)

        viewModel.getMyResponse()
        viewModel.getMyResponse.observe(viewLifecycleOwner) { getMyResponse ->

            //myMeal = randomMeal
            val randomMeal = getMyResponse?.meals
            mealId = randomMeal?.get(0)?.idMeal
            textView.text = randomMeal?.get(0)?.strMeal
            Glide.with(requireContext())
                .load(randomMeal?.get(0)?.strMealThumb)
                //.placeholder(R.drawable.baseline_image_24)
                .centerCrop()
                .into(imageView)

            processBarMeal.visibility = View.GONE
        }

        //Categories
        val recyclerView2 = view.findViewById<RecyclerView>(R.id.recyclerViewCategories)
        val processBarCategory: ProgressBar = view.findViewById(R.id.progressBar_category)

        recyclerView2.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.HORIZONTAL, false)

        viewModel.getAllCategories()
        viewModel.getAllCategoriesResponse.observe(viewLifecycleOwner) { getMyResponse ->
            val category = getMyResponse.categories
            recyclerView2.adapter = CategoryAdapter(category)
            processBarCategory.visibility = View.GONE
        }

        //Recipes
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewRecipes)
        val progressBarRecipe = view.findViewById<ProgressBar>(R.id.progressBar_recipe)

        recyclerView.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.HORIZONTAL, false)

        var adapter: Adapter? = null
        viewModel.getMealsByRandomLetter()
        viewModel.getMealsByLetterResponse.observe(viewLifecycleOwner) { getMyResponse ->
            val meal = getMyResponse?.meals
            adapter = Adapter(meal)

            recyclerView.adapter = adapter
            adapter?.setOnItemClickListener(object : Adapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    val meal = viewModel.getMealsByLetterResponse.value?.meals?.get(position)
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment2(meal?.idMeal ?: "0")
                    findNavController().navigate(action)            }

            })
        }


        imageView.setOnClickListener {
            //fragment
//            val meal = viewModel.getMyResponse.value?.meals
//            if (meal != null) {
//                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment2(meal[0])
//                findNavController().navigate(action)
//            }

                val myMeal = viewModel.getMyResponse.value?.meals?.get(0)
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment2(myMeal?.idMeal ?: "0")
                findNavController().navigate(action)

//            val meal = viewModel.getMyResponse.value?.meals
//            if (!meal.isNullOrEmpty()) {
//                recyclerView.adapter = Adapter(meal)
//                progressBarRecipe.visibility = View.GONE
//            }
        }

//        }

        Log.d("userId","${AuthSharedPref(requireContext()).getUserId()}")

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