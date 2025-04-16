package com.mangaversetest.presentation.ui.demo


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.mangaversetest.utils.customView.OverlayView
import com.mangaversetest.utils.helper.FaceDetectorHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FaceDetectorComposeBuilder(
    private val context: Context,
    private val scope: CoroutineScope,
    private val isGranted:Boolean
) : FaceDetectorHelper.DetectorListener {
    private val TAG = "FaceDetectorBuilder"
    private lateinit var faceDetectorHelper: FaceDetectorHelper
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var previewView: PreviewView
    private lateinit var overlayView: OverlayView
    private lateinit var containerModifier: Modifier
    private var containerShape = RectangleShape

    @Composable
    fun Build(
        modifier: Modifier = Modifier,
        containerShape: Shape = RectangleShape,
    ) {
        this.containerModifier = modifier
        this.containerShape = containerShape
        CheckCameraPermission()
    }

    @Composable
    private fun CheckCameraPermission() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)

        ) {
            AnimatedVisibility(
                visible = isGranted,
                enter = scaleIn(tween(550)),
                exit = scaleOut(tween(550))
            ) {
                OrganismFaceDetector()
            }

        }

    }

    @Composable
    private fun OrganismFaceDetector() {
        val lensFacing = CameraSelector.LENS_FACING_FRONT
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        val preview = Preview.Builder().build()
        val previewView = remember {
            PreviewView(context)
        }
        val overlayView = remember {
            OverlayView(context, null)
        }
        val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        DisposableEffect(lifecycleOwner) {
            val lifecycleObserver = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        lifecycleOwner.lifecycleScope.launch { doOnResume() }
                    }

                    Lifecycle.Event.ON_PAUSE -> {
                        lifecycleOwner.lifecycleScope.launch { doOnPause() }
                    }

                    else -> {

                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            }
        }

        LaunchedEffect(lensFacing) {
            val cameraProvider = context.getCameraProvider()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview)
            preview.setSurfaceProvider(previewView.surfaceProvider)
            integrateFaceDetector(lifecycleOwner)
            withContext(Dispatchers.Default) {
                if (faceDetectorHelper.isClosed()) {
                    faceDetectorHelper.setupFaceDetector()
                }
            }
        }

        this.previewView = previewView.apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER


        }
        this.overlayView = overlayView

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
            AndroidView(factory = { overlayView }, modifier = Modifier.fillMaxSize())
        }
    }

    private suspend fun doOnResume() {

        if (!::faceDetectorHelper.isInitialized) return
        withContext(Dispatchers.Default) {
            if (faceDetectorHelper.isClosed()) {
                faceDetectorHelper.setupFaceDetector()
            }
        }
    }

    private suspend fun doOnPause() {
        if (this::faceDetectorHelper.isInitialized) {
            // Close the face detector and release resources
            withContext(Dispatchers.Default) {
                faceDetectorHelper.clearFaceDetector()
            }
        }
    }

    private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
        suspendCoroutine { continuation ->
            ProcessCameraProvider.getInstance(this).also { cameraProvider ->
                cameraProvider.addListener({
                    continuation.resume(cameraProvider.get())
                }, ContextCompat.getMainExecutor(this))
            }
        }

    private suspend fun integrateFaceDetector(lifecycleOwner: LifecycleOwner) {
        withContext(Dispatchers.Default) {
            faceDetectorHelper = FaceDetectorHelper(
                context = context,
                threshold = 0.5f,
                currentDelegate = FaceDetectorHelper.DELEGATE_CPU,
                faceDetectorListener = this@FaceDetectorComposeBuilder,
                runningMode = RunningMode.LIVE_STREAM
            )
            previewView.post {
                setUpCamera(lifecycleOwner)
            }
        }
    }

    private fun setUpCamera(lifecycleOwner: LifecycleOwner) {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()
                bindCameraUseCases(lifecycleOwner)
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases(lifecycleOwner: LifecycleOwner) {

        val cameraProvider =
            cameraProvider
                ?: throw IllegalStateException("Camera initialization failed.")

        val cameraSelector =
            CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()

        preview =
            Preview.Builder()
                .setResolutionSelector(
                    ResolutionSelector.Builder().setAspectRatioStrategy(
                        AspectRatioStrategy(
                            AspectRatio.RATIO_4_3,
                            AspectRatioStrategy.FALLBACK_RULE_AUTO
                        )
                    ).build()
                )
                .setTargetRotation(previewView.display.rotation)
                .build()

        imageAnalyzer =
            ImageAnalysis.Builder()
                .setResolutionSelector(
                    ResolutionSelector.Builder().setAspectRatioStrategy(
                        AspectRatioStrategy(
                            AspectRatio.RATIO_4_3,
                            AspectRatioStrategy.FALLBACK_RULE_AUTO
                        )
                    ).build()
                )
                .setTargetRotation(previewView.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                .also {
                    it.setAnalyzer(
                        Executors.newSingleThreadExecutor(),
                        faceDetectorHelper::detectLivestreamFrame
                    )
                }

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )

            preview?.setSurfaceProvider(previewView.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    override fun onError(error: String, errorCode: Int) {
        scope.launch(Dispatchers.Main) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResults(resultBundle: FaceDetectorHelper.ResultBundle) {
        scope.launch(Dispatchers.Main) {
            if (::overlayView.isInitialized) {
                val detectionResult = resultBundle.results[0]
                overlayView.setResults(
                    detectionResult,
                    resultBundle.inputImageHeight,
                    resultBundle.inputImageWidth
                )

                overlayView.invalidate()
            }
        }
    }
}
