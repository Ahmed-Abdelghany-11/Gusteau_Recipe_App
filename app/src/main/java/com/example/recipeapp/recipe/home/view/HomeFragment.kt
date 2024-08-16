package com.example.recipeapp.recipe.home.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.recipe.deleteMealDialog.view.DeleteFavDialogFragment
import com.example.recipeapp.recipe.deleteMealDialog.view.DeleteFavDialogFragmentArgs
import com.example.recipeapp.R
import com.example.recipeapp.recipe.common.ChangeFavBtn
import com.example.recipeapp.recipe.common.CheckInternetViewModel
import com.example.recipeapp.recipe.common.OnDeleteMealListener
import com.example.recipeapp.recipe.common.OnFavBtnClickListener
import com.example.recipeapp.recipe.common.OnMealClickListener
import com.example.recipeapp.data.sharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.recipe.home.view.adapter.RecipeAdapter
import com.example.recipeapp.recipe.home.view.adapter.CategoryAdapter
import com.example.recipeapp.recipe.home.view.adapter.OnCategoryClickListener
import com.example.recipeapp.recipe.home.repo.RetrofitRepoImp
import com.example.recipeapp.recipe.home.viewModel.HomeViewModel
import com.example.recipeapp.recipe.home.viewModel.ViewModelFactory

class HomeFragment : Fragment(R.layout.fragment_home), OnCategoryClickListener, OnMealClickListener,
    OnFavBtnClickListener, ChangeFavBtn, OnDeleteMealListener {
    private lateinit var viewModel: HomeViewModel
    private lateinit var recipesAdapter: RecipeAdapter
private lateinit var btnToUpdate:ImageView

    private val checkInternetViewModel: CheckInternetViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    private lateinit var authSharedPref: AuthSharedPref


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authSharedPref = AuthSharedPref(requireContext())
        gettingViewModelReady()


//        internetViewModel = ViewModelProvider(this)[CheckInternetViewModel::class]

        checkInternetViewModel.isOnline.observe(viewLifecycleOwner) { isOnline ->
            if (isOnline) {
                fetchData(view)
            }
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
            recipesAdapter = RecipeAdapter(meals, this, this, this)
            recyclerViewRecipe.adapter = recipesAdapter
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
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
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
        btnToUpdate=btn
        val userId = authSharedPref.getUserId()
        viewModel.isFavoriteMeal(userId, meal.idMeal).observe(viewLifecycleOwner) { isFav ->
            if (isFav) showAlertDialog(meal)
            else {
                addMealToFav(meal)
                btn.setImageResource(R.drawable.baseline_favorite_24)
            }
        }
    }


    private fun showAlertDialog(meal: Meal) {
        val dialog = DeleteFavDialogFragment()
        val args = DeleteFavDialogFragmentArgs(meal)
        dialog.arguments = args.toBundle()
        dialog.show(childFragmentManager, "DeleteFavDialogFragment")
    }

    override fun confirmDelete(meal: Meal) {
        deleteFromFav(meal)
        btnToUpdate.setImageResource(R.drawable.baseline_favorite_border_24)
    }

    private fun addMealToFav(meal: Meal) {
        viewModel.insertMeal(meal)
        viewModel.insertIntoFav(
            userMealCrossRef = UserMealCrossRef(
                authSharedPref.getUserId(),
                meal.idMeal
            )
        )

    }

    private fun deleteFromFav( meal: Meal) {
        viewModel.deleteMeal(meal)
        viewModel.deleteFromFav(
            userMealCrossRef = UserMealCrossRef(
                authSharedPref.getUserId(),
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