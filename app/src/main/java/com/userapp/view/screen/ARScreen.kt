package com.userapp.view.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingState
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberView

@SuppressLint("UnrememberedMutableState")
@Composable
fun ARScreen(
    productId: String,
    navController: NavController
) {
    val context = LocalContext.current
    val hasCameraPermission = rememberCameraPermissionState(navController)

    val engine = rememberEngine()
    val cameraNode = rememberARCameraNode(engine = engine)
    val view = rememberView(engine = engine)
    val frame = remember { mutableStateOf<Frame?>(null) }

    val hasDetectedPlane = remember { mutableStateOf(false) }

    val isPlaneCurrentlyTracked = remember(frame.value) {
        isPlaneTracking(frame.value)
    }

    LaunchedEffect(isPlaneCurrentlyTracked) {
        if (isPlaneCurrentlyTracked && !hasDetectedPlane.value) {
            hasDetectedPlane.value = true
        }
    }

    if (hasCameraPermission) {
        Box(modifier = Modifier
            .fillMaxSize()
        ) {
            ARScene(
                modifier = Modifier.fillMaxSize(),
                engine = engine,
                cameraNode = cameraNode,
                view = view,
                planeRenderer = true,
                onSessionUpdated = { _, updatedFrame -> frame.value = updatedFrame },
                sessionConfiguration = { session, config ->
                    config.depthMode = if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC))
                        Config.DepthMode.AUTOMATIC else Config.DepthMode.DISABLED
                    config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                },
            )

            if (!hasDetectedPlane.value) {
                Card(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.6f)
                    )
                ) {
                    Text(
                        text = "Detecting surfaces...",
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .statusBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun rememberCameraPermissionState(navController: NavController): Boolean {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted) navController.popBackStack()
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    return hasPermission
}

fun isPlaneTracking(frame: Frame?): Boolean {
    return frame
        ?.getUpdatedTrackables(Plane::class.java)
        ?.any { it.trackingState == TrackingState.TRACKING } == true
}
