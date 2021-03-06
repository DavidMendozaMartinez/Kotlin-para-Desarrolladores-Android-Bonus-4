package com.antonioleiva.flowworkshop.data.db

import com.antonioleiva.flowworkshop.data.domain.LocalDataSource
import com.antonioleiva.flowworkshop.data.domain.Movie
import com.antonioleiva.flowworkshop.data.toDomainMovie
import com.antonioleiva.flowworkshop.data.toRoomMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.antonioleiva.flowworkshop.data.db.Movie as DbMovie

class RoomDataSource(db: MovieDatabase) : LocalDataSource {

    private val movieDao = db.movieDao()

    override suspend fun isEmpty(): Boolean = size() <= 0

    override suspend fun size(): Int = movieDao.movieCount()

    override suspend fun saveMovies(movies: List<Movie>) {
        movieDao.insertMovies(movies.map { it.toRoomMovie() })
    }

    override fun getMovies(): Flow<List<Movie>> =
            movieDao.getAll().map { it.map(DbMovie::toDomainMovie) }
}