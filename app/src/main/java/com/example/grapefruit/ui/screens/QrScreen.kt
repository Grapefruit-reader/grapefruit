package com.example.grapefruit.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.grapefruit.data.handleResponse
import com.example.grapefruit.data.viewmodel.WriteVoteViewModel
import com.example.grapefruit.model.User
import com.example.grapefruit.navigation.Routes
import com.example.grapefruit.utils.BarcodeAnalyser
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import java.util.concurrent.Executors

@androidx.camera.core.ExperimentalGetImage
@Composable
fun QrScreen(
    navController: NavController,
    writeVoteViewModel: WriteVoteViewModel = hiltViewModel(),
) {
    var code by remember { mutableStateOf("") }
    val voteResult by writeVoteViewModel.voteWriting.collectAsState()
    var user: User? = null
    val context = LocalContext.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    val startNewActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        navController.navigate(Routes.HOME_SCREEN)
    }

    fun readUserData(rawData: String) {
        val params = rawData.split(";")
        if (params.size != 2 && params[1].toDoubleOrNull() == null) {
            return
        }
        code = rawData

        user = User(params[0], params[1].toDouble())
    }

    fun vote(vote: String) {
        try{

        readUserData(code)
        }catch (_: Exception){
            Toast.makeText(context, "Not a voting QR code!", Toast.LENGTH_SHORT).show()
            return
        }
        when (vote) {
            "Yes" -> writeVoteViewModel.writeYes(user!!.name, user!!.share)
            "No" -> writeVoteViewModel.writeNo(user!!.name, user!!.share)
            "Resides" -> writeVoteViewModel.writeResides(user!!.name, user!!.share)
        }
        handleResponse(voteResult,
            onSuccess = {
                code = ""
            },
            onError = { error ->
                when (error) {
                    is UserRecoverableAuthIOException -> startNewActivityLauncher.launch(error.intent)
                    else -> {
                        Log.d("InitTab", error.toString())
                    }
                }
            },
            onInitial = {
                code = ""
            }
        )


    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (hasCameraPermission) {
            Column {
                AndroidView(
                    { context ->
                        val cameraExecutor = Executors.newSingleThreadExecutor()
                        val previewView = PreviewView(context).also {
                            it.scaleType = PreviewView.ScaleType.FILL_CENTER
                        }
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                        cameraProviderFuture.addListener({
                            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                            val preview = Preview.Builder()
                                .build()
                                .also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }

                            val imageCapture = ImageCapture.Builder().build()

                            val imageAnalyzer = ImageAnalysis.Builder()
                                .build()
                                .also {
                                    it.setAnalyzer(cameraExecutor, BarcodeAnalyser {
                                        code = it
                                    })
                                }
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    context as ComponentActivity,
                                    cameraSelector,
                                    preview,
                                    imageCapture,
                                    imageAnalyzer
                                )
                            } catch (exc: Exception) {
                                Log.e("DEBUG", "Use case binding failed", exc)
                            }
                        }, ContextCompat.getMainExecutor(context))
                        previewView
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.height(20.dp))
                if (code == "") {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { navController.popBackStack() }
                        ) {
                            Text(text = "End topic")
                        }

                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (code != "") {
                        Text(text = code)
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { vote("Yes") },
                                modifier = Modifier.size(100.dp, 50.dp)
                            ) {
                                Text("Yes")
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Button(
                                onClick = { vote("No") },
                                modifier = Modifier.size(100.dp, 50.dp)
                            ) {
                                Text("No")
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Button(
                                onClick = { vote("Resides") },
                                modifier = Modifier.size(100.dp, 50.dp)
                            ) {
                                Text("Resides")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        else{

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.popBackStack() }
                    ) {
                        Text(text = "Back")
                    }
                }

        }

    }



}
