package com.akas62083.qm.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.google.android.gms.maps.model.StreetViewPanoramaLocation
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.streetview.StreetView
import com.google.maps.android.compose.streetview.rememberStreetViewCameraPositionState
import com.google.maps.android.ktx.MapsExperimentalFeature
import kotlinx.coroutines.launch

@OptIn(MapsExperimentalFeature::class)
@Composable
fun HomeScreen() {
    val singapore = LatLng(1.35, 103.87)
    val singaporeMarkerState = rememberMarkerState(position = singapore)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = singaporeMarkerState,
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
        Button(onClick = {
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

*/