package com.example.recipeapp.home.details.view
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.RetrofitHelper
import com.example.recipeapp.data.remote.dto.Meal
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {
   private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var video: YouTubePlayerView
    private var youtubePlayer: YouTubePlayer? = null
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
            val data :Meal = args.MealData
//            val myResponse = RetrofitHelper.service.getMealById(args.idMeal)
//            val meal = myResponse.meals[0]
            val title = view.findViewById<TextView>(R.id.txtTitle)
            val img = view.findViewById<ImageView>(R.id.image)
            val category = view.findViewById<TextView>(R.id.txtCategory)
            val details = view.findViewById<TextView>(R.id.txtdetails)
            video = view.findViewById<YouTubePlayerView>(R.id.youtube_player_view)
            video.enableAutomaticInitialization = false
            lifecycle.addObserver(video)
//            Log.d("video", meal?.strYoutube?: "null")

            /*
            video.initialize(object: YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {}

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {}

            override fun onError(
            youTubePlayer: YouTubePlayer,
            error: PlayerConstants.PlayerError
            ) {}

            override fun onPlaybackQualityChange(
            youTubePlayer: YouTubePlayer,
            playbackQuality: PlayerConstants.PlaybackQuality
            ) {}

            override fun onPlaybackRateChange(
            youTubePlayer: YouTubePlayer,
            playbackRate: PlayerConstants.PlaybackRate
            ) {}

            override fun onReady(youTubePlayer: YouTubePlayer) {
            Log.d("video_id", "hello")
            youtubePlayer = youTubePlayer
            youtubePlayer?.loadVideo(meal.strYoutube, 0f)

            }

            override fun onStateChange(
            youTubePlayer: YouTubePlayer,
            state: PlayerConstants.PlayerState
            ) {}

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {}

            override fun onVideoLoadedFraction(
            youTubePlayer: YouTubePlayer,
            loadedFraction: Float
            ) {}

            })
            */
            details.text = data.strInstructions
            title.text = data.strMeal
            category.text = data.strCategory
            Glide.with(requireContext())
                .load(data.strMealThumb)
                .into(img)
        }


//        val title = view.findViewById<TextView>(R.id.txtTitle)
//        val img = view.findViewById<ImageView>(R.id.image)
//        title.text = args.mealObj.strMeal
//        Glide.with(requireContext())
//            .load(args.mealObj.strMealThumb)
//            .into(img)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        video.release()
    }

}