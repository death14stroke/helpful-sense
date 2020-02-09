package com.andruid.magic.helpfulsense.database

import android.app.Application
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.database.entity.Contact

class DbRepository {
    companion object {
        private lateinit var database: HelpDatabase
        private lateinit var instance: DbRepository

        fun init(application: Application) {
            database = HelpDatabase(application.applicationContext)
            instance = DbRepository()
        }

        fun getInstance(): DbRepository {
            if (!::instance.isInitialized)
                throw Exception("must call init() first in application class")
            return instance
        }
    }

    fun insert(action: Action) {
        database.actionDao().insert(action)
    }

    fun delete(action: Action) {
        database.actionDao().delete(action)
    }

    fun fetchActions() = database.actionDao().getAllActions()

    fun insertAll(contacts: List<Contact>) {
        val oldContacts = fetchContacts()
        val deletedContacts = oldContacts.minus(contacts)
        deletedContacts.forEach {
            database.contactDao().delete(it.contactID)
        }
        database.contactDao().insertAll(contacts)
    }

    fun fetchLiveContacts() = database.contactDao().getLiveContacts()

    fun fetchContacts() = database.contactDao().getAllContacts()
}