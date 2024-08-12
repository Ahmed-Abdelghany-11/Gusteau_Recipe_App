package com.example.recipeapp.recipe.search.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.recipeapp.recipe.deleteMealDialog.view.DeleteFavDialogFragment
import com.example.recipeapp.recipe.deleteMealDialog.view.DeleteFavDialogFragmentArgs
import com.example.recipeapp.R
import com.example.recipeapp.recipe.common.ChangeFavBtn
import com.example.recipeapp.recipe.common.CheckInternetViewModel
import com.example.recipeapp.recipe.common.OnDeleteMealListener
import com.example.recipeapp.recipe.common.OnFavBtnClickListener
import com.example.recipeapp.recipe.common.OnMealClickListener
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.recipe.search.view.adapter.SearchAdapter
import com.example.recipeapp.recipe.search.repo.SearchRepoImp
import com.example.recipeapp.recipe.search.viewmodel.SearchViewModel
import com.example.recipeapp.recipe.search.viewmodel.SearchViewModelFactory


class SearchFragment : Fragment(R.layout.fragment_search), OnMealClickListener,
    OnFavBtnClickListener, ChangeFavBtn, OnDeleteMealListener {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var resultRv: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var noResultText: ImageView
    private lateinit var noInternetAnim: LottieAnimationView
    private lateinit var btnToUpdate: ImageView // Store the button to update

    private var userId: Int = 0
    private var isDataDisplayed = false

    private var isInitialLoad = true

    private val checkInternetViewModel: CheckInternetViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = AuthSharedPref(requireContext()).getUserId()

        resultRv = view.findViewById(R.id.search_rv)
        searchView = view.findViewById(R.id.searchView)
        noResultText = view.findViewById(R.id.no_result)
        noInternetAnim = view.findViewById(R.id.no_internet_anim)


        getViewModelReady()

        // observe result

        checkInternetViewModel.isOnline.observe(viewLifecycleOwner) { isOnline ->
            if (isOnline) {
                noInternetAnim.visibility = View.GONE
                setUpSearchView()
                searchViewModel.searchResultOfMeals.observe(viewLifecycleOwner) { meals ->
                    val meal = meals?.meals
                    if (!meal.isNullOrEmpty()) {
                        resultRv.visibility = View.VISIBLE
                        noResultText.visibility = View.GONE
                        setUpRecyclerView(meals)
                        isDataDisplayed = true
                    } else {
                        resultRv.visibility = View.GONE
                        noResultText.visibility = View.VISIBLE
                        isDataDisplayed = false
                    }
                }
            } else {
                if (!isDataDisplayed) {
                    noInternetAnim.visibility = View.VISIBLE
                }
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

    private fun setUpRecyclerView(meals: MealList = MealList(emptyList())) {
        searchAdapter = SearchAdapter(meals, this, this, this)
        resultRv.layoutManager = LinearLayoutManager(requireContext())
        resultRv.adapter = searchAdapter

    }

    private fun setUpSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                if (!p0.isNullOrBlank()) {
                    if (checkInternetViewModel.isOnline.value == true) {
                        searchViewModel.getSearchResult(p0)
                    } else {
                        resultRv.visibility = View.GONE
                        noResultText.visibility = View.GONE
                        noInternetAnim.visibility = View.VISIBLE
                    }
                } else {
                    resultRv.visibility = View.GONE
                    noResultText.visibility = View.GONE
                }
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (!p0.isNullOrBlank()) {
                    if (checkInternetViewModel.isOnline.value == true) {
                        searchViewModel.getSearchResult(p0)
                    } else {
                        resultRv.visibility = View.GONE
                        noResultText.visibility = View.GONE
                        noInternetAnim.visibility = View.VISIBLE
                    }
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
        btnToUpdate=btn
        searchViewModel.isFavoriteMeal(userId, meal.idMeal).observe(viewLifecycleOwner) { isFav ->
            if (isFav) showAlertDialog(meal)
            else {
                addMealToFav(meal)
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

    fun deleteFromFav(meal: Meal) {
        searchViewModel.deleteMeal(meal)
        searchViewModel.deleteFromFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )
    }

    private fun addMealToFav(meal: Meal) {
        searchViewModel.insertMeal(meal)
        searchViewModel.insertIntoFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )
    }



}