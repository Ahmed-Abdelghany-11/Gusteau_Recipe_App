package com.example.recipeapp.search.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.authentication.login.repo.LoginRepoImp
import com.example.recipeapp.authentication.login.viewmodel.LoginViewModel
import com.example.recipeapp.authentication.login.viewmodel.LoginViewModelFactory
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.search.adapter.SearchAdapter
import com.example.recipeapp.search.repo.SearchRepoImp
import com.example.recipeapp.search.viewmodel.SearchViewModel
import com.example.recipeapp.search.viewmodel.SearchViewModelFactory


class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var resultRv: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var noResultText: ImageView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        searchAdapter = SearchAdapter(meals, viewModel)
        resultRv.layoutManager = LinearLayoutManager(requireContext())
        resultRv.adapter = searchAdapter
        searchAdapter.setOnItemClickListener(object: SearchAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val meal = searchViewModel.searchResultOfMeals.value?.meals?.get(position)
                val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(meal?.idMeal ?: "0")
                findNavController().navigate(action)
            }

        })
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

}