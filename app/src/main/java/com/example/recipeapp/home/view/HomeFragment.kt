package com.example.recipeapp.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.common.ChangeFavBtn
import com.example.recipeapp.common.CheckInternetViewModel
import com.example.recipeapp.common.OnFavBtnClickListener
import com.example.recipeapp.common.OnMealClickListener
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.home.view.adapter.RecipeAdapter
import com.example.recipeapp.home.view.adapter.CategoryAdapter
import com.example.recipeapp.home.view.adapter.OnCategoryClickListener
import com.example.recipeapp.home.repo.RetrofitRepoImp
import com.example.recipeapp.home.viewModel.HomeViewModel
import com.example.recipeapp.home.viewModel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment(), OnCategoryClickListener, OnMealClickListener,
    OnFavBtnClickListener, ChangeFavBtn {
    private lateinit var viewModel: HomeViewModel

    private var isInitialLoad= true

    private val checkInternetViewModel: CheckInternetViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    private lateinit var authSharedPref: AuthSharedPref

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
        authSharedPref = AuthSharedPref(requireContext())
        gettingViewModelReady()


//        internetViewModel = ViewModelProvider(this)[CheckInternetViewModel::class]

        checkInternetViewModel.isOnline.observe(viewLifecycleOwner) { isOnline ->
            if (isOnline) {
                fetchData(view)
               /* if (!isInitialLoad) {
                    Toast.makeText(requireContext(), "Internet restored", Toast.LENGTH_SHORT).show()
                }*/
            } /*else {
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
            }*/
            isInitialLoad = false
        }
    }

    private fun fetchData(view: View) {

        // Random Recipe
        val textView = view.findViewById<TextView>(R.id.RandomTextView)
        val imageView = view.findViewById<ImageView>(R.id.RandomImageView)
        val imageButton = view.findViewById<ImageView>(R.id.randomFav)
        val card = view.findViewById<CardView>(R.id.cardViewRandom)
        val progressBarMeal: ProgressBar = view.findViewById(R.id.progressBar_random)

        viewModel.getMyResponse()
        viewModel.getMyResponse.observe(viewLifecycleOwner) { getMyResponse ->
            val randomMeal = getMyResponse?.meals?.get(0)
            randomMeal?.let { meal ->
                textView.text = meal.strMeal
                Glide.with(requireContext())
                    .load(meal.strMealThumb)
                    .placeholder(R.drawable.baseline_image_24)
                    .centerCrop()
                    .into(imageView)

                changeFavBtn(meal, imageButton)

                imageButton.setOnClickListener {
                    onFavBtnClick(meal, imageButton)
                }

                card.setOnClickListener {
                    onMealClick(meal)
                }

                progressBarMeal.visibility = View.GONE
            }
        }

        //Categories
        val recyclerViewCategory =
            view.findViewById<RecyclerView>(R.id.recyclerViewCategories)
        val processBarCategory: ProgressBar = view.findViewById(R.id.progressBar_category)

        recyclerViewCategory.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL, false
        )

        viewModel.getAllCategories()
        viewModel.getAllCategoriesResponse.observe(viewLifecycleOwner) { getMyResponse ->
            val category = getMyResponse.categories
            recyclerViewCategory.adapter = CategoryAdapter(category, this)
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
            val adapter = RecipeAdapter(meals, this, this, this)
            recyclerViewRecipe.adapter = adapter
            progressBarRecipe.visibility = View.GONE
        }
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

    override fun onClick(categoryName: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToCategoryFragment(categoryName)
        findNavController().navigate(action)
    }

    override fun onMealClick(meal: Meal) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment2(meal)
        findNavController().navigate(action)
    }

    override fun onFavBtnClick(meal: Meal, btn: ImageView) {
        val userId = authSharedPref.getUserId()
        viewModel.isFavoriteMeal(userId, meal.idMeal).observe(viewLifecycleOwner) { isFav ->
            if (isFav) showAlertDialog(userId, meal, btn)
            else {
                addMealToFav(userId, meal)
                btn.setImageResource(R.drawable.baseline_favorite_24)
            }
        }
    }


    private fun showAlertDialog(userId: Int, meal: Meal, btn: ImageView) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Remove Meal From Favorites")
            .setMessage("Are you sure you want to remove this meal from favorites?")
            .setPositiveButton("Remove") { dialog, _ ->
                deleteFromFav(userId, meal)
                dialog.dismiss()
                btn.setImageResource(R.drawable.baseline_favorite_border_24)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun addMealToFav(userId: Int, meal: Meal) {
        viewModel.insertMeal(meal)
        viewModel.insertIntoFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )

    }

    private fun deleteFromFav(userId: Int, meal: Meal) {
        viewModel.deleteMeal(meal)
        viewModel.deleteFromFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )
    }

    override fun changeFavBtn(meal: Meal, btn: ImageView) {

        viewModel.isFavoriteMeal(authSharedPref.getUserId(), meal.idMeal)
            .observe(viewLifecycleOwner) { isFav ->
                btn.setImageResource(
                    if (isFav) R.drawable.baseline_favorite_24
                    else R.drawable.baseline_favorite_border_24
                )


            }
    }

}