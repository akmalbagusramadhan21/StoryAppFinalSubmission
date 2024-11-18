package com.example.storyapp

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.ui.detailstory.DetailActivity

class StoryAdapter(private val stories: List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_story, parent, false)
        return StoryViewHolder(view)
    }
    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        holder.bind(story)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("STORY_ID", story.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = stories.size

    inner class StoryViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        fun bind(story: ListStoryItem){
            val imageView = itemView.findViewById<ImageView>(R.id.iv_item_photo)
            val tvItemName = itemView.findViewById<TextView>(R.id.tv_item_name)

            tvItemName.text = story.name

            Log.d("StoryAdapter", "Loading image from URL: ${story.photoUrl}")

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(imageView)


        }

    }
}