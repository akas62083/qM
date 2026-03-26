package com.akas62083.qm.screens.home

import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagWithPoints
import com.google.android.gms.maps.model.LatLng

sealed interface HomeEvent {
    // ## Drawer
    // ### point not-edit
    data class PointMapClicked(val point: PointWithTags): HomeEvent
    // ### tag not-edit
    data class TagMapClicked(val tag: TagWithPoints): HomeEvent
    data object AddTagDialogToggle: HomeEvent
    // ### point edit
    data class EditPointSTagsBottomSheetToggle(val point: MapPointEntity?): HomeEvent
    data class EditPointNameDialogToggle(val point: MapPointEntity?): HomeEvent
    data class DeletePointDialogToggle(val point: MapPointEntity?): HomeEvent
    // ### tag edit
    data class DeleteTagDialogToggle(val tag: MapTagEntity?): HomeEvent
    data class EditTagNameDialogToggle(val tag: MapTagEntity?): HomeEvent
    //
    data class DrawerMenuChanged(val value: Boolean): HomeEvent

    // ## Map
    data class MapClicked(val latLng: LatLng?): HomeEvent

    // ## AddTag
    data class NewTagNameChanged(val value: String): HomeEvent
    data object ColorPickerBottomSheetToggle: HomeEvent
    data class ColorChanged(val color: SelectedColor): HomeEvent
    data object AddNewTagButtonClicked: HomeEvent
    // ## AddPoint
    data class NewPointNameChanged(val value: String): HomeEvent
    data class SelectedTagClicked(val tag: MapTagEntity): HomeEvent
    data class UnSelectedTagClicked(val tag: MapTagEntity): HomeEvent
    data object AddNewPointButtonClicked: HomeEvent
    // ## DeleteTag
    data object DeleteTagButtonClicked: HomeEvent
    // ## DeletePoint
    data object DeletePointButtonClicked: HomeEvent
    // ## EditTagName
    data object EditTagNameButtonClicked: HomeEvent
    data class EditTagName(val value: String): HomeEvent
    // ## EditPointName
    data object EditPointNameButtonClicked: HomeEvent
    data class EditPointName(val value: String): HomeEvent
    // ## EditPointSTags
    data class RemoveTag(val tag: MapTagEntity): HomeEvent
    data class AddTag(val tag: MapTagEntity): HomeEvent
}
