package com.example.newsapp.data.repository

import android.content.Context
import com.example.newsapp.util.Constants

class EmailPreference(context: Context) {

    val preference = context.getSharedPreferences(Constants.shared_preferences_name, Context.MODE_PRIVATE)

    fun getLoggedInEmail(): String? {
        return preference.getString(Constants.email_preference_name, null)
    }

    fun setLoggedInEmail(email: String){
        val editor = preference.edit()
        editor.putString(Constants.email_preference_name,email)
        editor.apply()
    }

    fun clearLoggedInEmail(){
        val editor = preference.edit()
        editor.putString(Constants.email_preference_name,null)
        editor.apply()
    }

}