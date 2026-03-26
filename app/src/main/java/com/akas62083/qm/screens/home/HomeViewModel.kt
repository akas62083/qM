package com.akas62083.qm.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            // ## Drawer
            // ### point not-edit
            is HomeEvent.PointMapClicked -> { pointMapClicked(event.point) }
            // ### tag not-edit
            is HomeEvent.TagMapClicked -> { tagMapClicked(event.tag.tag) }
            is HomeEvent.AddTagDialogToggle -> { addTagDialogToggle() }
            // ### point edit
            is HomeEvent.EditPointSTagsBottomSheetToggle -> { editPointSTagsBottomSheetToggle(event.point) }
            is HomeEvent.EditPointNameDialogToggle -> { editPointNameDialogToggle(event.point) }
            is HomeEvent.DeletePointDialogToggle -> { deletePointDialogToggle(event.point) }
            // ### tag edit
            is HomeEvent.DeleteTagDialogToggle -> { deleteTagDialogToggle(event.tag) }
            is HomeEvent.EditTagNameDialogToggle -> { editTagNameDialogToggle(event.tag) }
            //
            is HomeEvent.DrawerMenuChanged -> { drawerMenuChanged(event.value) }

            // ## Map
            is HomeEvent.MapClicked -> { mapClicked(event.latLng) }

            // ## AddTag
            is HomeEvent.NewTagNameChanged -> { newTagTextChanged(event.value) }
            is HomeEvent.ColorPickerBottomSheetToggle -> { colorPickerBottomSheetToggle() }
            is HomeEvent.ColorChanged -> { colorChanged(event.color) }
            is HomeEvent.AddNewTagButtonClicked -> { addNewTagButtonClicked() }
            // ## AddPoint
            is HomeEvent.NewPointNameChanged -> { pointNameChanged(event.value) }
            is HomeEvent.SelectedTagClicked -> { selectedTagClicked(event.tag) }
            is HomeEvent.UnSelectedTagClicked -> { unSelectedTagClicked(event.tag) }
            is HomeEvent.AddNewPointButtonClicked -> { addNewPointButtonClicked() }
            // ## DeleteTag
            is HomeEvent.DeleteTagButtonClicked -> { deleteTagButtonClicked() }
            // ## DeletePoint
            is HomeEvent.DeletePointButtonClicked -> { deletePointButtonClicked() }
            // ## EditTagName
            is HomeEvent.EditTagName -> { editTagName(event.value) }
            is HomeEvent.EditTagNameButtonClicked -> { editTagNameButtonClicked() }
            // ## EditPointName
            is HomeEvent.EditPointName -> { editPointName(event.value) }
            is HomeEvent.EditPointNameButtonClicked -> { editPointNameButtonClicked() }
            // ## EditPointSTags
            is HomeEvent.RemoveTag -> { removeTag(event.tag) }
            is HomeEvent.AddTag -> { addTag(event.tag) }
        }
    }

    private fun pointMapClicked(point: PointWithTags) {
        _uiState.update { currentState ->
            currentState.copy(markerType = MapMarker.MarkerPointWithTags(point))
        }
    }
    private fun tagMapClicked(tag: MapTagEntity) {
        viewModelScope.launch {
            mapRepository.getTagWithPointsWithTagsById(tag.id).collect {
                _uiState.update { currentState ->
                    currentState.copy(markerType = MapMarker.MarkerTagWithPointsWithTags(it))
                }
            }
        }
    }
    private fun addTagDialogToggle() {
        if(uiState.value.addOrEditEntity !is AddOrEditEntity.None) {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.None)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.AddTag(text = "", selectedColor = SelectedColor.Red))
            }
        }
    }
    private fun editPointSTagsBottomSheetToggle(point: MapPointEntity?) {
        if(point == null) {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.None)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.EditPointSTags(point = point))
            }
        }
    }
    private fun editPointNameDialogToggle(point: MapPointEntity?) {
        if(point == null) {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.None)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    addOrEditEntity = AddOrEditEntity.EditPointName(
                        point = point,
                        text = point.name
                    )
                )
            }
        }
    }
    private fun deletePointDialogToggle(point: MapPointEntity?) {
        if(point == null) {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.None)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.DeletePoint(point))
            }
        }
    }
    private fun deleteTagDialogToggle(tag: MapTagEntity?) {
        if(tag == null) {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.None)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.DeleteTag(tag))
            }
        }
    }
    private fun editTagNameDialogToggle(tag: MapTagEntity?) {
        if(tag == null) {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.None)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.EditTagName(tag = tag, text = tag.name))
            }
        }
    }
    private fun mapClicked(latLng: LatLng?) {
        if(latLng == null) {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.None)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(addOrEditEntity = AddOrEditEntity.AddPoint(
                    text = "", //地名にしたい
                    latLng = latLng,
                    unSelectedTags = uiState.value.tagWithPoints.map { it.tag }
                ))
            }
        }
    }
    private fun drawerMenuChanged(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(dropDownMenuLocationDisplay = value)
        }
    }
    private fun newTagTextChanged(value: String) {
        _uiState.update { currentState ->
            val state = currentState.addOrEditEntity as AddOrEditEntity.AddTag
            currentState.copy(addOrEditEntity = state.copy(text = value))
        }
    }
    private fun colorPickerBottomSheetToggle() {
        _uiState.update { currentState ->
            val state = currentState.addOrEditEntity as AddOrEditEntity.AddTag
            currentState.copy(addOrEditEntity = state.copy(colorPicker = !state.colorPicker))
        }
    }
    private fun colorChanged(color: SelectedColor) {
        _uiState.update { currentState ->
            val state = currentState.addOrEditEntity as AddOrEditEntity.AddTag
            currentState.copy(addOrEditEntity = state.copy(selectedColor = color))
        }
        colorPickerBottomSheetToggle()
    }
    private fun addNewTagButtonClicked() {
        val state = uiState.value.addOrEditEntity as? AddOrEditEntity.AddTag ?: return
        viewModelScope.launch {
            mapRepository.insertMapTag(
                MapTagEntity(
                    name = state.text,
                    color = state.selectedColor.color,
                    description = ""
                )
            )
        }
        addTagDialogToggle()
    }
    private fun pointNameChanged(value: String) {
        _uiState.update { currentState ->
            val state = currentState.addOrEditEntity as AddOrEditEntity.AddPoint
            currentState.copy(addOrEditEntity = state.copy(text = value))
        }
    }
    private fun selectedTagClicked(tag: MapTagEntity) {
        _uiState.update { currentState ->
            val state = currentState.addOrEditEntity as AddOrEditEntity.AddPoint
            currentState.copy(
                addOrEditEntity = state.copy(
                    selectedTags = state.selectedTags.minus(tag),
                    unSelectedTags = state.unSelectedTags.plus(tag)
                )
            )
        }
    }
    private fun unSelectedTagClicked(tag: MapTagEntity) {
        _uiState.update { currentState ->
            val state = currentState.addOrEditEntity as AddOrEditEntity.AddPoint
            currentState.copy(
                addOrEditEntity = state.copy(
                    selectedTags = state.selectedTags.plus(tag),
                    unSelectedTags = state.unSelectedTags.minus(tag)
                )
            )
        }
    }
    private fun addNewPointButtonClicked() {
        viewModelScope.launch {
            val state = uiState.value.addOrEditEntity as AddOrEditEntity.AddPoint
            val newId = mapRepository.insertMapPoint(
                MapPointEntity(
                    name = state.text,
                    description = "",
                    latLng = state.latLng,
                )
            )
            state.selectedTags.forEach { tag ->
                mapRepository.insertTagPointRef(
                    TagPointRef(
                        tagId = tag.id,
                        pointId = newId
                    )
                )
            }
        }
        mapClicked(null)
    }
    private fun deleteTagButtonClicked() {
        val state = uiState.value.addOrEditEntity as AddOrEditEntity.DeleteTag
        viewModelScope.launch {
            mapRepository.deleteMapTag(state.tag)
            deleteTagDialogToggle(null)
        }
    }
    private fun deletePointButtonClicked() {
        val state = uiState.value.addOrEditEntity as AddOrEditEntity.DeletePoint
        viewModelScope.launch {
            mapRepository.deleteMapPoint(state.point)
            deletePointDialogToggle(null)
        }
    }
    private fun editTagName(value: String) {
        _uiState.update { currentState ->
            val state = currentState.addOrEditEntity as AddOrEditEntity.EditTagName
            currentState.copy(addOrEditEntity = state.copy(text = value))
        }
    }
    private fun editTagNameButtonClicked() {
        val state = uiState.value.addOrEditEntity as AddOrEditEntity.EditTagName
        viewModelScope.launch {
            mapRepository.updateMapTag(
                state.tag.copy(name = state.text)
            )
            editTagNameDialogToggle(null)
        }
    }
    private fun editPointName(value: String) {
        _uiState.update { currentState ->
            val state = currentState.addOrEditEntity as AddOrEditEntity.EditPointName
            currentState.copy(addOrEditEntity = state.copy(text = value))
        }
    }
    private fun editPointNameButtonClicked() {
        val state = uiState.value.addOrEditEntity as AddOrEditEntity.EditPointName
        viewModelScope.launch {
            mapRepository.updateMapPoint(
                state.point.copy(name = state.text)
            )
            editPointNameDialogToggle(null)
        }
    }
    private fun removeTag(tag: MapTagEntity) {
        val state = uiState.value.addOrEditEntity as AddOrEditEntity.EditPointSTags
        viewModelScope.launch {
            mapRepository.deleteTagPointRef(
                TagPointRef(
                    tagId = tag.id,
                    pointId = state.point.id
                )
            )
        }
    }
    private fun addTag(tag: MapTagEntity) {
        val state = uiState.value.addOrEditEntity as AddOrEditEntity.EditPointSTags
        viewModelScope.launch {
            mapRepository.insertTagPointRef(
                TagPointRef(
                    tagId = tag.id,
                    pointId = state.point.id
                )
            )
        }
    }
}
