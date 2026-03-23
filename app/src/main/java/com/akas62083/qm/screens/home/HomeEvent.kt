package com.akas62083.qm.screens.home

import com.akas62083.qm.db.maptag.MapTagEntity
import com.google.android.gms.maps.model.LatLng

sealed interface HomeEvent {
    data class DropDownMenuDisplayChange(val value: Boolean): HomeEvent //trueは地点一覧、falseはタグ一覧
    data object OpenOrCloseAddTagDialog: HomeEvent
    data object OpenOrCloseColorPickBottomSheet: HomeEvent
    data class ClickedColor(val color: SelectedColor): HomeEvent
    data class ChangeTextFieldValueInAddTagDialog(val value: String): HomeEvent
    data object ClickedConfirmButton: HomeEvent
    data class ClickedMap(val latLng: LatLng?): HomeEvent
    data class ClickedTag(val tag: MapTagEntity): HomeEvent
    data class ClickedTagToUnSelected(val tag: MapTagEntity): HomeEvent
    data object SavePoint: HomeEvent
    data class ChangeMapPointName(val value: String): HomeEvent
}
