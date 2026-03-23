package com.akas62083.qm.repository.data_repo

import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagPointRef
import com.akas62083.qm.db.tagandpoint.TagWithPoints
import kotlinx.coroutines.flow.Flow

interface MapDataRepository {
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

    suspend fun getTagAndPointsByName(name: String): TagWithPoints
    fun getAllTagAndPoints(): Flow<List<TagWithPoints>>
    suspend fun getPointAndTagsById(id: Int): PointWithTags
    fun getAllPointAndTags(): Flow<List<PointWithTags>>
}