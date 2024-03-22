package com.dhxxn17.ifourcut.ui.page.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.dhxxn17.ifourcut.R
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executor

@Composable
fun CameraPreviewWithCaptureButton(lifecycleOwner: LifecycleOwner, onClickListener: (Bitmap) -> Unit) {
    val context = LocalContext.current
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    val executor = ContextCompat.getMainExecutor(context)
    var imageCapture: ImageCapture? = null // 이미지 촬영 용도

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Box(
            Modifier.aspectRatio(9 / 13f)
        ) {
            val guideImage: Painter = painterResource(id = R.drawable.guide)

            CameraPreview(
                Modifier.matchParentSize(),
                cameraProviderFuture,
                executor,
                lifecycleOwner
            ) { cameraProvider ->
                imageCapture = ImageCapture.Builder().build()

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    imageCapture
                )
            }

            Image(
                painter = guideImage,
                contentDescription = "Guide Image",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Fit
            )
        }

        Button(
            shape = CircleShape,
            modifier = Modifier.padding(10.dp).size(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            onClick = {
                imageCapture?.takePicture(
                    executor,
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(image: ImageProxy) {

                            val buffer = image.planes[0].buffer
                            val bytes = ByteArray(buffer.remaining())
                            buffer.get(bytes)
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
                            onClickListener.invoke(bitmap)
                            image.close()
                        }

                        override fun onError(exception: ImageCaptureException) {
                            super.onError(exception)
                            Log.e("BaseCamera", "Error $exception")
                        }
                    }
            )
        }) {
            Text(" ")
        }
    }

}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    executor: Executor,
    lifecycleOwner: LifecycleOwner,
    onBind: (ProcessCameraProvider) -> Unit
) {
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                onBind(cameraProvider)
            }, executor)
            previewView
        },
        modifier = modifier
    )
}
