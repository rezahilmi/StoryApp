package com.example.storyapp.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.api.ListStoryItem
import com.example.storyapp.ui.detailStory.DetailStoryActivity

class StoryAdapter :
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
        holder.bind(story)
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tv_item_name)
        val description: TextView = itemView.findViewById(R.id.description_story)
        private val image: ImageView = itemView.findViewById(R.id.iv_item_photo)

        fun bind(story: ListStoryItem) {
            title.text = story.name
            description.text = if (story.description!!.length > 100) {
                "${story.description.substring(0, 100)}..."
            } else {
                story.description
            }
            Glide.with(itemView.context).load(story.photoUrl).into(image)

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val storyId = storyList[position]
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                        putExtra(DetailStoryActivity.KEY_ID, storyId.id)
                    }

                    val imagePair = androidx.core.util.Pair<View, String>(image, "photo")
                    val titlePair = androidx.core.util.Pair<View, String>(title, "name")
                    val descriptionPair = androidx.core.util.Pair<View, String>(description, "description")

                    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        imagePair,
                        titlePair,
                        descriptionPair
                    )

                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }
}

