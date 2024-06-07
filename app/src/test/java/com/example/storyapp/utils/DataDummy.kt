package com.example.storyapp.utils

import com.example.storyapp.data.network.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "name $i",
                "description $i",
                "http://example.com/photo$i.jpg",
                "2024-06-05T00:00:00Z",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}