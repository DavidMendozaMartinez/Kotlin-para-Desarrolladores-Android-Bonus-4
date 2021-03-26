package com.antonioleiva.flowworkshop.data.domain

import kotlinx.coroutines.flow.Flow

class MoviesRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) {
    companion object {
        const val PAGE_THRESHOLD = 10
        const val PAGE_SIZE = 20
    }

    fun getMovies(): Flow<List<Movie>> = localDataSource.getMovies()

    suspend fun checkRequireNewPage(lastVisible: Int) {
        val size = localDataSource.size()
        if (lastVisible >= size - PAGE_THRESHOLD) {
            val page = size / PAGE_SIZE + 1
            val newMovies = remoteDataSource.getMovies(page)
            localDataSource.saveMovies(newMovies)
        }
    }
}

interface RemoteDataSource {
    suspend fun getMovies(page: Int): List<Movie>
}

interface LocalDataSource {
    suspend fun isEmpty(): Boolean
    suspend fun size(): Int
    suspend fun saveMovies(movies: List<Movie>)
    fun getMovies(): Flow<List<Movie>>
}