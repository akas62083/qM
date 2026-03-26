package com.akas62083.qm.repository.data_repo.impl

import android.R.attr.name
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.akas62083.qm.db.MapDao
import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagPointRef
import com.akas62083.qm.db.tagandpoint.TagWithPoints
import com.akas62083.qm.db.tagandpoint.TagWithPointsWithTags
import com.akas62083.qm.repository.data_repo.MapDataRepository
import com.akas62083.qm.repository.data_repo.MapDataStore
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class MapDataRepositoryImpl @Inject constructor(
    private val dao: MapDao,
    private val dataStore: DataStore<Preferences>
): MapDataRepository {
    private companion object {
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
        val ZOOM = doublePreferencesKey("zoom")
    }
    override val currentMapDataStore: Flow<MapDataStore> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                MapDataStore(
                    latitude = preferences[LATITUDE] ?: 0.0,
                    longitude = preferences[LONGITUDE] ?: 0.0,
                    zoom = preferences[ZOOM] ?: 15.0
                )
            }


    override suspend fun saveLatLng(latlng: LatLng) {
        dataStore.edit { preferences ->
            preferences[LATITUDE] = latlng.latitude
            preferences[LONGITUDE] = latlng.longitude
        }
    }
    override suspend fun saveZoom(zoom: Double) {
        dataStore.edit { preferences ->
            preferences[ZOOM] = zoom
        }
    }

    override suspend fun insertMapPoint(mapPoint: MapPointEntity) = dao.insertMapPoint(mapPoint)
    override suspend fun deleteMapPoint(mapPoint: MapPointEntity) = dao.deleteMapPoint(mapPoint)
    override suspend fun updateMapPoint(mapPoint: MapPointEntity) = dao.updateMapPoint(mapPoint)
    override fun getAllMapPoint(): Flow<List<MapPointEntity>> = dao.getAllMapPoint()
    override suspend fun getMapPointById(id: Int): MapPointEntity = dao.getMapPointById(id)

    override suspend fun insertMapTag(mapTag: MapTagEntity): Long = dao.insertMapTag(mapTag)
    override suspend fun deleteMapTag(mapTag: MapTagEntity) = dao.deleteMapTag(mapTag)
    override suspend fun updateMapTag(mapTag: MapTagEntity) = dao.updateMapTag(mapTag)
    override fun getAllMapTag(): Flow<List<MapTagEntity>> = dao.getAllMapTag()
    override suspend fun getMapTagById(id: Int): MapTagEntity = dao.getMapTagById(id)

    override suspend fun insertTagPointRef(ref: TagPointRef) = dao.insertTagPointRef(ref)
    override suspend fun deleteTagPointRef(ref: TagPointRef) = dao.deleteTagPointRef(ref)

    override fun getTagAndPointsByName(id: Long): Flow<TagWithPoints> = dao.getTagAndPointsByName(id)
    override fun getAllTagAndPoints(): Flow<List<TagWithPoints>> = dao.getAllTagWithPoints()
    override fun getPointAndTagsById(id: Int): Flow<PointWithTags> = dao.getPointAndTagsById(id)
    override fun getAllPointAndTags(): Flow<List<PointWithTags>> = dao.getAllPointWithTags()
    override fun getTagWithPointsWithTagsById(id: Long): Flow<TagWithPointsWithTags> = dao.getTagWithPointsWithTagsById(id)
}