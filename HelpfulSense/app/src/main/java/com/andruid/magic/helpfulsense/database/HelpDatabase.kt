package com.andruid.magic.helpfulsense.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andruid.magic.helpfulsense.database.dao.ActionDao
import com.andruid.magic.helpfulsense.database.dao.ContactDao
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.database.entity.Contact

/**
 * Abstract class extending [RoomDatabase]
 */
@Database(entities = [Action::class, Contact::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class HelpDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private lateinit var instance: HelpDatabase
        private val LOCK = Any()

        operator fun invoke(context: Context): HelpDatabase {
            if (!::instance.isInitialized)
                synchronized(LOCK) {
                    instance = buildDatabase(context)
                }
            return instance
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
                HelpDatabase::class.java, "helpful-sense.db")
                .build()

    }

    /**
     * Get dao for [Action] related queries
     * @return dao
     */
    abstract fun actionDao(): ActionDao

    /**
     * Get dao for [Contact] related queries
     * @return dao
     */
    abstract fun contactDao(): ContactDao
}