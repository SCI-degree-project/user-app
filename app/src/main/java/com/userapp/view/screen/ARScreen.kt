package com.userapp.view.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.MotionEvent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import com.google.ar.core.TrackingState
import com.userapp.viewmodel.Product3DModelViewModel
import com.userapp.viewmodel.Product3DModelViewModelFactoryProvider
import dagger.hilt.android.EntryPointAccessors
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView

@SuppressLint("UnrememberedMutableState")
@Composable
fun ARScreen(
    productId: String,
    navController: NavController
) {
    val context = LocalContext.current
    val factory = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            Product3DModelViewModelFactoryProvider::class.java
        ).product3DModelViewModelFactory()
    }

    val viewModel: Product3DModelViewModel = remember {
        factory.create("3fa85f64-5717-4562-b3fc-2c963f66afa6", productId)
    }

    val modelUrl by viewModel.modelUrl.collectAsState()

    val modelUrlChecked = remember { mutableStateOf(false) }



    val hasCameraPermission = rememberCameraPermissionState(navController)

    val engine = rememberEngine()
    val cameraNode = rememberARCameraNode(engine = engine)
    val view = rememberView(engine = engine)
    val frame = remember { mutableStateOf<Frame?>(null) }
    val modelLoader = rememberModelLoader(engine = engine)
    val materialLoader = rememberMaterialLoader(engine = engine)
    val childNodes = rememberNodes()
    val collisionSystem = rememberCollisionSystem(view = view)
    val trackingFailureReason = remember { mutableStateOf<TrackingFailureReason?>(null) }
    val isModelPlaced = remember { mutableStateOf(false) }
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
                modelLoader = modelLoader,
                materialLoader = materialLoader,
                childNodes = childNodes,
                collisionSystem = collisionSystem,
                onTrackingFailureChanged = { trackingFailureReason.value = it },

                onGestureListener = rememberOnGestureListener(
                    onSingleTapConfirmed = { e: MotionEvent, node: Node? ->
                        if (!isModelPlaced.value && node == null) {
                            val hitResult = frame.value
                                ?.hitTest(e.x, e.y)
                                ?.firstOrNull { it.isValid(depthPoint = false, point = false) }
                                ?.createAnchorOrNull()

                            hitResult?.let { anchor ->
                                isModelPlaced.value = true

                                placeModelFromUrl(
                                    engine = engine,
                                    modelLoader = modelLoader,
                                    anchor = anchor,
                                    modelUrl = modelUrl!!,
                                    onReady = { anchorNode ->
                                        childNodes += anchorNode
                                    }
                                )
                            }
                        }
                    }
                )
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

fun placeModelFromUrl(
    engine: Engine,
    modelLoader: ModelLoader,
    anchor: Anchor,
    modelUrl: String,
    onReady: (AnchorNode) -> Unit
) {
    modelLoader.loadModelInstanceAsync(
        fileLocation = modelUrl,
        onResult = { modelInstance ->
            if (modelInstance != null) {
                val anchorNode = AnchorNode(engine = engine, anchor = anchor)

                val modelNode = ModelNode(
                    modelInstance = modelInstance,
                    scaleToUnits = 1.0f
                ).apply {
                    isEditable = false
                }

                anchorNode.addChildNode(modelNode)
                onReady(anchorNode)
            }
        }
    )
}
