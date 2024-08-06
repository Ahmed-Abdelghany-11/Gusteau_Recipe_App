package com.example.recipeapp.aboutUs.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.data.local.model.TeamMember
import com.example.recipeapp.aboutUs.view.adapters.TeamMembersAdapter

class AboutUsFragment : Fragment(R.layout.fragment_about_us) {
    private lateinit var membersRV: RecyclerView
    private lateinit var membersAdapter: TeamMembersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        membersRV = view.findViewById(R.id.members_rv)

        val members = listOf(
            TeamMember("Ahmed Abdelghany", "anofal786@gmail.com", R.drawable.avatar),
            TeamMember("Alaa Hassan", "alaahassan2019th@gmail.com", R.drawable.girl_image),
            TeamMember("Mai Muhammed", "maimuhammedkhalil@gmail.com", R.drawable.girl_image),
            TeamMember("Ziad Tarek", "ziadghonim621@gmail.com", R.drawable.avatar)
        )

        setUpMembersRecyclerView(members)
    }

    private fun setUpMembersRecyclerView(members: List<TeamMember>) {
        membersAdapter = TeamMembersAdapter(members)
        membersRV.layoutManager = LinearLayoutManager(requireContext())
        membersRV.adapter = membersAdapter
    }
}

