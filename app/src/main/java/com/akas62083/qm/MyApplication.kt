package com.akas62083.qm

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.akas62083.qm.repository.data_repo.MapDataRepository
import com.akas62083.qm.repository.data_repo.impl.MapDataRepositoryImpl
import dagger.hilt.android.HiltAndroidApp



@HiltAndroidApp
class MyApplication: Application() {
}