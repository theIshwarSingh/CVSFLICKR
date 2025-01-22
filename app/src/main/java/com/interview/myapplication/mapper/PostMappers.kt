package com.interview.myapplication.mapper

import com.interview.myapplication.data.Items
import com.example.imageflickrapp.data.mapper.Mapper
import com.interview.myapplication.data.Posts

/**
 * Maps Items entity to Photo domain model.
 * Converts properties from Items to Photo for use in the domain layer.
 */
object PostMappers : Mapper<Items, Posts> {
    override fun mapToDomain(type: Items): Posts {
        return Posts(
            link = type.media.thumbnailUrl,
            title = type.title,
            description = type.description,
            author = type.author,
            dataTaken = type.dateTaken
        )
    }
}