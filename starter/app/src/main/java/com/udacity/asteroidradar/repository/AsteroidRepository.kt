package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.DateUtils
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.api.AsteroidApi
import com.udacity.asteroidradar.network.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun getTodayAsteroids(): List<Asteroid> = withContext(Dispatchers.IO) {
      database.asteroidDao.getTodayAsteroids(DateUtils.startDate()).asDomainModel()
    }

    suspend fun getWeekAsteroids(): List<Asteroid> = withContext(Dispatchers.IO) {
        database.asteroidDao.getWeekAsteroids(DateUtils.startDate(), DateUtils.endDate()).asDomainModel()
    }

    suspend fun getImage() =  withContext(Dispatchers.IO) {
        AsteroidApi.retrofitService.getImage()
    }

    suspend fun refreshAsteroids() = withContext(Dispatchers.IO) {
        val asteroids = AsteroidApi.retrofitService.getAsteroid(
            startDate = DateUtils.startDate(),
            endDate = DateUtils.endDate()
        )
        val asteroidsConverted = parseAsteroidsJsonResult(JSONObject(asteroids.body()!!))
        database.asteroidDao.insertAll(*asteroidsConverted.asDatabaseModel())
    }

}