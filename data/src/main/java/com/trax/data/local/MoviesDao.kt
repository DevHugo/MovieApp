package com.trax.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface MoviesDao {

    @Query("SELECT * FROM MovieEntity")
    fun getAll(): Observable<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity WHERE id=:movieId")
    fun getOne(movieId: Int): Observable<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE title=:title")
    fun getOne(title: String): MovieEntity

    @Insert
    fun insertAll(movies: List<MovieEntity>): Completable

    @Query("DELETE FROM MovieEntity")
    fun deleteAll(): Completable
}
