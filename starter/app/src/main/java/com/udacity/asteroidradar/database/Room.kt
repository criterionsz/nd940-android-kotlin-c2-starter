package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("select * from databaseasteroid order by date(closeApproachDate)")
    fun getAsteroids():LiveData<List<DatabaseAsteroid>>
    @Query("select * from databaseasteroid where date(closeApproachDate)>=date(:startDay) and date(closeApproachDate)<=date(:endDay) order by date(closeApproachDate)")
    fun getWeekAsteroids(startDay: String, endDay: String):List<DatabaseAsteroid>
    @Query("select * from databaseasteroid where closeApproachDate=:today")
    fun getTodayAsteroids(today: String):List<DatabaseAsteroid>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AsteroidDatabase::class.java,
                    "videos").build()
        }
    }
    return INSTANCE
}