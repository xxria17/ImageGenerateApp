package com.dhxxn17.ifourcut.ui.page.upload

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class UploadScreen(
    private val navController: NavController,
    private val imageUrl: String
) : BaseScreen() {

    @Composable
    override fun CreateContent() {
        val viewModel: UploadViewModel = viewModel()
        val choosePicture = remember { mutableStateOf(CHOOSE.NONE) }
        var imageSelected = false

        var imageTypeByView by remember {
            mutableStateOf(ImageTypeForView.Upload)
        }

        val context = LocalContext.current

        val permissionsToRequest by lazy {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                    )
                }

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                    )
                }

                else -> {
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }

        // 갤러리
        val getPhotoFromGalleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null && !imageSelected) {
                Toast.makeText(context, context.resources.getString(R.string.upload_toast), Toast.LENGTH_SHORT).show()
                imageSelected = true
                val imageBitmap = uriToBitmap(context, uri)
                val imageString = bitmapToString(imageBitmap)
                val encodedUrl =
                    URLEncoder.encode(imageString, StandardCharsets.UTF_8.toString())
                navController.navigate(Screens.LoadingScreen.withImageUrl("$encodedUrl,${ImageTypeForView.Gallery.name}"))
                imageTypeByView = ImageTypeForView.Gallery
                imageSelected = false
            } else {
                imageSelected = false
//                imageTypeByView = ImageTypeForView.Upload
                Log.d("TAG", "Selected image uri is null")
            }
        }

        // 카메라
        val takePhotoFromCameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { photo ->
            if (photo != null) {
                val imagePath = saveBitmapToFile(context, photo)
                val encodedFilePath = URLEncoder.encode(imagePath, StandardCharsets.UTF_8.toString())
                navController.navigate(Screens.LoadingScreen.withImageUrl("$encodedFilePath,${ImageTypeForView.PhotoShoot.name}"))
                imageTypeByView = ImageTypeForView.PhotoShoot
            } else {
                imageTypeByView = ImageTypeForView.Upload
            }
        }

        val cameraPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                if (isGranted) {
                    // 권한이 허용되었을 때의 처리 로직
                    takePhotoFromCameraLauncher.launch()
//                    navController.navigate(Screens.CameraScreen.route)
                } else {
                    // 권한이 거부되었을 때의 처리 로직
                    Log.e("UploadScreen", "camera permission denied")
                }
            }
        )

        val requestMultiplePermissionsLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach permissionLoop@ {
                when (it.key) {
                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        if (!imageSelected) {
                            getPhotoFromGalleryLauncher.launch("image/*")
                            return@permissionLoop
                        }
                    }
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO -> {
                        if (!imageSelected) {
                            getPhotoFromGalleryLauncher.launch("image/*")
                            return@permissionLoop
                        }
                    }
                }
            }
        }


        // 카메라/갤러리 선택 했을 때 카메라/갤러리 열기
        LaunchedEffect(choosePicture.value) {
            when (choosePicture.value) {
                CHOOSE.CAMERA -> {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        // 권한이 없을 경우, 사용자에게 권한 요청
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    } else {
                        takePhotoFromCameraLauncher.launch()
//                        navController.navigate(Screens.CameraScreen.route)
                    }
                }

                CHOOSE.GALLERY -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                            requestMultiplePermissionsLauncher.launch(permissionsToRequest)
                        }else{
                            getPhotoFromGalleryLauncher.launch("image/*")
                        }
                    }else {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestMultiplePermissionsLauncher.launch(permissionsToRequest)
                        }else{
                            getPhotoFromGalleryLauncher.launch("image/*")
                        }
                    }
                }

                else -> {}
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
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
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(30.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                Spacer(modifier = Modifier.height(70.dp))

                Text(
                    text = stringResource(id = R.string.upload_title),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp,
                    color = colorResource(id = R.color.main_black),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .clickable {
                                choosePicture.value = CHOOSE.CAMERA
                            }
                            .background(Color.White.copy(alpha = 0.5f))
                            .padding(50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_camera),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(id = R.string.upload_camera),
                            fontSize = 17.sp,
                            color = colorResource(id = R.color.main_black)
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Column(
                        modifier = Modifier
                            .clickable {
                                choosePicture.value = CHOOSE.GALLERY
                            }
                            .background(Color.White.copy(alpha = 0.5f))
                            .padding(50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_gallery),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(id = R.string.upload_gallery),
                            fontSize = 17.sp,
                            color = colorResource(id = R.color.main_black)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))


                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(
                            color = colorResource(id = R.color.main_pink),
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(
                        text = "Tips",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(id = R.string.upload_tip1),
                    color = colorResource(id = R.color.main_black),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.upload_tip2),
                    color = colorResource(id = R.color.main_black),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }


    }
}

private fun checkPermissionByVersion(context: Context): Boolean {
    val permissionsToCheck = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            listOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        }

        else -> {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
    return permissionsToCheck.all { permission ->
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}

fun uriToBitmap(context: Context, uri: Uri): Bitmap {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    return BitmapFactory.decodeStream(inputStream)
}

fun bitmapToString(bitmap: Bitmap): String {
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
    val byteArray: ByteArray = baos.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): String {
    val filename = "icut_image_${System.currentTimeMillis()}.png"

    val outputStream: FileOutputStream
    val file = File(context.cacheDir, filename).apply {
        if (!exists()) createNewFile()
    }
    try {
        outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return file.absolutePath
}

enum class ImageTypeForView {
    Gallery,
    Upload,
    PhotoShoot
}

enum class CHOOSE {
    CAMERA,
    GALLERY,
    NONE
}