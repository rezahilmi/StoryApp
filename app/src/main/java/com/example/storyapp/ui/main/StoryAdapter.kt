package com.example.storyapp.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.network.ListStoryItem
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.ui.detailStory.DetailStoryActivity

@Suppress("DEPRECATION")
class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvItemName.text = story.name
            binding.descriptionStory.text = if (story.description!!.length > 100) {
                "${story.description.substring(0, 100)}..."
            } else {
                story.description
            }
            Glide.with(itemView.context).load(story.photoUrl).into(binding.ivItemPhoto)

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val storyId = getItem(position)
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                        putExtra(DetailStoryActivity.KEY_ID, storyId?.id)
                    }

                    val imagePair = androidx.core.util.Pair(binding.ivItemPhoto as View, "photo")
                    val titlePair = androidx.core.util.Pair(binding.tvItemName as View, "name")
                    val descriptionPair = androidx.core.util.Pair(binding.descriptionStory as View, "description")

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

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

