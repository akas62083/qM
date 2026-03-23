package com.akas62083.qm

import android.R.attr.name
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.akas62083.qm.screens.home.HomeScreen
import com.akas62083.qm.screens.home.HomeViewModel
import com.akas62083.qm.ui.theme.QɐMTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QɐMTheme {
                Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    NavHost(
        navController = navController,
        startDestination = Route.HomeScreen,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<Route.HomeScreen>() {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QɐMTheme {
        Greeting()
    }
}