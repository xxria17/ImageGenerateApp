package com.dhxxn17.ifourcut.ui.page.complete

import android.app.Activity
import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CompleteScreen(
    private val navController: NavController,
) : BaseScreen() {

    @Composable
    override fun CreateContent() {
        val viewModel: CompleteViewModel = hiltViewModel()
        val context = LocalContext.current
        val scrollState = rememberScrollState()

        val onBackPressedDispatcher =
            LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

        DisposableEffect(Unit) {
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.popBackStack(Screens.UploadScreen.route, false)
                }
            }
            onBackPressedDispatcher?.addCallback(callback)
            onDispose {
                callback.remove()
            }
        }


        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize(),
        ) {

            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(color = Color.White.copy(alpha = 0.5f), shape = CircleShape)
                            .clickable {
                                navController.popBackStack(
                                    Screens.StartScreen.route,
                                    false
                                )
                            }
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "",
                            modifier = Modifier
                                .size(15.dp)

                        )
                    }


                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .background(color = Color.White.copy(alpha = 0.5f), shape = CircleShape)
                            .clickable {
                                viewModel.state.image
                                    .value()
                                    ?.let {
                                        addLogoAndSave(context, it, R.drawable.aizac_logo)
//                                        saveBitmapImage(it, context)
                                        Toast
                                            .makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                            }
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_download),
                            contentDescription = "",
                            modifier = Modifier
                                .size(25.dp)

                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .background(color = Color.White.copy(alpha = 0.5f), shape = CircleShape)
                            .clickable {
                                viewModel.state.image
                                    .value()
                                    ?.let {
                                        shareImage(it, context.findActivity())
                                    }
                            }
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = "",
                            modifier = Modifier
                                .size(25.dp)

                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                viewModel.state.image.value()?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(400.dp)
                    )

                }

            }
        }

    }

    private fun shareImage(bitmap: Bitmap, activity: Activity) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, getImageUri(activity, bitmap))
            type = "image/*"
        }
        activity.startActivity(Intent.createChooser(shareIntent, "이미지 공유하기"))
    }

    private fun getImageUri(activity: Activity, bitmap: Bitmap): Uri {
        val cachePath = File(activity.cacheDir, "images")
        cachePath.mkdirs()
        val fileName = generateFileName()
        val stream = FileOutputStream("$cachePath/${fileName}.png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()
        val imagePath = File(activity.cacheDir, "images").absolutePath
        val imageFile = File(imagePath, "${fileName}.png")
        return FileProvider.getUriForFile(
            activity,
            "${activity.packageName}.provider",
            imageFile
        )
    }

    fun Context.findActivity(): Activity {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("no activity")
    }


    private fun generateFileName(): String {
        val timestamp = SimpleDateFormat("ddHHmm", Locale.getDefault()).format(Date())
        val randomString = (1..6).map { ('a'..'z').random() }.joinToString("")
        return "image_${timestamp}$randomString.png"
    }

    private fun saveBitmapImage(bitmap: Bitmap, context: Context) {
        val timestamp = System.currentTimeMillis()
        val contentResolver = context.findActivity().contentResolver
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + generateFileName())
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                try {
                    val outputStream = contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                            outputStream.close()
                        } catch (e: Exception) {
                            Log.e("CompleteScreen", "saveBitmapImage: ", e)
                        }
                    }
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    contentResolver.update(uri, values, null, null)

                } catch (e: Exception) {
                    Log.e("CompleteScreen", "saveBitmapImage: ", e)
                }
            }
        } else {
            val imageFileFolder = File(
                Environment.getExternalStorageDirectory().toString() + '/' + generateFileName()
            )
            if (!imageFileFolder.exists()) {
                imageFileFolder.mkdirs()
            }
            val mImageName = "$timestamp.png"
            val imageFile = File(imageFileFolder, mImageName)
            try {
                val outputStream: OutputStream = FileOutputStream(imageFile)
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    Log.e("CompleteScreen", "saveBitmapImage: ", e)
                }
                values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            } catch (e: Exception) {
                Log.e("CompleteScreen", "saveBitmapImage: ", e)
            }
        }
    }
    private fun addLogoAndSave(context: Context, imageBitmap: Bitmap, logoResId: Int) {
        val logoBitmap = BitmapFactory.decodeResource(context.resources, logoResId)
        val scaledLogo = Bitmap.createScaledBitmap(logoBitmap, 400, 120, false)

        val resultBitmap = Bitmap.createBitmap(imageBitmap.width, imageBitmap.height, imageBitmap.config)
        val canvas = Canvas(resultBitmap)

        canvas.drawBitmap(imageBitmap, 0f, 0f, null)

        val logoX = imageBitmap.width - 400
        val logoY = imageBitmap.height - 120
        val dstRect = Rect(logoX, logoY, logoX + 400, logoY + 120)
        canvas.drawBitmap(scaledLogo, null, dstRect, null)

        saveBitmapImage(resultBitmap, context)
    }

}