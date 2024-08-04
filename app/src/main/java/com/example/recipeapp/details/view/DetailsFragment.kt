package com.example.recipeapp.details.view
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.colormoon.readmoretextview.ReadMoreTextView
import com.example.recipeapp.R
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.details.repo.DetailsRepoImpl
import com.example.recipeapp.details.viewmodel.DetailsViewModel
import com.example.recipeapp.details.viewmodel.DetailsViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch


class DetailsFragment : Fragment() {


    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var viewModel : DetailsViewModel
    private var youtubePlayer: YouTubePlayer? = null
    private lateinit var authSharedPref: AuthSharedPref


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {

            //get the views
            val title = view.findViewById<TextView>(R.id.txtTitle)
            val img = view.findViewById<ImageView>(R.id.image)
            val category = view.findViewById<TextView>(R.id.txtCategory)
            val details = view.findViewById<ReadMoreTextView>(R.id.txtdetails)
            val video = view.findViewById<YouTubePlayerView>(R.id.youtube_player_view)
            val favBtn = view.findViewById<ImageView>(R.id.addToFav)

            //get the data from args
            val data: Meal = args.MealData

            // get the view model ready and get the user id
            gettingViewModelReady()
            authSharedPref = AuthSharedPref(requireContext())
            val userId = authSharedPref.getUserId()
            details.setTrimLength(2)

            //check if the meal is favorite or not
            viewModel.isFavoriteMeal(userId, data.idMeal)

            //observe the isFavorite value and set the image resource
            viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
                favBtn.setImageResource(
                    if (isFavorite) R.drawable.baseline_favorite_24
                    else R.drawable.baseline_favorite_border_24
                )
                favBtn.setOnClickListener {
                    if (isFavorite) {
                        showRemoveFavoriteDialog(data, userId)
                    } else {
                        addFavoriteMeal(data, userId)
                    }
                }
            }


            //check if we will the data is from the api or from the local database
            if (data.strArea == null) {

                viewModel.getMealById(data.idMeal)
                viewModel.meal.observe(viewLifecycleOwner) { meal ->
                    details.setTrimLength(2)
                    val newData: MealList = meal
                    val mealData = newData.meals[0]
                    details.text = mealData?.strInstructions
                    title.text = mealData?.strMeal
                    category.text = mealData?.strCategory
                    Glide.with(requireContext())
                        .load(mealData?.strMealThumb)
                        .into(img)
                    val videoId = mealData?.strYoutube?.substringAfterLast("v=")
                    if (videoId != null) {
                        video.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                youtubePlayer = youTubePlayer
                                youtubePlayer?.loadVideo(videoId, 0f)
                            }
                        })

                    }
                }}

            else {

                        details.text = data.strInstructions
                        title.text = data.strMeal
                        category.text = data.strCategory
                        Glide.with(requireContext())
                            .load(data.strMealThumb)
                            .into(img)

                        val videoId = data.strYoutube?.substringAfterLast("v=")
                        if (videoId != null) {
                            video.addYouTubePlayerListener(object :
                                AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    youtubePlayer = youTubePlayer
                                    youtubePlayer?.loadVideo(videoId, 0f)
                                }
                            })
                        }

                    }
                }
            }

            private fun gettingViewModelReady() {
                val detailsFactory = DetailsViewModelFactory(
                    DetailsRepoImpl(
                        localDataSource = LocalDataSourceImpl(requireContext()),
                        remoteDataSource = APIClient
                    )
                )
                viewModel = ViewModelProvider(this, detailsFactory)[DetailsViewModel::class.java]
            }

            private fun addFavoriteMeal(meal: Meal, userId: Int) {
                viewModel.insertMeal(meal)
                viewModel.insertIntoFav(UserMealCrossRef(userId, meal.idMeal))
                viewModel.isFavoriteMeal(userId, meal.idMeal)
            }

            private fun showRemoveFavoriteDialog(meal: Meal, userId: Int) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Remove Meal From Favorites")
                    .setMessage("Are you sure you want to remove this meal from favorites?")
                    .setPositiveButton("Remove") { dialog, _ ->
                        viewModel.deleteMeal(meal)
                        viewModel.deleteFromFav(UserMealCrossRef(userId, meal.idMeal))
                        viewModel.isFavoriteMeal(userId, meal.idMeal)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            override fun onPause() {
                super.onPause()
                youtubePlayer?.pause()
            }

            override fun onDestroyView() {
                super.onDestroyView()
                youtubePlayer?.pause()
            }
    }
