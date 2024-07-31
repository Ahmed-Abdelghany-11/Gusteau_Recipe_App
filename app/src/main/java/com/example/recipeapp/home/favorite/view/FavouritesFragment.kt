package com.example.recipeapp.home.favorite.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuItemWrapperICS
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.model.UserWithMeal
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.RemoteDataSource
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.home.favorite.adapter.FavAdapter
import com.example.recipeapp.home.favorite.repo.FavRepoImpl
import com.example.recipeapp.home.favorite.viewmodel.FavViewModel
import com.example.recipeapp.home.favorite.viewmodel.FavViewModelFactory


class FavouritesFragment : Fragment() {

    private lateinit var viewModel: FavViewModel
    private lateinit var favAdapter: FavAdapter
    private lateinit var recyclerViewFav: RecyclerView
    private lateinit var authSharedPref: AuthSharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gettingViewModelReady()
        recyclerViewFav = view.findViewById(R.id.FavRv)
        authSharedPref = AuthSharedPref(requireContext())

        val userId = 1
        viewModel.gerUserWithMeals(userId)

        viewModel.userFavMeals.observe(viewLifecycleOwner) { userFavMeals ->
            Log.d("FavouritesFragment", "Observed favorite meals: ${userFavMeals}")
            if (userFavMeals != null) {
                setUpRecyclerView(userFavMeals.meals, recyclerViewFav)
            }
        }
    }

    private fun setUpRecyclerView(data: List<Meal>, recyclerView: RecyclerView) {
        favAdapter = FavAdapter(data, viewModel)
        recyclerView.adapter = favAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun gettingViewModelReady() {
        val favFactory = FavViewModelFactory(
            FavRepoImpl(
                localDataSource = LocalDataSourceImpl(requireContext()),
                remoteDataSource = APIClient
            )
        )
        viewModel = ViewModelProvider(this, favFactory)[FavViewModel::class.java]
    }

}




