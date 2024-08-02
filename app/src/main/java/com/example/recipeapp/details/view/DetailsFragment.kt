package com.example.recipeapp.details.view
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.colormoon.readmoretextview.ReadMoreTextView
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.RetrofitHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch


class DetailsFragment : Fragment() {
   private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var video: YouTubePlayerView
    //private var youtubePlayer: YouTubePlayer? = null
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
            Log.v("mealId", args.idMeal)
            val myResponse = RetrofitHelper.service.getMealById(args.idMeal)
            val meal = myResponse.meals[0]
            val title = view.findViewById<TextView>(R.id.txtTitle)
            val img = view.findViewById<ImageView>(R.id.image)
            val category = view.findViewById<TextView>(R.id.txtCategory)
            val details = view.findViewById<ReadMoreTextView>(R.id.txtdetails)
            video = view.findViewById<YouTubePlayerView>(R.id.youtube_player_view)
            video.enableAutomaticInitialization = false
            lifecycle.addObserver(video)
            val videoId = findId(meal?.strYoutube ?: "https://www.youtube.com/watch?v=S0Q4gqBUs7c")
            Log.d("videoId", videoId)

            video.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })


            details.setTrimLength(2)
            details.setText(meal?.strInstructions)

            title.text = meal?.strMeal
            category.text = meal?.strCategory
            Glide.with(requireContext())
                .load(meal?.strMealThumb)
                .into(img)
        }




    }

    private fun findId(strYoutube: String): String {
        var index: Int = 0
        for (i in 0..strYoutube.length) {
            if (strYoutube[i] == '=') {
                index = i
                break
            }
        }
        val result = strYoutube.substring(index+1)
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        video.release()
    }

}