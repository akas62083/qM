package com.akas62083.qm.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akas62083.qm.screens.home.conponent.DrawerScreen
import com.akas62083.qm.screens.home.conponent.dialogs_bottomsheets.AddPointBottomSheet
import com.akas62083.qm.screens.home.conponent.dialogs_bottomsheets.AddTagDialog
import com.akas62083.qm.screens.home.conponent.dialogs_bottomsheets.DeletePointDialog
import com.akas62083.qm.screens.home.conponent.dialogs_bottomsheets.DeleteTagDialog
import com.akas62083.qm.screens.home.conponent.dialogs_bottomsheets.EditPointNameDialog
import com.akas62083.qm.screens.home.conponent.dialogs_bottomsheets.EditPointSTagsBottomSheet
import com.akas62083.qm.screens.home.conponent.dialogs_bottomsheets.EditTagNameDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.ktx.MapsExperimentalFeature
import kotlinx.coroutines.launch

@OptIn(
    MapsExperimentalFeature::class,
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val markerState = rememberMarkerState() // when user click the map

    var positionChanged by remember { mutableStateOf(true) }
    LaunchedEffect(uiState.firstLatLng) {
        if(uiState.firstLatLng != null && positionChanged) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                uiState.firstLatLng!!,
                19f
            )
            positionChanged = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                DrawerScreen(
                    uiState = uiState,
                    //point not-edit
                    pointMapClicked = {
                        viewModel.onEvent(HomeEvent.PointMapClicked(it))
                        scope.launch { drawerState.close() }
                    },
                    //tag not-edit
                    tagMapClicked = {
                        viewModel.onEvent(HomeEvent.TagMapClicked(it))
                        scope.launch { drawerState.close() }
                    },
                    addTagDialogToggle = { viewModel.onEvent(HomeEvent.AddTagDialogToggle) },
                    //point edit
                    editPointSTagsBottomSheetToggle = { viewModel.onEvent(HomeEvent.EditPointSTagsBottomSheetToggle(it)) },
                    editPointNameDialogToggle = { viewModel.onEvent(HomeEvent.EditPointNameDialogToggle(it)) },
                    deletePointDialogToggle = { viewModel.onEvent(HomeEvent.DeletePointDialogToggle(it)) },
                    //tag edit
                    deleteTagDialogToggle = { viewModel.onEvent(HomeEvent.DeleteTagDialogToggle(it)) },
                    editTagNameDialogToggle = { viewModel.onEvent(HomeEvent.EditTagNameDialogToggle(it)) },
                    //
                    drawerMenuChanged = { viewModel.onEvent(HomeEvent.DrawerMenuChanged(it)) },
                )
            }
        }
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if(drawerState.isClosed) drawerState.open()
                                    else drawerState.close()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "menu"
                            )
                        }
                    },
                )
            },
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    mergeDescendants = false,
                    onMapClick = {
                        scope.launch {
                            markerState.position = it
                            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 18f))
                        }
                        viewModel.onEvent(HomeEvent.MapClicked(it))
                    },
                    onMapLoaded = {
                        Log.d("load", "hello")
                    }
                ) {
                    if(uiState.addOrEditEntity is AddOrEditEntity.AddPoint) {
                        Marker(
                            markerState
                        )
                    }
                    key(uiState.markerType) { //   important!!!
                        when (val markerType = uiState.markerType) {
                            is MapMarker.MarkerTagWithPointsWithTags -> {
                                markerType.tagWithPointsWithTags.pointWithTags.forEach { pointWithTags ->
                                    key(pointWithTags.point.id) {
                                        Marker(
                                            state = rememberMarkerState(
                                                position = pointWithTags.point.latLng
                                            )
                                        )
                                    }
                                }
                            }
                            is MapMarker.MarkerPointWithTags -> {
                                key(markerType.pointWithTags.point.id) {
                                    Marker(
                                        state = rememberMarkerState(
                                            position = markerType.pointWithTags.point.latLng
                                        )
                                    )
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
        LaunchedEffect(cameraPositionState.isMoving) {
            if(!cameraPositionState.isMoving && cameraPositionState.position.target != LatLng(0.0, 0.0)) {
                viewModel.saveLatLng(
                    LatLng(
                        cameraPositionState.position.target.latitude,
                        cameraPositionState.position.target.longitude
                    )
                )
            }
        }


        when(uiState.addOrEditEntity) {
            is AddOrEditEntity.AddTag -> {
                AddTagDialog(
                    uiState = uiState,
                    onValueChange = { viewModel.onEvent(HomeEvent.NewTagNameChanged(it)) },
                    onColorPickerBottomSheetToggle = { viewModel.onEvent(HomeEvent.ColorPickerBottomSheetToggle) },
                    onColorClicked = { viewModel.onEvent(HomeEvent.ColorChanged(it)) },
                    cancel = { viewModel.onEvent(HomeEvent.AddTagDialogToggle) },
                    confirm = { viewModel.onEvent(HomeEvent.AddNewTagButtonClicked) }
                )
            }
            is AddOrEditEntity.AddPoint -> {
                AddPointBottomSheet(
                    uiState = uiState,
                    onValueChange = { viewModel.onEvent(HomeEvent.NewPointNameChanged(it)) },
                    onSelectedTagClicked = { viewModel.onEvent(HomeEvent.SelectedTagClicked(it)) },
                    onUnSelectedTagClicked = { viewModel.onEvent(HomeEvent.UnSelectedTagClicked(it)) },
                    confirm = { viewModel.onEvent(HomeEvent.AddNewPointButtonClicked) },
                    cancel = { viewModel.onEvent(HomeEvent.MapClicked(null)) },
                )
            }
            is AddOrEditEntity.DeleteTag -> {
                DeleteTagDialog(
                    uiState = uiState,
                    cancel = { viewModel.onEvent(HomeEvent.DeleteTagDialogToggle(null)) },
                    confirm = { viewModel.onEvent(HomeEvent.DeleteTagButtonClicked) }
                )
            }
            is AddOrEditEntity.DeletePoint -> {
                DeletePointDialog(
                    uiState = uiState,
                    cancel = { viewModel.onEvent(HomeEvent.DeletePointDialogToggle(null)) },
                    confirm = { viewModel.onEvent(HomeEvent.DeletePointButtonClicked) },
                )
            }
            is AddOrEditEntity.EditTagName -> {
                EditTagNameDialog(
                    uiState = uiState,
                    onValueChange = { viewModel.onEvent(HomeEvent.EditTagName(it)) },
                    cancel = { viewModel.onEvent(HomeEvent.EditTagNameDialogToggle(null)) },
                    confirm = { viewModel.onEvent(HomeEvent.EditTagNameButtonClicked) },
                )
            }
            is AddOrEditEntity.EditPointName -> {
                EditPointNameDialog(
                    uiState = uiState,
                    onValueChange = { viewModel.onEvent(HomeEvent.EditPointName(it)) },
                    cancel = { viewModel.onEvent(HomeEvent.EditPointNameDialogToggle(null)) },
                    confirm = { viewModel.onEvent(HomeEvent.EditPointNameButtonClicked) },
                )
            }
            is AddOrEditEntity.EditPointSTags -> {
                EditPointSTagsBottomSheet(
                    uiState = uiState,
                    removeTag = { viewModel.onEvent(HomeEvent.RemoveTag(it)) },
                    addTag = { viewModel.onEvent(HomeEvent.AddTag(it)) },
                    cancel = { viewModel.onEvent(HomeEvent.EditPointSTagsBottomSheetToggle(null)) },
                )
            }
            else -> {}
        }
    }
}
