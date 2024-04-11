package com.dhxxn17.ifourcut.ui.page.complete

import android.app.Activity
import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CompleteScreen (
    private val navController: NavController,
): BaseScreen() {

    @Composable
    override fun CreateContent() {
        val viewModel: CompleteViewModel = hiltViewModel()
        val context = LocalContext.current


        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
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
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                //TODO
                                navController.popBackStack()
                                navController.popBackStack()
                            }
                    )
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

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 24.dp,
                            bottom = 36.dp
                        )
                ) {
                    Button(
                        onClick = {
                            viewModel.state.image.value()?.let {
                                saveBitmapImage(it, context)
                                Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor= colorResource(id = R.color.main_white),
                            contentColor= colorResource(id = R.color.main_white),
                            disabledContainerColor= colorResource(id = R.color.main_white),
                            disabledContentColor= colorResource(id = R.color.main_white),
                        )

                    ) {
                        Text(
                            text = stringResource(id = R.string.complete_save),
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.main_pink),
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            viewModel.state.image.value()?.let {
                                shareImage(it, context.findActivity())
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor= colorResource(id = R.color.main_pink),
                            contentColor= colorResource(id = R.color.main_pink),
                            disabledContainerColor= colorResource(id = R.color.main_white),
                            disabledContentColor= colorResource(id = R.color.main_white),
                        )

                    ) {
                        Text(
                            text = stringResource(id = R.string.complete_share),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

    }

    private fun shareImage(bitmap: Bitmap, activity: Activity) {
        val shareIntent = Intent().apply{
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
            val imageFileFolder = File(Environment.getExternalStorageDirectory().toString() + '/' + generateFileName())
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

}