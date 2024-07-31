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
import com.example.recipeapp.data.local.LocalDataSourceImpl
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gettingViewModelReady()
        val recyclerViewFav = view.findViewById<RecyclerView>(R.id.FavRv)
        recyclerViewFav.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        val userId = 0 // Replace with actual user ID
        viewModel.getAllUserFavMeals(userId)

        viewModel.userFavMeals.observe(viewLifecycleOwner) { userFavMeals ->
            Log.d("FavouritesFragment", "Observed favorite meals: $userFavMeals")
            if (userFavMeals != null) {
                addElements(userFavMeals, recyclerViewFav)
            }
        }

        viewModel.getMyResponse.observe(viewLifecycleOwner) { mealList ->
            mealList?.meals?.firstOrNull()?.let { meal ->
                displayMealDetails(meal)
            }
        }
    }

    private fun addElements(data: List<Meal>, recyclerView: RecyclerView) {
        Log.d("Fragment", "Adding elements: $data")
        val mutableCopy = mutableListOf<Meal>().apply {
            addAll(data)
            Log.d("Fragment", "Elements added: ${data}")
        }

        recyclerView.adapter = FavAdapter(mutableCopy, viewModel)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun gettingViewModelReady() {
        val favFactory = FavViewModelFactory(
            FavRepoImpl(
                localDataSource = LocalDataSourceImpl(requireContext()), remoteDataSource = APIClient
            )
        )
        viewModel = ViewModelProvider(this, favFactory)[FavViewModel::class.java]
    }

    private fun displayMealDetails(meal: Meal) {
        // Update the UI with meal details
        val mealNameTextView: TextView = view?.findViewById(R.id.meal_name) ?: return
        val mealImageView: ImageView = view?.findViewById(R.id.image) ?: return

        mealNameTextView.text = meal.strMeal
        Glide.with(requireContext()).load(meal.strMealThumb).into(mealImageView)
    }


}