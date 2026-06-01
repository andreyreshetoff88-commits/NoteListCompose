package ru.reshetoff.database

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.reshetoff.common.Constants.ACCESS_TOKEN_KEY
import javax.inject.Inject
import androidx.core.content.edit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.reshetoff.common.Constants.REFRESH_TOKEN_KEY

class SharedPref @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "NoteListPref",
        Context.MODE_PRIVATE
    )

    var accessToken: String?
        get() = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        set(value) = sharedPreferences.edit { putString(ACCESS_TOKEN_KEY, value) }

    var refreshToken: String?
        get() = sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
        set(value) = sharedPreferences.edit { putString(REFRESH_TOKEN_KEY, value) }

    fun observeAccessToken(): Flow<String?> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == ACCESS_TOKEN_KEY) {
                trySend(accessToken)
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        trySend(accessToken)

        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
}