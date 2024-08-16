package com.example.recipeapp.recipe.category.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.recipe.category.view.adapter.CatRecipesAdapter
import com.example.recipeapp.recipe.category.repo.CategoryRepoImp
import com.example.recipeapp.recipe.category.viewModel.CatViewModelFactory
import com.example.recipeapp.recipe.category.viewModel.CategoryViewModel
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.dto.Meal
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.example.recipeapp.recipe.deleteMealDialog.view.DeleteFavDialogFragment
import com.example.recipeapp.recipe.deleteMealDialog.view.DeleteFavDialogFragmentArgs
import com.example.recipeapp.recipe.common.ChangeFavBtn
import com.example.recipeapp.recipe.common.CheckInternetViewModel
import com.example.recipeapp.recipe.common.OnDeleteMealListener
import com.example.recipeapp.recipe.common.OnFavBtnClickListener
import com.example.recipeapp.recipe.common.OnMealClickListener
import com.example.recipeapp.data.sharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.model.UserMealCrossRef


class CategoryFragment : Fragment(R.layout.fragment_category), OnMealClickListener,
    OnFavBtnClickListener, ChangeFavBtn, OnDeleteMealListener {

    private lateinit var viewModel: CategoryViewModel
    private lateinit var categoriesAdapter: CatRecipesAdapter
    private lateinit var catRecyclerView: RecyclerView
    private val args: CategoryFragmentArgs by navArgs()
    private lateinit var authSharedPref: AuthSharedPref
    private lateinit var noInternetAnim: LottieAnimationView
    private lateinit var btnToUpdate: ImageView
    private var userId = 0


    private var isInitialLoad = true

    private val checkInternetViewModel: CheckInternetViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authSharedPref = AuthSharedPref(requireContext())
        noInternetAnim = view.findViewById(R.id.no_internet_anim)
        catRecyclerView= view.findViewById(R.id.CategoryRv)
        val categoryNameTextView: TextView = view.findViewById(R.id.categoryName)
        categoryNameTextView.text = args.categoryName
        catRecyclerView = view.findViewById(R.id.CategoryRv)
        userId = AuthSharedPref(requireContext()).getUserId()


        gettingViewModelReady()
        checkInternetViewModel.isOnline.observe(viewLifecycleOwner) { isOnline ->
            if (isOnline) {
                hideNoInternetAnim()
                catRecyclerView = view.findViewById(R.id.CategoryRv)
                viewModel.getRecipesOfCategory(args.categoryName)
                viewModel.categoryRecipes.observe(viewLifecycleOwner) { mealList ->
                    setUpRecyclerView(mealList.meals as MutableList<Meal>)
                }
            } else if (isInitialLoad) {
                showNoInternetAnim()
            }
            isInitialLoad = false
        }


    }

    private fun showNoInternetAnim() {
        catRecyclerView.visibility = View.GONE
        noInternetAnim.visibility = View.VISIBLE
        noInternetAnim.playAnimation()
    }

    private fun hideNoInternetAnim() {
        catRecyclerView.visibility = View.VISIBLE
        noInternetAnim.cancelAnimation()
        noInternetAnim.visibility = View.GONE
    }


    private fun setUpRecyclerView(data: MutableList<Meal>) {
        categoriesAdapter = CatRecipesAdapter(data, this, this, this)
        catRecyclerView.adapter = categoriesAdapter
        catRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun gettingViewModelReady() {
        val catViewModelFactory = CatViewModelFactory(
            CategoryRepoImp(
                remoteDataSource = APIClient,
                localDataSource = LocalDataSourceImpl(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, catViewModelFactory)[CategoryViewModel::class.java]
    }

    override fun onMealClick(meal: Meal) {
        val action = CategoryFragmentDirections.actionCategoryFragmentToDetailsFragment(meal)
        findNavController().navigate(action)
    }

    override fun onFavBtnClick(meal: Meal, btn: ImageView) {
        btnToUpdate = btn
        viewModel.isFavoriteMeal(userId, meal.idMeal).observe(viewLifecycleOwner) { isFav ->
            if (isFav) showAlertDialog(meal)
            else {
                addMealToFav(meal)
                btn.setImageResource(R.drawable.baseline_favorite_24)
            }
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


    private fun addMealToFav(meal: Meal) {
        viewModel.insertMeal(meal)
        viewModel.insertIntoFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )

    }

    private fun deleteFromFav(meal: Meal) {
        viewModel.deleteMeal(meal)
        viewModel.deleteFromFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )
    }

    override fun changeFavBtn(meal: Meal, btn: ImageView) {

        viewModel.isFavoriteMeal(authSharedPref.getUserId(), meal.idMeal)
            .observe(viewLifecycleOwner) { isFav ->
                btn.setImageResource(
                    if (isFav) R.drawable.baseline_favorite_24
                    else R.drawable.baseline_favorite_border_24
                )


            }
    }


}