package com.example.recipeapp.favorite.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.example.recipeapp.favorite.view.adapter.FavAdapter
import com.example.recipeapp.favorite.repo.FavRepoImpl
import com.example.recipeapp.favorite.view.adapter.OnFavBtnClickListener
import com.example.recipeapp.favorite.view.adapter.OnFavMealClickListener
import com.example.recipeapp.favorite.viewmodel.FavViewModel
import com.example.recipeapp.favorite.viewmodel.FavViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class FavouritesFragment : Fragment(), OnFavBtnClickListener, OnFavMealClickListener {

    private lateinit var viewModel: FavViewModel
    private lateinit var favAdapter: FavAdapter
    private lateinit var recyclerViewFav: RecyclerView
    private lateinit var authSharedPref: AuthSharedPref
    private lateinit var noFav: ImageView

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
        recyclerViewFav = view.findViewById(R.id.CategoryRv)
        authSharedPref = AuthSharedPref(requireContext())
        noFav = view.findViewById(R.id.no_fav)

        val userId = authSharedPref.getUserId()
        viewModel.gerUserWithMeals(userId)

        viewModel.userFavMeals.observe(viewLifecycleOwner) { userFavMeals ->
            Log.d("FavouritesFragment", "Observed favorite meals: ${userFavMeals}")
            if (userFavMeals != null && !userFavMeals.meals.isNullOrEmpty()) {
                recyclerViewFav.visibility = View.VISIBLE
                noFav.visibility = View.GONE
                setUpRecyclerView(userFavMeals.meals as MutableList<Meal>, recyclerViewFav)
            } else {
                recyclerViewFav.visibility = View.GONE
                noFav.visibility = View.VISIBLE

            }
        }
        removeBySwipe()
    }

    private fun setUpRecyclerView(data: MutableList<Meal>, recyclerView: RecyclerView) {
        favAdapter = FavAdapter(data, this, this)
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

    private fun removeBySwipe() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val meal = favAdapter.mealList[position]
                showFunAlertDialog(requireContext(), meal, position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewFav)
    }


    private fun showFunAlertDialog(context: Context, meal: Meal, position: Int) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Remove Meal From Favorites")
            .setMessage("Are you sure you want to remove this meal from favorites?")
            .setPositiveButton("Remove") { dialog, _ ->
                removeMeal(meal, position)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                favAdapter.notifyItemChanged(favAdapter.mealList.indexOf(meal))
                dialog.dismiss()
            }
            .show()
    }

    private fun removeMeal(meal: Meal, position: Int) {
        val userId = authSharedPref.getUserId()
        viewModel.deleteMeal(meal)
        viewModel.deleteFromFav(
            UserMealCrossRef(
                id = userId,
                idMeal = meal.idMeal
            )
        )
        // delete from adapter
        favAdapter.mealList.removeAt(position)
        favAdapter.notifyItemRemoved(position)

        viewModel.gerUserWithMeals(userId)

    }

    override fun onMealClick(meal: Meal) {
        val action = FavouritesFragmentDirections.actionFavouritesFragmentToDetailsFragment(meal)
        findNavController().navigate(action)
    }

    override fun onFavBtnClick(meal: Meal) {
        val position = favAdapter.mealList.indexOf(meal)
        showFunAlertDialog(requireContext(), meal, position)
    }


}




