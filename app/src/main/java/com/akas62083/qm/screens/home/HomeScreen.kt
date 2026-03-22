package com.akas62083.qm.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

@OptIn(MapsExperimentalFeature::class)
@Composable
fun HomeScreen() {
    var check by remember { mutableStateOf(false) }
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    val singaporeGroundOverlayPosition = GroundOverlayPosition.create(
        singapore, 0f, 30f
    )
    val tileOverlayState = rememberTileOverlayState()
    LaunchedEffect(check) {
        if(check) {
        } else {
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            mergeDescendants = false
        ) {
        }
        Button(onClick = {
            check = !check
        }) {
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