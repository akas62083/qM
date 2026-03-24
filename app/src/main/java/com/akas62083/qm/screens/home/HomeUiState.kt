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
    val dropDownMenuLocationDisplay: Boolean = true, //trueだと、ドロワーで地点表示、falseだとタグ表示
    val isAddTagDialogOpened: Boolean = false,
    val isColorPickBottomSheetOpened: Boolean = false,
    val selectedColor: SelectedColor = SelectedColor.Red,
    val textFieldValueInAddTagDialog: String = "",
    val selectedLatLng: LatLng? = null,
    val selectedTags: List<MapTagEntity> = emptyList(),
    val notSelectedTags: List<MapTagEntity> = emptyList(),
    val pointName: String = "",
    val markerType: MapMarker = MapMarker.None
) {
    val addTagDialogEnabled: Boolean =  textFieldValueInAddTagDialog != "" && !tagWithPoints.any { it.tag.name == textFieldValueInAddTagDialog }

}
sealed class SelectedColor {
    data object Red: SelectedColor()
    data object Orange: SelectedColor()
    data object Yellow: SelectedColor()
    data object Green: SelectedColor()
    data object Blue: SelectedColor()
    data object Purple: SelectedColor()
    data object Pink: SelectedColor()
    data class Custom(val color: Color): SelectedColor()
}

sealed class MapMarker {
    data object None: MapMarker()
    data class MarkerTagWithPointsWithTags(val tagWithPointsWithTags: TagWithPointsWithTags): MapMarker()
    data class MarkerPointWithTags(val pointWithTags: PointWithTags): MapMarker()
}