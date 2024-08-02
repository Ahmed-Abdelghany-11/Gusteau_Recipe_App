package com.example.recipeapp.details.view
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.dao.UserWithMealDao
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.remote.RetrofitHelper
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.home.details.repo.DetailsRepo
import com.example.recipeapp.home.details.repo.DetailsRepoImpl
import com.example.recipeapp.home.details.viewmodel.DetailsViewModel
import com.example.recipeapp.home.details.viewmodel.DetailsViewModelFactory
import com.example.recipeapp.home.favorite.repo.FavRepo
import com.example.recipeapp.home.favorite.repo.FavRepoImpl
import com.example.recipeapp.home.favorite.viewmodel.FavViewModel
import com.example.recipeapp.home.favorite.viewmodel.FavViewModelFactory
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
            val data: Meal = args.MealData
            val title = view.findViewById<TextView>(R.id.txtTitle)
            val img = view.findViewById<ImageView>(R.id.image)
            val category = view.findViewById<TextView>(R.id.txtCategory)
            val details = view.findViewById<TextView>(R.id.txtdetails)
            val video = view.findViewById<YouTubePlayerView>(R.id.youtube_player_view)
            val videoId = data.strYoutube?.split("v=")?.get(1)
            video.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youtubePlayer = youTubePlayer
                    youtubePlayer?.loadVideo(videoId!!, 0f)
                }
            })

            gettingViewModelReady()
            authSharedPref = AuthSharedPref(requireContext())

            val userId = authSharedPref.getUserId()

            val favBtn = view.findViewById<ImageView>(R.id.addToFav)
            favBtn.setOnClickListener {
                viewModel.insertMeal(data)
                viewModel.insertIntoFav(
                    userMealCrossRef = UserMealCrossRef(
                        userId,
                        data.idMeal
                    )
                )
                favBtn.setImageResource(R.drawable.baseline_favorite_24)
            }

            details.text = data.strInstructions
            title.text = data.strMeal
            category.text = data.strCategory
            Glide.with(requireContext())
                .load(data.strMealThumb)
                .into(img)

        }
        }



    private fun gettingViewModelReady() {
        val DetailesFactory = DetailsViewModelFactory(
            DetailsRepoImpl(
                localDataSource = LocalDataSourceImpl(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, DetailesFactory)[DetailsViewModel::class.java]
    }
}