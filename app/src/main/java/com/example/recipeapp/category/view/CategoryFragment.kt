package com.example.recipeapp.category.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.category.adapter.CatRecipesAdapter
import com.example.recipeapp.category.repo.CategoryRepoImp
import com.example.recipeapp.category.viewModel.CatViewModelFactory
import com.example.recipeapp.category.viewModel.CategoryViewModel
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.dto.Meal
import androidx.navigation.fragment.navArgs


class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var viewModel: CategoryViewModel
    private lateinit var categoriesAdapter: CatRecipesAdapter
    private lateinit var catRecyclerView: RecyclerView
    private val args: CategoryFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gettingViewModelReady()
        catRecyclerView = view.findViewById(R.id.CategoryRv)
        viewModel.getRecipesOfCategory(args.categoryName)
        viewModel.categoryRecipes.observe(viewLifecycleOwner){ mealList ->
            setUpRecyclerView(mealList.meals as MutableList<Meal>)
        }
    }


    private fun setUpRecyclerView(data: MutableList<Meal>) {
        categoriesAdapter = CatRecipesAdapter(data, viewModel)
        catRecyclerView.adapter = categoriesAdapter
        catRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun gettingViewModelReady() {
        val catViewModelFactory = CatViewModelFactory(
            CategoryRepoImp(
                remoteDataSource = APIClient
            )
        )
        viewModel = ViewModelProvider(this, catViewModelFactory)[CategoryViewModel::class.java]
    }


}