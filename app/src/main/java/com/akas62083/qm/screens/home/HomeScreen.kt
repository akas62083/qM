package com.akas62083.qm.screens.home

import android.R.attr.contentDescription
import android.R.attr.onClick
import android.R.attr.tag
import android.R.attr.text
import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import android.util.Log.v
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.checkScrollableContainerConstraints
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.akas62083.qm.screens.home.dialogs.AddTagDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.StreetViewPanoramaLocation
import com.google.android.gms.maps.model.TileProvider
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GroundOverlay
import com.google.maps.android.compose.GroundOverlayPosition
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberTileOverlayState
import com.google.maps.android.compose.streetview.StreetView
import com.google.maps.android.compose.streetview.rememberStreetViewCameraPositionState
import com.google.maps.android.ktx.MapsExperimentalFeature
import kotlinx.coroutines.launch
import java.util.Locale

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
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) // ドロワーの開閉
    var dropDownMenuExpanded by remember { mutableStateOf(false) } //地点一覧表示かタグ一覧表示かを指定する四角い小さいやつ
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Box {
                        Row(
                            modifier = Modifier.height(IntrinsicSize.Min)
                                .clickable(
                                    interactionSource = null,
                                    indication = null
                                ) { dropDownMenuExpanded = !dropDownMenuExpanded },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if(uiState.dropDownMenuLocationDisplay) "地点一覧" else "タグ一覧",
                                modifier = Modifier.padding(16.dp),
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Box(modifier = Modifier.aspectRatio(1f).padding(15.dp).fillMaxSize()) {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = dropDownMenuExpanded,
                            onDismissRequest = { dropDownMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text("地点一覧")
                                },
                                onClick = {
                                    viewModel.onEvent(HomeEvent.DropDownMenuDisplayChange(true))
                                    dropDownMenuExpanded = false
                                },
                            )
                            DropdownMenuItem(
                                text = {
                                    Text("タグ一覧")
                                },
                                onClick = {
                                    viewModel.onEvent(HomeEvent.DropDownMenuDisplayChange(false))
                                    dropDownMenuExpanded = false
                                },
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(10.dp))
                    if(uiState.dropDownMenuLocationDisplay) {
                        if(uiState.pointWithTags.isNotEmpty()) {
                            uiState.pointWithTags.forEach {
                                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                                    Text(it.point.name)
                                    it.tags.forEach { tag ->
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = tag.name,
                                            modifier = Modifier.padding(10.dp)
                                                .background(
                                                    color = tag.color,
                                                )
                                        )
                                    }
                                }
                            }
                        } else {
                            Text("地点がありません >_<")
                        }
                    } else {
                        if(uiState.tagWithPoints.isNotEmpty()) {
                            uiState.tagWithPoints.forEach {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                    Text(
                                        text = it.tag.name,
                                        modifier = Modifier.background(it.tag.color, CutCornerShape(5.dp))
                                            .padding(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.fillMaxHeight()) {
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(it.points.size.toString() + "個のポイント")
                                    }
                                }
                            }
                        } else {
                            Text("タグがありません >_<")
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Button(
                                onClick = { viewModel.onEvent(HomeEvent.OpenOrCloseAddTagDialog) },
                                shape = CutCornerShape(5.dp)
                            ) {
                                Text(text = "タグを追加")
                            }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                TopAppBar(
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
                    title = {
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.height(80.dp).aspectRatio(1f).height(IntrinsicSize.Min),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier.padding(10.dp).fillMaxSize(),
                        imageVector = Icons.Default.Add,
                        contentDescription = "add fab"
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = rememberCameraPositionState(),
                    mergeDescendants = false,
                    onMapClick = {
                        viewModel.onEvent(HomeEvent.ClickedMap(it))
                    }
                ) {
                }
            }
        }
        if(uiState.isAddTagDialogOpened) {
            AddTagDialog(
                uiState = uiState,
                cancel = { viewModel.onEvent(HomeEvent.OpenOrCloseAddTagDialog) },
                openOrCloseColorPickBottomSheet = { viewModel.onEvent(HomeEvent.OpenOrCloseColorPickBottomSheet) },
                clickedColor = { viewModel.onEvent(HomeEvent.ClickedColor(it)) },
                onValueChange = { viewModel.onEvent(HomeEvent.ChangeTextFieldValueInAddTagDialog(it)) },
                confirm = { viewModel.onEvent(HomeEvent.ClickedConfirmButton) }
            )
        }
        if(uiState.selectedLatLng != null) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.onEvent(HomeEvent.ClickedMap(null)) },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
                modifier = Modifier.fillMaxHeight().fillMaxWidth()
            ) {
                SharedTransitionLayout {
                    Column {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.selectedLatLng!!.latitude.toString() + ", " + uiState.selectedLatLng!!.longitude.toString(),
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = uiState.pointName,
                            onValueChange = { viewModel.onEvent(HomeEvent.ChangeMapPointName(it)) },
                            label = { Text("名前") },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedContent(
                            targetState = uiState.selectedTags,
                            label = "tag-list"
                        ) { tags ->
                            FlowRow(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 30.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Black,
                                        shape = RoundedCornerShape(5.dp)
                                    )
                            ) {
                                tags.forEach {
                                    Card(
                                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                                        modifier = Modifier.padding(10.dp)
                                            .sharedElement(
                                                rememberSharedContentState(key = "tag-${it.name}"),
                                                animatedVisibilityScope = this@AnimatedContent,
                                                boundsTransform = { _, _ ->
                                                    spring(
                                                        dampingRatio = 0.8f,
                                                        stiffness = 380f
                                                    )
                                                }
                                            )
                                            .clickable { viewModel.onEvent(HomeEvent.ClickedTagToUnSelected(it)) }
                                    ) {
                                        Text(
                                            text = it.name,
                                            modifier = Modifier.background(
                                                it.color,
                                                CutCornerShape(5.dp)
                                            )
                                                .padding(10.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        AnimatedContent(
                            targetState = uiState.notSelectedTags,
                            label = "tag-lists"
                        ) { tags ->
                            FlowRow(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 30.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(5.dp)
                                    )
                            ) {
                                tags.forEach {
                                    Card(
                                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                                        modifier = Modifier.padding(10.dp)
                                            .sharedElement(
                                                rememberSharedContentState(key = "tag-${it.name}"),
                                                animatedVisibilityScope = this@AnimatedContent,
                                                boundsTransform = { _, _ ->
                                                    spring(
                                                        dampingRatio = 0.8f,
                                                        stiffness = 380f
                                                    )
                                                }
                                            )
                                            .clickable { viewModel.onEvent(HomeEvent.ClickedTag( it )) }
                                    ) {
                                        Text(
                                            text = it.name,
                                            modifier = Modifier.background(
                                                it.color,
                                                CutCornerShape(5.dp)
                                            )
                                                .padding(10.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Button(
                                onClick = {
                                    viewModel.onEvent(HomeEvent.SavePoint)
                                }
                            ) {
                                Text("保存")
                            }
                        }
                    }
                }
            }
        }
    }
}



/* learning note

## Class
- class CameraPositionState :
    マップのカメラ状態を制御及監視するオブジェクト
    GoogleMap()の引数
    position: CameraPosition カメラの位置
        CameraPosition.fromLatLngZoom(target: LatLng, zoom: Float)
        CameraPosition.builder()でtitleやbearingを指定できる
        (こういうのをファクトリメソッドという。)
    isMoving: Boolean カメラが動いているかどうか
    cameraMoveStartedReason: CameraMoveStartedReason カメラが動いている理由
    sus animate(update: CameraUpdate, durationMs: Int = 1000) カメラをアニメーションで動かす
        update = CameraUpdateFactory.newLatLngZoom(singapore, 100f)
    sus move(update: CameraUpdate) カメラを動かす

- class MapUiSettings :
    マップ上のUI関連設定のためのクラス
    GoogleMap()の引数

- class MapProperties :
    マップ上で表示可能なプロパティを表示
    GoogleMap()の引数

- class GroundOverlayPosition :
    GroundOverlayの位置
    GroundOverlay()の引数
    height: Float
    width: Float
    latLngBounds: LatLngBounds
    latLng: LatLng

- class MarkerState :
    マーカーの位置を制御及監視するオブジェクト
    Marker()系関数の引数
    .fromLatLng()や.fromBounds()といったファクトリメソッドを使用する
    isDragging: Boolean マーカーがドラッグ中かどうか(ドラッグ可能な場合に限る)
    position: LatLng マーカーの位置
    fun hideInfoWindow() マーカーのInfoWindowを非表示にする
    fun showInfoWindow() マーカーのInfoWindowを表示する

## Functions

- GoogleMap() :
    マップを表示する関数
    A compose container for a MapView.
    cameraPositionState: CameraPositionState = rememberCameraPositionState()
    properties: MapProperties = DefaultMapProperties
    uiSettings: MapUiSettings = DefaultMapUiSettings
    onMapClick: (LatLng) -> Unit
    onMapLongClick: (LatLng) -> Unit
    onMapLoaded: () -> Unit
    mergeDescendants: Boolean = false
        子要素のセマンティクス統合
        マップ要素へのフォーカス抑制

- Marker() :
- AdvancedMarker() :
    マーカー
    state: MarkerState = rememberUpdateMarkerState()

- Circle() :
    マップ上に丸を表示させる関数
    canter: LatLng
        丸の中心のLatLng
    radius: Double
        半径(メートル)

- GoogleMapFactory() :
    GoogleMapのファクトリパターンを提供する。

- GroundOverlay() :
    座標の境界に画像を表示させる。

- InputHandler() :
    地図上でユーザーが行った操作(クリック、ドラッグ、ズームなど)を検知し、特定の処理を実行する仕組み。

- Polygon() :
    points: List<LatLng> の座標を結び、塗りつぶす。
    fillColor: Color
    strokeColor: Color 線の色
    clickable: Boolean クリック可能か
    onClick: (Polygon) -> Unit

- Polyline() :
    List<LatLng> の順に線が結ばれる。
    width: Float 幅
    また他の引数は上のやつとほぼ同じ事ができる。

- TileOverlay() :
    マップ上のtile overlay
    天気図などの画像を表示させる

- rememberCameraPositionState():
    rememberSaveableを使用してCameraPositionStateを作成し、記憶する。
*/