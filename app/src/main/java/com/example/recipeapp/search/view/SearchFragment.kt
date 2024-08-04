package com.example.recipeapp.search.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.authentication.login.repo.LoginRepoImp
import com.example.recipeapp.authentication.login.viewmodel.LoginViewModel
import com.example.recipeapp.authentication.login.viewmodel.LoginViewModelFactory
import com.example.recipeapp.category.view.CategoryFragmentDirections
import com.example.recipeapp.common.OnMealClickListener
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.search.adapter.ChangeFavBtn
import com.example.recipeapp.search.adapter.OnFavBtnClickListener
import com.example.recipeapp.search.adapter.SearchAdapter
import com.example.recipeapp.search.repo.SearchRepoImp
import com.example.recipeapp.search.viewmodel.SearchViewModel
import com.example.recipeapp.search.viewmodel.SearchViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class SearchFragment : Fragment(R.layout.fragment_search), OnMealClickListener,
OnFavBtnClickListener, ChangeFavBtn{

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var resultRv: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var noResultText: ImageView
    private var userId: Int = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = AuthSharedPref(requireContext()).getUserId()

        resultRv = view.findViewById(R.id.search_rv)
        searchView = view.findViewById(R.id.searchView)
        noResultText = view.findViewById(R.id.no_result)

        getViewModelReady()
        setUpSearchView()
        setUpSearchView()


        // observe result


        searchViewModel.searchResultOfMeals.observe(viewLifecycleOwner) { meals ->
            val meal = meals?.meals
            if (!meal.isNullOrEmpty()) {
                resultRv.visibility = View.VISIBLE
                noResultText.visibility = View.GONE
                setUpRecyclerView(meals, searchViewModel)
            } else {
                resultRv.visibility = View.GONE
                noResultText.visibility = View.VISIBLE
            }
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

    private fun setUpRecyclerView(meals: MealList= MealList(emptyList()), viewModel: SearchViewModel) {
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

    override fun onFavBtnClick(meal: Meal?, btn: ImageView?) {
        searchViewModel.isFavoriteMeal(userId,meal!!.idMeal).observe(viewLifecycleOwner) { isFav ->
            if (btn != null) {
                if (isFav) showAlertDialog(userId, meal, btn)
                else {
                    addMealToFav(userId, meal)
                    btn.setImageResource(R.drawable.baseline_favorite_24)
                }
            }
        }
    }

    override fun changeFavBtn(meal: Meal, btn: ImageView) {
        TODO("Not yet implemented")
    }


    fun showAlertDialog(userId: Int, meal: Meal, btn: ImageView) {
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