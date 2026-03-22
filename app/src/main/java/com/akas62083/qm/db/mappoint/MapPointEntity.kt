package com.akas62083.qm.db.mappoint

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akas62083.qm.db.maptag.MapTagEntity
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "map_point")
data class MapPointEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "point_id")
    val id: Long = 0,
    @ColumnInfo(name = "point_name")
    val name: String = "",
    @ColumnInfo(name = "point_description")
    val description: String = "",
    @ColumnInfo(name = "point_latlng")
    val latLng: LatLng,
    @ColumnInfo(name = "point_done")
    val done: Boolean = false
)