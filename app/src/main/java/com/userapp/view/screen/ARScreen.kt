package com.userapp.view.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.MotionEvent
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
import com.userapp.view.components.CenterCard
import com.userapp.viewmodel.Product3DModelViewModel
import com.userapp.viewmodel.Product3DModelViewModelFactoryProvider
import com.userapp.viewmodel.uistate.UiState
import dagger.hilt.android.EntryPointAccessors
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import kotlinx.coroutines.delay

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
    val hasCameraPermission = rememberCameraPermissionState(navController)

    val engine = rememberEngine()
    val cameraNode = rememberARCameraNode(engine = engine)
    val view = rememberView(engine = engine)
    val frame = remember { mutableStateOf<Frame?>(null) }
    val modelLoader = rememberModelLoader(engine = engine)
    val childNodes = rememberNodes()
    val collisionSystem = rememberCollisionSystem(view = view)

    val isModelPlaced = remember { mutableStateOf(false) }
    val hasDetectedPlane = remember { mutableStateOf(false) }

    val modelLoadState = remember { mutableStateOf<UiState<ModelNode>>(UiState.Loading) }

    val modelNodeRef = remember { mutableStateOf<ModelNode?>(null) }

    LaunchedEffect(modelUrl) {
        if (!modelUrl.isNullOrBlank()) {
            modelLoadState.value = UiState.Loading
            modelLoader.loadModelInstanceAsync(
                fileLocation = modelUrl!!,
                onResult = { instance ->
                    if (instance != null) {
                        val modelNode = ModelNode(
                            modelInstance = instance,
                            scaleToUnits = 1.0f
                        ).apply {
                            isEditable = false
                        }

                        modelNodeRef.value = modelNode
                        modelLoadState.value = UiState.Success(modelNode)
                    } else {
                        modelLoadState.value = UiState.Error("Failed to load 3D model")
                    }
                }
            )
        }
    }

    val isAnimating = remember { mutableStateOf(true) }

    LaunchedEffect(modelNodeRef.value, isAnimating.value) {
        val modelNode = modelNodeRef.value ?: return@LaunchedEffect
        while (isAnimating.value) {
            modelNode.rotation.y += 1f
            delay(16L)
        }
    }

    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()) {
            ARScene(
                modifier = Modifier.fillMaxSize(),
                engine = engine,
                cameraNode = cameraNode,
                view = view,
                planeRenderer = true,
                onSessionUpdated = { _, updatedFrame ->
                    frame.value = updatedFrame

                    if (!hasDetectedPlane.value && isPlaneTracking(updatedFrame)) {
                        hasDetectedPlane.value = true
                    }
                },
                sessionConfiguration = { session, config ->
                    config.depthMode = if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC))
                        Config.DepthMode.AUTOMATIC else Config.DepthMode.DISABLED
                    config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                },
                modelLoader = modelLoader,
                childNodes = childNodes,
                collisionSystem = collisionSystem,
                onGestureListener = rememberOnGestureListener(
                    onSingleTapConfirmed = onSingleTapConfirmed@{ e: MotionEvent, node: Node? ->
                        val modelNode = modelNodeRef.value ?: return@onSingleTapConfirmed

                        modelNode.isEditable = true
                        modelNode.isScaleEditable = false

                        if (!isModelPlaced.value && node == null) {
                            val currentFrame = frame.value ?: return@onSingleTapConfirmed
                            val hitResult = currentFrame
                                .hitTest(e.x, e.y)
                                .firstOrNull { it.isValid(depthPoint = false, point = false) }
                                ?.createAnchorOrNull()


                            hitResult?.let { anchor ->
                                val anchorNode = AnchorNode(engine = engine, anchor = anchor)
                                anchorNode.addChildNode(modelNode)
                                childNodes += anchorNode
                                isModelPlaced.value = true
                            }
                        }
                    }
                )
            )

            val overlayMessage = when {
                !hasDetectedPlane.value -> "Aim at the ground to scan it"
                !isModelPlaced.value -> "Press to place the object"
                else -> null
            }

            overlayMessage?.let {
                CenterCard(
                    message = it,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            IconButton(
                onClick = {
                    childNodes.clear()
                    isAnimating.value = false
                    navController.popBackStack()
                },
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
