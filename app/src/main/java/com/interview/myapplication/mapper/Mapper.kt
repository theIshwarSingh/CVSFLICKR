package com.example.imageflickrapp.data.mapper

/**
 * Generic interface for mapping entities to domain models.
 */
interface Mapper<E, D> {
    fun mapToDomain(type: E): D
}

