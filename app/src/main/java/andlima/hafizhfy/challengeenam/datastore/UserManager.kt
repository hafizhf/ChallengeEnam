package andlima.hafizhfy.challengeenam.datastore

import andlima.hafizhfy.challengeenam.model.GetAllFilmResponseItem
import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context: Context) {
    private val dataStore : DataStore<Preferences> = context.createDataStore(name = "userpref")

    companion object {
        val USERNAME = preferencesKey<String>("KEY_USERNAME")
        val EMAIL = preferencesKey<String>("KEY_EMAIL")
        val AVATAR = preferencesKey<String>("KEY_AVATAR")
        val PASSWORD = preferencesKey<String>("KEY_PASSWORD")

        val ID = preferencesKey<String>("KEY_ID")
        val COMPLETE_NAME = preferencesKey<String>("KEY_COMPLETE_NAME")
        val ADDRESS = preferencesKey<String>("KEY_ADDRESS")
        val DATE_OF_BIRTH = preferencesKey<String>("KEY_DATE_OF_BIRTH")
    }

    suspend fun loginUserData(
        username: String,
        email: String,
        avatar: String,
        password: String,
        id: String,
        completeName: String,
        address: String,
        dateOfBirth: String
    ) {
        dataStore.edit {
            it[USERNAME] = username
            it[EMAIL] = email
            it[AVATAR] = avatar
            it[PASSWORD] = password
            it[ID] = id
            it[COMPLETE_NAME] = completeName
            it[ADDRESS] = address
            it[DATE_OF_BIRTH] = dateOfBirth
        }
    }

    suspend fun editUserData(
        username: String,
        email: String,
        avatar: String,
        completeName: String,
        address: String,
        dateOfBirth: String
    ) {
        dataStore.edit {
            it[USERNAME] = username
            it[EMAIL] = email
            it[AVATAR] = avatar
            it[COMPLETE_NAME] = completeName
            it[ADDRESS] = address
            it[DATE_OF_BIRTH] = dateOfBirth
        }
    }

    val username : Flow<String> = dataStore.data.map {
        it[USERNAME] ?: ""
    }

    val email : Flow<String> = dataStore.data.map {
        it[EMAIL] ?: ""
    }

    val avatar : Flow<String> = dataStore.data.map {
        it[AVATAR] ?: ""
    }

    val password : Flow<String> = dataStore.data.map {
        it[PASSWORD] ?: ""
    }

    val id : Flow<String> = dataStore.data.map {
        it[ID] ?: ""
    }

    val completeName : Flow<String> = dataStore.data.map {
        it[COMPLETE_NAME] ?: ""
    }

    val address : Flow<String> = dataStore.data.map {
        it[ADDRESS] ?: ""
    }

    val dateOfBirth : Flow<String> = dataStore.data.map {
        it[DATE_OF_BIRTH] ?: ""
    }

    suspend fun clearData() {
        dataStore.edit {
            it.clear()
        }
    }

}