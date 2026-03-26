package com.akas62083.qm.screens.home

import android.R.attr.tag
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagPointRef
import com.akas62083.qm.repository.data_repo.MapDataRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mapRepository: MapDataRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = combine(
        _uiState,
        mapRepository.getAllPointAndTags(),
        mapRepository.getAllTagAndPoints(),
        mapRepository.currentMapDataStore
    ) { currentState, pat, tap, mapData ->
        currentState.copy(
            pointWithTags = pat,
            tagWithPoints = tap,
            firstLatLng = LatLng(mapData.latitude, mapData.longitude),
            firstZoom = mapData.zoom
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun saveLatLng(latlng: LatLng) {
        viewModelScope.launch {
            mapRepository.saveLatLng(latlng)
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
            is HomeEvent.ClickedMap -> { clickedMap(event.latLng) }
            is HomeEvent.ClickedTag -> { clickedTag(event.tag) }
            is HomeEvent.ClickedTagToUnSelected -> { clickedTagToUnSelected(event.tag) }
            is HomeEvent.SavePoint -> { savePoint() }
            is HomeEvent.ChangeMapPointName -> { changeMapPointName(event.value) }
            is HomeEvent.ClickedDrawerMenuPoint -> { clickedDrawerMenuPoint(event.point) }
            is HomeEvent.ClickedDrawerMenuTag -> { clickedDrawerMenuTag(event.tag.tag) }
            is HomeEvent.OpenOrCloseEditPointNameDialog -> { openOrCloseEditPointNameDialog(event.point) }
            is HomeEvent.EditPointName -> { editPointName() }
            is HomeEvent.DeletePointDialog -> { deletePointDialog(event.point) }
            is HomeEvent.DeletePoint -> { deletePoint() }
            is HomeEvent.OpenOrCloseBottomSheetOfEditPointsTags -> { openOrCloseBottomSheetOfEditPointsTags(event.point) }
            is HomeEvent.RemoveTag -> { removeTag(event.tag) }
            is HomeEvent.AddTag -> { addTag(event.tag)  }
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
    private fun clickedMap(latLng: LatLng?) {
        val notSelectedTags = mutableListOf<MapTagEntity>()
        uiState.value.tagWithPoints.forEach { tag ->
            notSelectedTags.add(tag.tag)
        }
        _uiState.update { currentState ->
            currentState.copy(
                pointName = "",
                selectedLatLng = latLng,
                selectedTags = emptyList(),
                notSelectedTags = notSelectedTags
            )
        }
    }
    private fun clickedTag(tag: MapTagEntity) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedTags = currentState.selectedTags.plus(tag),
                notSelectedTags = currentState.notSelectedTags.minus(tag)
            )
        }
    }
    private fun clickedTagToUnSelected(tag: MapTagEntity) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedTags = currentState.selectedTags.minus(tag),
                notSelectedTags = currentState.notSelectedTags.plus(tag)
            )
        }
    }
    private fun savePoint() {
        viewModelScope.launch {
            val newId = mapRepository.insertMapPoint(
                MapPointEntity(
                    name = uiState.value.pointName,
                    description = "",
                    latLng = uiState.value.selectedLatLng!!,
                )
            )
            uiState.value.selectedTags.forEach { tag ->
                mapRepository.insertTagPointRef(
                    TagPointRef(
                        tagId = tag.id,
                        pointId = newId
                    )
                )
            }
        }
        _uiState.update { currentState ->
            currentState.copy(selectedLatLng = null)
        }
    }
    private fun changeMapPointName(value: String) {
        _uiState.update { currentState ->
            currentState.copy(pointName = value)
        }
    }
    private fun clickedDrawerMenuTag(tag: MapTagEntity) {
        viewModelScope.launch {
            mapRepository.getTagWithPointsWithTagsById(tag.id).collect {
                _uiState.update { currentState ->
                    currentState.copy(markerType = MapMarker.MarkerTagWithPointsWithTags(it))
                }
            }
        }
    }
    private fun clickedDrawerMenuPoint(point: PointWithTags) {
        _uiState.update { currentState ->
            currentState.copy(markerType = MapMarker.MarkerPointWithTags(point))
        }
    }
    private fun openOrCloseEditPointNameDialog(point: MapPointEntity?) {
        _uiState.update { currentState ->
            currentState.copy(editPointName = point, pointName = point?.name ?: "")
        }
    }
    private fun editPointName() {
        viewModelScope.launch {
            mapRepository.updateMapPoint(
                uiState.value.editPointName!!.copy(name = uiState.value.pointName)
            )
        }
    }
    private fun deletePointDialog(point: MapPointEntity?) {
        _uiState.update { currentState ->
            currentState.copy(deletePoint = point)
        }
    }
    private fun deletePoint() {
        viewModelScope.launch {
            mapRepository.deleteMapPoint(uiState.value.deletePoint!!)
        }
    }
    private fun openOrCloseBottomSheetOfEditPointsTags(point: MapPointEntity?) {
        _uiState.update { currentState ->
            currentState.copy(
                editPointsTags = point
            )
        }
    }
    private fun removeTag(tag: MapTagEntity) {
        viewModelScope.launch {
            mapRepository.deleteTagPointRef(
                TagPointRef(
                    tagId = tag.id,
                    pointId = uiState.value.editPointsTags!!.id
                )
            )
        }
    }
    private fun addTag(tag: MapTagEntity) {
        viewModelScope.launch {
            mapRepository.insertTagPointRef(
                TagPointRef(
                    tagId = tag.id,
                    pointId = uiState.value.editPointsTags!!.id
                )
            )
        }
    }
}
