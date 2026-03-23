package com.akas62083.qm.screens.home

import androidx.compose.ui.graphics.Color
import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagWithPoints

data class HomeUiState (
    val pointWithTags: List<PointWithTags> = emptyList(),
    val tagWithPoints: List<TagWithPoints> = emptyList(),
    val dropDownMenuLocationDisplay: Boolean = true, //trueだと、ドロワーで地点表示、falseだとタグ表示
    val isAddTagDialogOpened: Boolean = false,
    val isColorPickBottomSheetOpened: Boolean = false,
    val selectedColor: SelectedColor = SelectedColor.Red,
    val textFieldValueInAddTagDialog: String = ""
)
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