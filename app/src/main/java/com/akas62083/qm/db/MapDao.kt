package com.akas62083.qm.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagPointRef
import com.akas62083.qm.db.tagandpoint.TagWithPoints
import com.akas62083.qm.db.tagandpoint.TagWithPointsWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface MapDao {
    @Insert
    suspend fun insertMapPoint(point: MapPointEntity): Long
    @Delete
    suspend fun deleteMapPoint(point: MapPointEntity)
    @Update
    suspend fun updateMapPoint(point: MapPointEntity)
    @Query("select * from map_point")
    fun getAllMapPoint(): Flow<List<MapPointEntity>>
    @Query("select * from map_point where point_id = :id")
    suspend fun getMapPointById(id: Int): MapPointEntity

    @Insert
    suspend fun insertMapTag(tag: MapTagEntity): Long
    @Delete
    suspend fun deleteMapTag(tag: MapTagEntity)
    @Update
    suspend fun updateMapTag(tag: MapTagEntity)
    @Query("select * from map_tag")
    fun getAllMapTag(): Flow<List<MapTagEntity>>
    @Query("select * from map_tag where tag_id = :id")
    suspend fun getMapTagById(id: Int): MapTagEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTagPointRef(ref: TagPointRef)
    @Delete
    suspend fun deleteTagPointRef(ref: TagPointRef)

    @Transaction
    @Query("""
        select * from map_tag
        where tag_id = :id
        limit 1
    """)
    fun getTagAndPointsByName(id: Long): Flow<TagWithPoints>
    @Transaction
    @Query("SELECT * FROM map_tag order by tag_name asc")
    fun getAllTagWithPoints(): Flow<List<TagWithPoints>>
    @Transaction
    @Query("""
        select * from map_point
        where point_id = :id
        limit 1
    """)
    fun getPointAndTagsById(id: Int): Flow<PointWithTags>
    @Transaction
    @Query("SELECT * FROM map_point order by point_id desc")
    fun getAllPointWithTags(): Flow<List<PointWithTags>>
    @Transaction
    @Query("select * from map_tag where tag_id = :id limit 1")
    fun getTagWithPointsWithTagsById(id: Long): Flow<TagWithPointsWithTags>
}