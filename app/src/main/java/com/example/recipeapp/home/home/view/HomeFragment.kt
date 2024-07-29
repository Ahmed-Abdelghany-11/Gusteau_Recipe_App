package com.example.recipeapp.home.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.home.home.repo.RetrofitRepoImp
import com.example.recipeapp.home.home.viewModel.HomeViewModel
import com.example.recipeapp.home.home.viewModel.ViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewRecipes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        gettingViewModelReady()
        viewModel.getMyResponse()
        viewModel.getMyResponse.observe(viewLifecycleOwner) { getMyResponse ->
            val meal = getMyResponse.meals
            recyclerView.adapter = RandomRecipeAdapter(meal)
        }*/

        val textView = view.findViewById<TextView>(R.id.RandomTextView)
        val imageView = view.findViewById<ImageView>(R.id.RandomImageView)
        gettingViewModelReady()
        viewModel.getMyResponse()
        viewModel.getMyResponse.observe(viewLifecycleOwner) { getMyResponse ->
            val meal = getMyResponse.meals
            textView.text = meal[0].strMeal
            Glide.with(requireContext()).load(meal[0].strMealThumb).centerCrop().into(imageView)
        }

    }

    private fun gettingViewModelReady(){
        val factory = ViewModelFactory(
            repo = RetrofitRepoImp(
                remoteDataSource = APIClient
            )
        )
        viewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)
    }


}