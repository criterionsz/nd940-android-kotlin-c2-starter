package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.DateUtils
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.api.AsteroidApi
import com.udacity.asteroidradar.network.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _imageDay = MutableLiveData<PictureOfDay?>()
    val imageDay: LiveData<PictureOfDay?>
        get() = _imageDay

    private val _asteroidsToday = MutableLiveData<List<Asteroid>?>()
    val asteroidsToday: LiveData<List<Asteroid>?>
        get() = _asteroidsToday

    private val _asteroidsWeek = MutableLiveData<List<Asteroid>?>()
    val asteroidsWeek: LiveData<List<Asteroid>?>
        get() = _asteroidsWeek

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidRepository(database)

    init {
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshAsteroids()
            } catch (e: Exception) {
                Timber.v(e)
            }

        }
        getImageOfTheDay()
    }

    val asteroid = asteroidsRepository.asteroids

    fun getAsteroidsToday() = viewModelScope.launch {
        _asteroidsToday.value = asteroidsRepository.getTodayAsteroids()
    }

    fun getAsteroidsWeek() = viewModelScope.launch {
        _asteroidsWeek.value = asteroidsRepository.getWeekAsteroids()
    }

    private fun getImageOfTheDay() = viewModelScope.launch {
        try {
            _imageDay.value = asteroidsRepository.getImage()
        } catch (e: Exception) {
            Timber.v(e)
        }
    }

    enum class AsteroidApiStatus { LOADING, ERROR, DONE }

    /**
     * Factory for constructing ViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }


}