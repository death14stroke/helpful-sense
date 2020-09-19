package com.andruid.magic.helpfulsense.database.dao

import androidx.room.*
import com.andruid.magic.helpfulsense.database.entity.Contact
import kotlinx.coroutines.flow.Flow

/**
 * Dao for [Contact] entity
 */
@Dao
interface ContactDao {
    /**
     * Query to insert/update contact(s)
     * @param contacts to be added/updated
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg contacts: Contact)

    /**
     * Query to fetch all contacts as [Flow]
     * @return flow of list of contacts
     */
    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Flow<List<Contact>>

    /**
     * Query to delete contact(s)
     * @param contacts to be deleted
     */
    @Delete
    suspend fun delete(vararg contacts: Contact)
}