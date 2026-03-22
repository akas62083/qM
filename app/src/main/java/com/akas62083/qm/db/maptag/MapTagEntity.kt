package com.akas62083.qm.db.maptag

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "map_tag")
data class MapTagEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tag_id")
    val id: Long = 0,
    @ColumnInfo(name = "tag_name")
    val name: String = "",
    @ColumnInfo(name = "tag_description")
    val description: String = "",
    @ColumnInfo(name = "tag_color")
    val color: Color = Color(0xffff0000)
)
