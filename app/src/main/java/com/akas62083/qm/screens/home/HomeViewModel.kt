package com.akas62083.qm.screens.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.repository.data_repo.MapDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mapRepository: MapDataRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }
    private fun load() {
        viewModelScope.launch {
            launch {
                mapRepository.getAllPointAndTags().collect {
                    _uiState.update { currentState ->
                        currentState.copy(pointWithTags = it)
                    }
                }
            }
            launch {
                mapRepository.getAllTagAndPoints().collect {
                    _uiState.update { currentState ->
                        currentState.copy(tagWithPoints = it)
                    }
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.DropDownMenuDisplayChange -> { dropDownMenuDisplayChange(event.value) }
            is HomeEvent.OpenOrCloseAddTagDialog -> { openOrCloseAddTagDialog() }
            is HomeEvent.OpenOrCloseColorPickBottomSheet -> { openOrCloseColorPickBottomSheet() }
            is HomeEvent.ClickedColor -> { clickedColor(event.color) }
            is HomeEvent.ChangeTextFieldValueInAddTagDialog -> { changeTextFieldValueInAddTagDialog(event.value) }
            is HomeEvent.ClickedConfirmButton -> { clickedConfirmButton() }
        }
    }

    private fun dropDownMenuDisplayChange(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(dropDownMenuLocationDisplay = value)
        }
    }
    private fun openOrCloseAddTagDialog() {
        if(!uiState.value.isAddTagDialogOpened) {
            _uiState.update { currentState ->
                currentState.copy(textFieldValueInAddTagDialog = "", selectedColor = SelectedColor.Red)
            }
        }
        _uiState.update { currentState ->
            currentState.copy(isAddTagDialogOpened = !currentState.isAddTagDialogOpened)
        }
    }
    private fun openOrCloseColorPickBottomSheet() {
        _uiState.update { currentState ->
            currentState.copy(isColorPickBottomSheetOpened = !currentState.isColorPickBottomSheetOpened)
        }
    }
    private fun clickedColor(color: SelectedColor) {
        _uiState.update { currentState ->
            currentState.copy(selectedColor = color)
        }
    }
    private fun changeTextFieldValueInAddTagDialog(value: String) {
        _uiState.update { currentState ->
            currentState.copy(textFieldValueInAddTagDialog = value)
        }
    }
    private fun clickedConfirmButton() {
        viewModelScope.launch {
            mapRepository.insertMapTag(
                MapTagEntity(
                    name = uiState.value.textFieldValueInAddTagDialog,
                    color = when(uiState.value.selectedColor) {
                        is SelectedColor.Red -> Color(0xffff5252)
                        is SelectedColor.Orange -> Color(0xffff9800)
                        is SelectedColor.Yellow -> Color(0xfffdd835)
                        is SelectedColor.Green -> Color(0xff4caf50)
                        is SelectedColor.Blue -> Color(0xff2196f3)
                        is SelectedColor.Purple -> Color(0xff9c27b0)
                        is SelectedColor.Pink -> Color(0xffe91e63)
                        is SelectedColor.Custom -> (uiState.value.selectedColor as SelectedColor.Custom).color
                    },
                    description = ""
                )
            )
        }
    }
}