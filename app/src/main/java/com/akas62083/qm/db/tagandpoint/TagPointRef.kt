package com.akas62083.qm.db.tagandpoint

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity

@Entity(
    tableName = "tag_point_ref",
    primaryKeys = ["ref_tag_id", "ref_point_id"],
    foreignKeys = [
        ForeignKey(
            entity = MapTagEntity::class,
            parentColumns = arrayOf("tag_id"),
            childColumns = arrayOf("ref_tag_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MapPointEntity::class,
            parentColumns = arrayOf("point_id"),
            childColumns = arrayOf("ref_point_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ref_point_id")]
)
data class TagPointRef(
    @ColumnInfo(name = "ref_tag_id")
    val tagId: Int,
    @ColumnInfo(name = "ref_point_id")
    val pointId: Int,
)


data class TagWithPoints(
    @Embedded
    val tag: MapTagEntity,
    @Relation(
        entity = MapPointEntity::class,
        parentColumn = "tag_id",
        entityColumn = "point_id",
        associateBy = Junction(
            TagPointRef::class,
            parentColumn = "ref_tag_id",
            entityColumn = "ref_point_id"
        )
    )
    val points: List<MapPointEntity>
)

data class PointWithTags(
    @Embedded
    val point: MapPointEntity,
    @Relation(
        entity = MapTagEntity::class,
        parentColumn = "point_id",
        entityColumn = "tag_id",
        associateBy = Junction(
            TagPointRef::class,
            parentColumn = "ref_point_id",
            entityColumn = "ref_tag_id"
        )
    )
    val tags: List<MapTagEntity>
)