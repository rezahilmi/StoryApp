package com.example.storyapp.ui.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.api.ListStoryItem
import com.example.storyapp.data.api.StoryResponse
import com.example.storyapp.ui.detailStory.DetailStoryActivity

class StoryAdapter(private val context: Context) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private val storyList = mutableListOf<ListStoryItem>()

    fun setStories(stories: List<ListStoryItem>) {
        storyList.clear()
        storyList.addAll(stories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = storyList[position]
        holder.title.text = story.name
        holder.description.text = story.description
        Glide.with(context).load(story.photoUrl).into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailStoryActivity::class.java).apply {
                putExtra(DetailStoryActivity.KEY_ID, story.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_story)
        val description: TextView = itemView.findViewById(R.id.description_story)
        val image: ImageView = itemView.findViewById(R.id.image_story)
    }
}

