package com.akas62083.qm.repository.data_repo

import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagPointRef
import com.akas62083.qm.db.tagandpoint.TagWithPoints
import com.akas62083.qm.db.tagandpoint.TagWithPointsWithTags
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface MapDataRepository {
    val currentMapDataStore: Flow<MapDataStore>
    suspend fun saveLatLng(latlng: LatLng)
    suspend fun saveZoom(zoom: Double)
    suspend fun insertMapPoint(mapPoint: MapPointEntity): Long
    suspend fun deleteMapPoint(mapPoint: MapPointEntity)
    suspend fun updateMapPoint(mapPoint: MapPointEntity)
    fun getAllMapPoint(): Flow<List<MapPointEntity>>
    suspend fun getMapPointById(id: Int): MapPointEntity

    suspend fun insertMapTag(mapTag: MapTagEntity): Long
    suspend fun deleteMapTag(mapTag: MapTagEntity)
    suspend fun updateMapTag(mapTag: MapTagEntity)
    fun getAllMapTag(): Flow<List<MapTagEntity>>
    suspend fun getMapTagById(id: Int): MapTagEntity

    suspend fun insertTagPointRef(ref: TagPointRef)
    suspend fun deleteTagPointRef(ref: TagPointRef)

    fun getTagAndPointsByName(id: Long): Flow<TagWithPoints>
    fun getAllTagAndPoints(): Flow<List<TagWithPoints>>
    fun getPointAndTagsById(id: Int): Flow<PointWithTags>
    fun getAllPointAndTags(): Flow<List<PointWithTags>>
    fun getTagWithPointsWithTagsById(id: Long): Flow<TagWithPointsWithTags>
}

data class MapDataStore(
    val latitude: Double,
    val longitude: Double,
    val zoom: Double
)