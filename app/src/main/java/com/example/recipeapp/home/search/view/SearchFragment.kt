package com.example.recipeapp.home.search.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
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
import com.example.recipeapp.home.search.adapter.SearchAdapter
import com.example.recipeapp.home.search.repo.SearchRepoImp
import com.example.recipeapp.home.search.viewmodel.SearchViewModel
import com.example.recipeapp.home.search.viewmodel.SearchViewModelFactory


class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var resultRv: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var searchAdapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultRv = view.findViewById(R.id.search_rv)
        searchView = view.findViewById(R.id.searchView)

        getViewModelReady()
        setUpSearchView()
        setUpSearchView()


        // observe result
        searchViewModel.searchResultOfMeals.observe(viewLifecycleOwner) { meals ->
            if (meals != null)
                setUpRecyclerView(meals)
        }
    }

    private fun getViewModelReady() {

        val searchViewModelFactory = SearchViewModelFactory(
            searchRepository = SearchRepoImp(
                remoteDataSource = APIClient
            )
        )
        searchViewModel =
            ViewModelProvider(this, searchViewModelFactory)[SearchViewModel::class.java]

    }

    private fun setUpRecyclerView(meals: MealList= MealList(emptyList())) {
        searchAdapter = SearchAdapter(meals)
        resultRv.layoutManager = LinearLayoutManager(requireContext())
        resultRv.adapter = searchAdapter
    }

    private fun setUpSearchView() {

        // handle listener to search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                if (!p0.isNullOrBlank()) {
                    searchViewModel.getSearchResult(p0)
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