package com.andruid.magic.helpfulsense.database.dao

import androidx.room.*
import com.andruid.magic.helpfulsense.database.entity.Action
import kotlinx.coroutines.flow.Flow

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
    suspend fun insert(vararg actions: Action)

    /**
     * Query to fetch all actions as [Flow]
     * @return flow of list of actions
     */
    @Query("SELECT * FROM actions ORDER BY `order` ASC")
    fun getAllActions(): Flow<List<Action>>

    /**
     * Query to delete action(s)
     * @param actions to be deleted
     */
    @Delete
    suspend fun delete(vararg actions: Action)

    @Query("SELECT COUNT(*) FROM actions")
    suspend fun count(): Int
}