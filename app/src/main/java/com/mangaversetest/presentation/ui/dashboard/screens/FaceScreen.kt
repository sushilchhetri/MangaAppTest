package com.mangaversetest.presentation.ui.dashboard.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.mangaversetest.R
import com.mangaversetest.presentation.ui.dashboard.viewmodel.FaceViewModel
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.mangaversetest.presentation.ui.demo.FaceDetectorComposeBuilder


@Composable
fun FaceScreen(viewModel: FaceViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var permissionRequested by remember { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        permissionRequested = true

        if (!isGranted) {
            Toast.makeText(
                context,
                context.getString(R.string.cameraPermissionDenied),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission && !permissionRequested) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.black))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            hasCameraPermission -> {
                FaceDetectorComposeBuilder(
                    context = context,
                    scope = scope,
                    isGranted = true
                ).Build(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    containerShape = RoundedCornerShape(0.dp)
                )
            }

            permissionRequested -> {
                Text(
                    text = context.getString(R.string.cameraPermissionDenied),
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = context.getString(R.string.cameraEnableInSettings),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            else -> {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

