package com.andruid.magic.helpfulsense.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andruid.magic.helpfulsense.database.entity.Action

/**
 * Dao for [Action] entity
 */
@Dao
interface ActionDao {
    /**
     * Query to insert/update action(s)
     * @param actions to be added/updated
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg actions: Action)

    /**
     * Query to fetch all actions as [LiveData]
     * @return liveData of list of actions
     */
    @Query("SELECT * FROM actions")
    fun getLiveActions(): LiveData<List<Action>>

    /**
     * Query to fetch all actions
     * @return list of actions
     */
    @Query("SELECT * FROM actions")
    fun getAllActions(): List<Action>

    /**
     * Query to delete action(s)
     * @param actions to be deleted
     */
    @Delete
    fun delete(vararg actions: Action)
}