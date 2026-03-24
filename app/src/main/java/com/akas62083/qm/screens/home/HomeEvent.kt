package com.akas62083.qm.screens.home

import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagWithPoints
import com.google.android.gms.maps.model.LatLng

sealed interface HomeEvent {
    // Drawer
    data class DropDownMenuDisplayChange(val value: Boolean): HomeEvent //trueは地点一覧、falseはタグ一覧
    data object OpenOrCloseAddTagDialog: HomeEvent
    data class ClickedDrawerMenuPoint(val point: PointWithTags): HomeEvent
    data class ClickedDrawerMenuTag(val tag: TagWithPoints): HomeEvent
    data class OpenOrCloseEditPointNameDialog(val point: MapPointEntity?): HomeEvent
    data object EditPointName: HomeEvent
    data class DeletePointDialog(val point: MapPointEntity?): HomeEvent
    data object DeletePoint: HomeEvent

    // Map
    data class ClickedMap(val latLng: LatLng?): HomeEvent

    // AddTagDialog
    // OpenOrCloseAddTagDialog: HomeEvent
    data object OpenOrCloseColorPickBottomSheet: HomeEvent
    data class ClickedColor(val color: SelectedColor): HomeEvent
    data class ChangeTextFieldValueInAddTagDialog(val value: String): HomeEvent
    data object ClickedConfirmButton: HomeEvent

    // MapBottomSheet
    // ClickedMap: HomeEvent
    data class ChangeMapPointName(val value: String): HomeEvent
    data class ClickedTagToUnSelected(val tag: MapTagEntity): HomeEvent
    data class ClickedTag(val tag: MapTagEntity): HomeEvent
    data object SavePoint: HomeEvent
}
