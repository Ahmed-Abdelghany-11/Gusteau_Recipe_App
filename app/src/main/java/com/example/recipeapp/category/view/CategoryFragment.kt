package com.example.recipeapp.category.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.category.view.adapter.CatRecipesAdapter
import com.example.recipeapp.category.repo.CategoryRepoImp
import com.example.recipeapp.category.viewModel.CatViewModelFactory
import com.example.recipeapp.category.viewModel.CategoryViewModel
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.dto.Meal
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.example.recipeapp.common.ChangeFavBtn
import com.example.recipeapp.common.CheckInternetViewModel
import com.example.recipeapp.common.OnFavBtnClickListener
import com.example.recipeapp.common.OnMealClickListener
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class CategoryFragment : Fragment(R.layout.fragment_category), OnMealClickListener,
    OnFavBtnClickListener, ChangeFavBtn {

    private lateinit var viewModel: CategoryViewModel
    private lateinit var categoriesAdapter: CatRecipesAdapter
    private lateinit var catRecyclerView: RecyclerView
    private val args: CategoryFragmentArgs by navArgs()
    private lateinit var authSharedPref: AuthSharedPref
    private lateinit var noInternetAnim: LottieAnimationView


    private var isInitialLoad= true

    private val checkInternetViewModel: CheckInternetViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authSharedPref = AuthSharedPref(requireContext())
        noInternetAnim = view.findViewById(R.id.no_internet_anim)
        catRecyclerView= view.findViewById(R.id.CategoryRv)


        gettingViewModelReady()
        checkInternetViewModel.isOnline.observe(viewLifecycleOwner) { isOnline ->
            if (isOnline) {
                hideNoInternetAnim()
                catRecyclerView = view.findViewById(R.id.CategoryRv)
                viewModel.getRecipesOfCategory(args.categoryName)
                viewModel.categoryRecipes.observe(viewLifecycleOwner) { mealList ->
                    setUpRecyclerView(mealList.meals as MutableList<Meal>)
                }
                if (!isInitialLoad) {
                    Toast.makeText(requireContext(), "Internet restored", Toast.LENGTH_SHORT).show()
                }
            } else {
                showNoInternetAnim()
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
            }
            isInitialLoad = false
        }


    }

    fun showNoInternetAnim() {
        catRecyclerView.visibility= View.GONE
        noInternetAnim.visibility = View.VISIBLE
        noInternetAnim.playAnimation()
    }

    fun hideNoInternetAnim() {
        catRecyclerView.visibility=View.VISIBLE
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
        val userId = authSharedPref.getUserId()

        viewModel.isFavoriteMeal(userId, meal.idMeal).observe(viewLifecycleOwner) { isFav ->
            if (isFav) showAlertDialog(userId, meal, btn)
            else {
                addMealToFav(userId, meal)
                btn.setImageResource(R.drawable.baseline_favorite_24)
            }
        }
    }


    private fun showAlertDialog(userId: Int, meal: Meal, btn: ImageView) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Remove Meal From Favorites")
            .setMessage("Are you sure you want to remove this meal from favorites?")
            .setPositiveButton("Remove") { dialog, _ ->
                deleteFromFav(userId, meal)
                dialog.dismiss()
                btn.setImageResource(R.drawable.baseline_favorite_border_24)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun addMealToFav(userId: Int, meal: Meal) {
        viewModel.insertMeal(meal)
        viewModel.insertIntoFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )

    }

    private fun deleteFromFav(userId: Int, meal: Meal) {
        viewModel.deleteMeal(meal)
        viewModel.deleteFromFav(
            userMealCrossRef = UserMealCrossRef(
                userId,
                meal.idMeal
            )
        )
    }

    override fun changeFavBtn(meal: Meal, btn: ImageView) {

        viewModel.isFavoriteMeal(authSharedPref.getUserId(), meal.idMeal).observe(viewLifecycleOwner) { isFav ->
            btn.setImageResource(
                if (isFav) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )


        }
    }


}