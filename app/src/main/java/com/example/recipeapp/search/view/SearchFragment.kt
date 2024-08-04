package com.example.recipeapp.search.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.search.view.adapter.SearchAdapter
import com.example.recipeapp.search.repo.SearchRepoImp
import com.example.recipeapp.search.viewmodel.SearchViewModel
import com.example.recipeapp.search.viewmodel.SearchViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class SearchFragment : Fragment(R.layout.fragment_search), OnMealClickListener,
    OnFavBtnClickListener, ChangeFavBtn {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var resultRv: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var noResultText: ImageView
    private var userId: Int = 0

    private var isInitialLoad= true

    private val checkInternetViewModel: CheckInternetViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = AuthSharedPref(requireContext()).getUserId()

        resultRv = view.findViewById(R.id.search_rv)
        searchView = view.findViewById(R.id.searchView)
        noResultText = view.findViewById(R.id.no_result)

        getViewModelReady()
        setUpSearchView()

        // observe result

        checkInternetViewModel.isOnline.observe(viewLifecycleOwner) { isOnline ->
            if (isOnline) {
                searchViewModel.searchResultOfMeals.observe(viewLifecycleOwner) { meals ->
                    val meal = meals?.meals
                    if (!meal.isNullOrEmpty()) {
                        resultRv.visibility = View.VISIBLE
                        noResultText.visibility = View.GONE
                        setUpRecyclerView(meals)
                    } else {
                        resultRv.visibility = View.GONE
                        noResultText.visibility = View.VISIBLE
                    }
                }
                if (!isInitialLoad) {
                    Toast.makeText(requireContext(), "Internet restored", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
            }
            isInitialLoad = false
        }

    }

    private fun getViewModelReady() {

        val searchViewModelFactory = SearchViewModelFactory(
            searchRepository = SearchRepoImp(
                remoteDataSource = APIClient,
                localDataSource = LocalDataSourceImpl(requireContext())
            )
        )
        searchViewModel =
            ViewModelProvider(this, searchViewModelFactory)[SearchViewModel::class.java]

    }

    private fun setUpRecyclerView(meals: MealList= MealList(emptyList())) {
        searchAdapter = SearchAdapter(meals, this, this, this)
        resultRv.layoutManager = LinearLayoutManager(requireContext())
        resultRv.adapter = searchAdapter

    }

    private fun setUpSearchView() {

        // handle listener to search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                if (!p0.isNullOrBlank()) {
                    searchViewModel.getSearchResult(p0)
                } else {
                    resultRv.visibility = View.GONE
                    noResultText.visibility = View.GONE
                }
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (!p0.isNullOrBlank()) {
                    searchViewModel.getSearchResult(p0)
                }
                return true
            }
        })
    }

    override fun onMealClick(meal: Meal) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(meal)
        findNavController().navigate(action)
    }

    override fun onFavBtnClick(meal: Meal, btn: ImageView) {
        searchViewModel.isFavoriteMeal(userId, meal.idMeal).observe(viewLifecycleOwner) { isFav ->
            if (isFav) showAlertDialog(userId, meal, btn)
            else {
                addMealToFav(userId, meal)
                btn.setImageResource(R.drawable.baseline_favorite_24)
            }
        }
    }

    override fun changeFavBtn(meal: Meal, btn: ImageView) {
        searchViewModel.isFavoriteMeal(userId, meal.idMeal).observe(viewLifecycleOwner) { isFav ->
            btn.setImageResource(
                if (isFav) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )


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
    fun deleteFromFav(userId: Int, meal: Meal) {
        searchViewModel.deleteMeal(meal)
        searchViewModel.deleteFromFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )
    }
    private fun addMealToFav(userId: Int, meal: Meal) {
        searchViewModel.insertMeal(meal)
        searchViewModel.insertIntoFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )
    }

}