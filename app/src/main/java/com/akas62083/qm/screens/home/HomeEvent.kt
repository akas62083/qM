package com.akas62083.qm.screens.home

sealed interface HomeEvent {
    data class DropDownMenuDisplayChange(val value: Boolean): HomeEvent //trueは地点一覧、falseはタグ一覧
    data object OpenOrCloseAddTagDialog: HomeEvent
    data object OpenOrCloseColorPickBottomSheet: HomeEvent
    data class ClickedColor(val color: SelectedColor): HomeEvent
    data class ChangeTextFieldValueInAddTagDialog(val value: String): HomeEvent
    data object ClickedConfirmButton: HomeEvent
}
