package com.example.recipeapp.aboutUs.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.data.local.model.TeamMember
import com.google.android.material.imageview.ShapeableImageView

class TeamMembersAdapter(val members:List<TeamMember>):RecyclerView.Adapter<TeamMembersAdapter.MemberViewHolder>() {


    class MemberViewHolder(view: View):RecyclerView.ViewHolder(view){
        val memberImg: ShapeableImageView = view.findViewById(R.id.imageView)
        val name: TextView = view.findViewById(R.id.member_name)
        val email: TextView = view.findViewById(R.id.member_email)

        fun bind(member: TeamMember) {
            memberImg.setImageResource(member.image)
            name.text = member.name
            email.text = member.email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.team_member,parent,false)
        return MemberViewHolder(view)
    }

    override fun getItemCount()= members.size


    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member= members[position]
        holder.bind(member)
    }
}