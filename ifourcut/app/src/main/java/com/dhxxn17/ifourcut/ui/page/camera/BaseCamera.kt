package com.dhxxn17.ifourcut.ui.page.camera

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.Surface
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.LifecycleOwner
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.common.saveBitmapImage
import com.dhxxn17.ifourcut.common.setStatusBarColor
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun CameraScreen(
    onBackController: () -> Unit,
    onCompleteController: (Bitmap?) -> Unit
) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraProvider = cameraProviderFuture.get()
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember {
        ImageCapture.Builder()
            .setTargetRotation(Surface.ROTATION_90)
            .build()
    }
    val isLoading = remember { mutableStateOf(false) }

    var countdown by remember { mutableStateOf(0) }
    var isCountingDown by remember { mutableStateOf(false) }

    LaunchedEffect(isCountingDown, countdown) {
        if (isCountingDown) {
            if (countdown > 0) {
                delay(1000L)
                countdown -= 1
            } else {
                isCountingDown = false
                isLoading.value = true
                takePhoto(imageCapture, context, onSuccessListener = {
                    onCompleteController.invoke(it)
                })
            }
        }
    }

    val view = LocalView.current

    SideEffect {
        setStatusBarColor(view, Color.Black)
    }

    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 16.dp, vertical = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Row(
                modifier = Modifier
                    .background(color = Color.Black.copy(alpha = 0.4f), shape = CircleShape)
                    .clickable {
                        onBackController.invoke()
                    }

            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_turnback),
                    contentDescription = "",
                    modifier = Modifier
                        .size(150.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(Color(0xffED6945), shape = CircleShape)
                    .clickable {
                        countdown = 3
                        isCountingDown = true
                        Toast.makeText(context, "카메라를 계속 응시해주세요!", Toast.LENGTH_LONG).show()
                    }
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(5.dp, Color.Black, CircleShape)
                        .background(Color(0xffED6945), shape = CircleShape)
                )
            }

            Image(
                painterResource(id = R.drawable.ic_switch),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clickable {
                        lensFacing =
                            if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK
                    },
                colorFilter = ColorFilter.tint(Color.White)
            )

        }

        Box(
            modifier = Modifier
                .aspectRatio(3f/ 4f)
                .heightIn(500.dp),
            contentAlignment = Alignment.Center
        ) {

            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                cameraProvider = cameraProvider,
                lifecycleOwner = lifecycleOwner,
                lensFacing = lensFacing,
                imageCapture = imageCapture
            )

            if (isCountingDown) {
                Text(
                    text = countdown.toString(),
                    fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                    fontSize = 180.sp,
                    color = Color.White
                )
            }

            if (isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    color = Color(0xffED6945),
                    strokeWidth = 5.dp
                )
            }
        }


    }
}


@Composable
fun CameraPreview(
    modifier: Modifier,
    cameraProvider: ProcessCameraProvider,
    lifecycleOwner: LifecycleOwner,
    lensFacing: Int,
    imageCapture: ImageCapture
) {
    val cameraSelector = remember(lensFacing) {
        CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
    }
    val preview = remember {
        Preview.Builder()
            .setTargetRotation(Surface.ROTATION_90)
            .build()
    }

    LaunchedEffect(cameraSelector) {
        cameraProvider.unbindAll()
        try {
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context)
            preview.setSurfaceProvider(previewView.surfaceProvider)
            previewView
        },
        modifier = modifier.graphicsLayer {
            scaleX = -1f
        }
    )
}

private fun takePhoto(
    imageCapture: ImageCapture,
    context: Context,
    onSuccessListener: (Bitmap?) -> Unit
) {
    val photoFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis()) + ".png"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = output.savedUri ?: Uri.fromFile(photoFile)

                val bitmap = BitmapFactory.decodeStream(
                    context.contentResolver.openInputStream(savedUri)
                )
//                val rotatedBitmap = rotateBitmap(bitmap, -90f)
                val flipedBitmap = flipHorizontally(bitmap)
//                val resizeBitmap = resizeBitmap(flipedBitmap)
                saveBitmapImage(flipedBitmap, context)
                onSuccessListener.invoke(flipedBitmap)
            }

            override fun onError(exc: ImageCaptureException) {
                Log.e("CameraXApp", "Photo capture failed: ${exc.message}", exc)
            }
        }
    )
}
private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

private fun flipHorizontally(bitmap: Bitmap): Bitmap {
    val matrix = Matrix()
    matrix.preScale(-1.0f, 1.0f)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}