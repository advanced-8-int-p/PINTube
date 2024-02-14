package com.example.pintube.ui.mypage

import android.content.Context
import android.content.SharedPreferences
import android.service.autofill.FillEventHistory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.pintube.data.local.entity.FavoriteEntity
import com.example.pintube.data.local.entity.LocalVideoEntity
import com.example.pintube.ui.main.MainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.util.concurrent.Flow
import java.util.prefs.Preferences
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@HiltViewModel
class MypageViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val favoriteEntity: FavoriteEntity,
    private val localVideoEntity: LocalVideoEntity
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    private var sharedPreferences: SharedPreferences


//    val EXAMPLE_COUNTER = intPreferencesKey("example_counter")
//    val exampleCounterFlow: Flow<Int> = context.dataStore.data
//        .map { preferences ->
//            // No type safety.
//            preferences[EXAMPLE_COUNTER] ?: 0
//        }
//
//    suspend fun incrementCounter() {
//        context.dataStore.edit { settings ->
//            val currentCounterValue = settings[EXAMPLE_COUNTER] ?: 0
//            settings[EXAMPLE_COUNTER] = currentCounterValue + 1
//        }
//    }
    init {
    sharedPreferences = MainActivity().getSharedPreferences("watchHistory", Context.MODE_PRIVATE)
    }

     fun saveWatchHistory(watchHistory: MutableList<String>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(watchHistory)
        editor.putString("id", json)
        editor.apply()
    }

    fun loadWatchHistory(): List<String> {
        val gson = Gson()
        val json = sharedPreferences.getString("id", null)
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}

