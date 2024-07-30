package com.example.recipeapp.home.favorite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.remote.dto.Meal
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

        val userId = 0
            gettingViewModelReady()

        val recyclerView = view.findViewById<RecyclerView>(R.id.FavRv)
        viewModel.getAllUserFavMeals(userId)
        viewModel.userFavMeals.observe(viewLifecycleOwner) { userWithMeals ->
            if (userWithMeals != null) {
                for (item in userWithMeals) {
                    addElements(item.meals, recyclerView)
                }
            }
        }
    }

    private fun addElements(data:List<Meal>, recyclerView: RecyclerView){
        val mutableCopy = mutableListOf<Meal>().apply {
            addAll(data)
        }
        recyclerView.adapter = FavAdapter(mutableCopy, viewModel)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }


    private fun gettingViewModelReady(){
        val favFactory = FavViewModelFactory(
            FavRepoImpl(
            localDataSource = LocalDataSourceImpl(requireContext())
            )
        )
        viewModel = ViewModelProvider(this,favFactory)[FavViewModel::class.java]
    }

}