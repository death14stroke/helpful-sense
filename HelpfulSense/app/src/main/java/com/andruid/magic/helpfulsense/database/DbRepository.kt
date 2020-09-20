package com.andruid.magic.helpfulsense.database

import android.app.Application
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.database.entity.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * Repository class for the database [HelpDatabase]
 */
object DbRepository {
    private lateinit var database: HelpDatabase

    /**
     * Create the database and repository instance
     * @param application current application
     */
    fun init(application: Application) {
        database = HelpDatabase(application.applicationContext)
    }

    /**
     * Helper to insert/update action in the database
     * @param action to be added/updated
     */
    suspend fun insert(action: Action, update: Boolean = false) {
        if (update)
            database.actionDao().insert(action)
        else
            database.actionDao().insert(action.copy(order = getActionsCount() + 1))
    }

    /**
     * Helper to insert list of actions in the database by deleting all previous actions
     * @param actions list of actions
     */
    suspend fun insertAllActions(actions: List<Action>) {
        fetchAllActions()
                .map { actionsList -> actionsList.toTypedArray() }
                .flowOn(Dispatchers.Default)
                .collect { oldActions ->
                    database.actionDao().delete(*oldActions)
                    database.actionDao().insert(*actions.toTypedArray())
                }
    }

    /**
     * Helper to delete action from the database
     * @param action to be deleted
     */
    suspend fun delete(action: Action) {
        database.actionDao().delete(action)
    }

    /**
     * Helper to get all the actions in the database
     * @return list of actions
     */
    fun fetchAllActions() = database.actionDao().getAllActions()

    /**
     * Helper to insert list of contacts in the database by deleting all previous contacts
     * @param contacts list of contacts
     */
    suspend fun insertAllContacts(contacts: List<Contact>) {
        fetchAllContacts()
                .collect { oldContacts ->
                    val deletedContacts = oldContacts.minus(contacts)
                    database.contactDao().delete(*deletedContacts.toTypedArray())
                    database.contactDao().insert(*contacts.toTypedArray())
                }
    }

    /**
     * Helper to delete a contact from the database
     * @param contact to be deleted
     */
    suspend fun delete(contact: Contact) {
        database.contactDao().delete(contact)
    }

    /**
     * Helper to get all the saved contacts
     * @return list of contacts
     */
    fun fetchAllContacts() = database.contactDao().getAllContacts()

    suspend fun getActionsCount() = database.actionDao().count()
}