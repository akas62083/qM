package com.akas62083.qm.screens.home

import androidx.compose.ui.graphics.Color
import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagWithPoints
import com.akas62083.qm.db.tagandpoint.TagWithPointsWithTags
import com.google.android.gms.maps.model.LatLng

data class HomeUiState (
    val pointWithTags: List<PointWithTags> = emptyList(), // a list of a point and its tags. get first.
    val tagWithPoints: List<TagWithPoints> = emptyList(), // a list of a tag and its points. get first.
    val dropDownMenuLocationDisplay: Boolean = true, // what a drawer displays. true is point, false is tag.
    val isAddTagDialogOpened: Boolean = false, // dialog of add tag is opened or closed. false is closed.
    val isColorPickBottomSheetOpened: Boolean = false, // bottom sheet of pick color is opened or closed, false is closed.
    val selectedColor: SelectedColor = SelectedColor.Red, // selected color in add tag dialog.
    val textFieldValueInAddTagDialog: String = "", // text field value in add tag dialog.
    val selectedLatLng: LatLng? = null, // latlng that user clicked in map.
    val selectedTags: List<MapTagEntity> = emptyList(), // selected tags in bottom sheet of making a point.
    val notSelectedTags: List<MapTagEntity> = emptyList(), // not selected tags in bottom sheet of making a point. it same tagWithPoints at first (when user clicked map).
    val pointName: String = "", // a name of a point in bottom sheet. it can be also used for edit point name.
    val markerType: MapMarker = MapMarker.None, // marker type in map. when user click the drawer. it is a response for it.
    val editPointName: MapPointEntity? = null,
    val deletePoint: MapPointEntity? = null,
    val firstLatLng: LatLng? = null,
    val firstZoom: Double? = null,
    val editPointsTags: MapPointEntity? = null,
) {
    val addTagDialogEnabled: Boolean =  textFieldValueInAddTagDialog != "" && !tagWithPoints.any { it.tag.name == textFieldValueInAddTagDialog } // can or cannot save a tag in dialog of add tag.
    val pointNameEnabled: Boolean = pointName != "" && pointName.length < 21
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