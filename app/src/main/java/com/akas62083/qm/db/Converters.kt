package com.akas62083.qm.db

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng

class Converters {
    @TypeConverter
    fun fromLatLng(latLng: LatLng): String = "${latLng.latitude},${latLng.longitude}"
    @TypeConverter
    fun toLatLng(data: String): LatLng {
        val pieces = data.split(",")
        return LatLng(pieces[0].toDouble(), pieces[1].toDouble())
    }

    @TypeConverter
    fun fromColor(color: Color): Long = color.value.toLong()

    @TypeConverter
    fun toColor(value: Long): Color = Color(value.toULong())
}