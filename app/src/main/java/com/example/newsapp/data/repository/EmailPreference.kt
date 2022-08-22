package com.example.newsapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.newsapp.util.Constants

class EmailPreference(context: Context) {

    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val preference = EncryptedSharedPreferences.create(
        Constants.shared_preferences_name,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)


    fun getLoggedInEmail(): String? {
        return preference.getString(Constants.email_preference_name, null)
    }

    fun setLoggedInEmail(email: String) {
        val editor = preference.edit()
        editor.putString(Constants.email_preference_name, email)
        editor.apply()
    }

    fun clearLoggedInEmail() {
        val editor = preference.edit()
        editor.putString(Constants.email_preference_name, null)
        editor.apply()
    }

}