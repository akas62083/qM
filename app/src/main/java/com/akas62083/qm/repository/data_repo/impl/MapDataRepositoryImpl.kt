package com.akas62083.qm.repository.data_repo.impl

import com.akas62083.qm.db.MapDao
import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagPointRef
import com.akas62083.qm.db.tagandpoint.TagWithPoints
import com.akas62083.qm.repository.data_repo.MapDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MapDataRepositoryImpl @Inject constructor(
    private val dao: MapDao
): MapDataRepository {
    override suspend fun insertMapPoint(mapPoint: MapPointEntity) = dao.insertMapPoint(mapPoint)
    override suspend fun deleteMapPoint(mapPoint: MapPointEntity) = dao.deleteMapPoint(mapPoint)
    override suspend fun updateMapPoint(mapPoint: MapPointEntity) = dao.updateMapPoint(mapPoint)
    override fun getAllMapPoint(): Flow<List<MapPointEntity>> = dao.getAllMapPoint()
    override suspend fun getMapPointById(id: Int): MapPointEntity = dao.getMapPointById(id)

    override suspend fun insetrMapTag(mapTag: MapTagEntity): Long = dao.insertMapTag(mapTag)
    override suspend fun deleteMapTag(mapTag: MapTagEntity) = dao.deleteMapTag(mapTag)
    override suspend fun updateMapTag(mapTag: MapTagEntity) = dao.updateMapTag(mapTag)
    override fun getAllMapTag(): Flow<List<MapTagEntity>> = dao.getAllMapTag()
    override suspend fun getMapTagById(id: Int): MapTagEntity = dao.getMapTagById(id)

    override suspend fun insertTagPointRef(ref: TagPointRef) = dao.insertTagPointRef(ref)
    override suspend fun deleteTagPointRef(ref: TagPointRef) = dao.deleteTagPointRef(ref)
    override suspend fun getTagAndPointsByName(name: String): TagWithPoints? = dao.getTagAndPointsByName(name)
    override suspend fun getPointAndTagsById(id: Int): PointWithTags? = dao.getPointAndTagsById(id)
}