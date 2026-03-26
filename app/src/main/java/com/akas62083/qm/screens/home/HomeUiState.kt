package com.akas62083.qm.screens.home

import androidx.compose.ui.graphics.Color
import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagWithPoints
import com.akas62083.qm.db.tagandpoint.TagWithPointsWithTags
import com.google.android.gms.maps.model.LatLng

data class HomeUiState (
    val pointWithTags: List<PointWithTags> = emptyList(),
    val tagWithPoints: List<TagWithPoints> = emptyList(),
    val markerType: MapMarker = MapMarker.None,
    val addOrEditEntity: AddOrEditEntity = AddOrEditEntity.None,
    val dropDownMenuLocationDisplay: Boolean = true,
    val firstLatLng: LatLng? = null,
    val firstZoom: Double? = null,
)

sealed class SelectedColor(val color: Color) {
    data object Red: SelectedColor(Color(0xffff5252))
    data object Orange: SelectedColor(Color(0xffff9800))
    data object Yellow: SelectedColor(Color(0xfffdd835))
    data object Green: SelectedColor(Color(0xff4caf50))
    data object Blue: SelectedColor(Color(0xff2196f3))
    data object Purple: SelectedColor(Color(0xff9c27b0))
    data object Pink: SelectedColor(Color(0xffe91e63))
    data class Custom(val customColor: Color): SelectedColor(customColor)
}

sealed class MapMarker {
    data object None: MapMarker()
    data class MarkerTagWithPointsWithTags(val tagWithPointsWithTags: TagWithPointsWithTags): MapMarker()
    data class MarkerPointWithTags(val pointWithTags: PointWithTags): MapMarker()
}

sealed interface AddOrEditEntity {
    data object None: AddOrEditEntity
    data class AddTag(val text: String, val selectedColor: SelectedColor, val colorPicker: Boolean = false): AddOrEditEntity {
        val enabled: Boolean = text.isNotBlank() && text.length <= 10
    }
    data class AddPoint(
        val text: String,
        val latLng: LatLng,
        val unSelectedTags: List<MapTagEntity>,
        val selectedTags: List<MapTagEntity> = emptyList()
    ): AddOrEditEntity {
        val enabled: Boolean = text.isNotBlank() && text.length <= 20
    }
    data class EditTagName(val tag: MapTagEntity, val text: String): AddOrEditEntity {
        val enabled: Boolean = text.isNotBlank() && text.length <= 10
    }
    data class EditPointName(val point: MapPointEntity, val text: String): AddOrEditEntity {
        val enabled: Boolean = text.isNotBlank() && text.length <= 20
    }
    data class EditPointSTags(val point: MapPointEntity): AddOrEditEntity
    data class DeletePoint(val point: MapPointEntity): AddOrEditEntity
    data class DeleteTag(val tag: MapTagEntity): AddOrEditEntity
}
