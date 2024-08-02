package com.example.recipeapp.home.favorite.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.home.favorite.adapter.FavAdapter
import com.example.recipeapp.home.favorite.repo.FavRepoImpl
import com.example.recipeapp.home.favorite.viewmodel.FavViewModel
import com.example.recipeapp.home.favorite.viewmodel.FavViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


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

        val userId = authSharedPref.getUserId()
        viewModel.gerUserWithMeals(userId)

        viewModel.userFavMeals.observe(viewLifecycleOwner) { userFavMeals ->
            if (userFavMeals != null) {
                setUpRecyclerView(userFavMeals.meals as MutableList<Meal>, recyclerViewFav)
            }
        }
        removeBySwipe ()
    }

    private fun setUpRecyclerView(data: MutableList<Meal>, recyclerView: RecyclerView) {
        favAdapter = FavAdapter(data, viewModel)
        recyclerView.adapter = favAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        favAdapter.setOnItemClickListener(object: FavAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val meal = viewModel.userFavMeals.value?.meals?.get(position)
                val action = meal?.let {
                    FavouritesFragmentDirections.actionFavouritesFragmentToDetailsFragment(
                        it
                    )
                }
                if (action != null) {
                    findNavController().navigate(action)
                }
            }

        })
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

    private fun removeBySwipe () {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val meal = favAdapter.mealList[position]
                favAdapter.showFunAlertDialog(requireContext(),meal,position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewFav)
    }

}




