package com.example.recipeapp.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.common.ChangeFavBtn
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
import com.example.recipeapp.home.view.adapter.RandomAdapter
import com.example.recipeapp.home.repo.RetrofitRepoImp
import com.example.recipeapp.home.viewModel.HomeViewModel
import com.example.recipeapp.home.viewModel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment(), OnCategoryClickListener, OnMealClickListener,
    OnFavBtnClickListener, ChangeFavBtn {
    private lateinit var viewModel: HomeViewModel
    private var userId: Int = 0


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
        userId = AuthSharedPref(requireContext()).getUserId()
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

        viewModel.isFavoriteMeal(userId, meal.idMeal).observe(viewLifecycleOwner) { isFav ->
            btn.setImageResource(
                if (isFav) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )


        }
    }

}