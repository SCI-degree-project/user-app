package com.userapp.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.userapp.viewmodel.ar.Product3DModelViewModel
import com.userapp.viewmodel.ar.Product3DModelViewModelFactoryProvider
import dagger.hilt.android.EntryPointAccessors
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.model.engine
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberView

@Composable
fun Model3DScreen(
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
        factory.create(productId)
    }

    val modelUrl by viewModel.modelUrl.collectAsState()

    val engine = rememberEngine()
    val view = rememberView(engine)
    val modelLoader = rememberModelLoader(engine)
    val cameraNode = rememberCameraNode(engine)
    val childNodes = remember { mutableStateListOf<Node>() }
    val isModelLoaded = remember { mutableStateOf(false) }

    LaunchedEffect(modelUrl) {
        val url = modelUrl
        if (!url.isNullOrBlank()) {
            modelLoader.loadModelInstanceAsync(url) { modelInstance ->
                if (modelInstance != null) {
                    val modelNode = ModelNode(
                        modelInstance = modelInstance,
                        scaleToUnits = 1.0f,
                    ).apply {
                        isScaleEditable = false
                        isPositionEditable = false
                        isRotationEditable = true
                    }
                    childNodes += modelNode
                    isModelLoaded.value = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Scene(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            view = view,
            cameraNode = cameraNode,
            childNodes = childNodes,
        )

        if (!isModelLoaded.value) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        IconButton(
            onClick = {navController.popBackStack()},
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
