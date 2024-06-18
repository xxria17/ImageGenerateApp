package com.dhxxn17.ifourcut.ui.page.upload

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dhxxn17.data.mapper.toBitmap
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.common.uriToBitmap
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect

class UploadScreen(
    private val navController: NavController
) : BaseScreen() {

    @Composable
    fun Effect(viewModel: UploadViewModel) {
        LaunchedEffect(viewModel.effect) {
            viewModel.effect.onEach { _effect ->
                when (_effect) {
                    is UploadContract.Effect.GoToLoadingScreen -> {
                        navController.navigate(Screens.LoadingScreen.route)
                    }
                }
            }.collect()
        }
    }

    @Composable
    override fun CreateContent() {
        val viewModel: UploadViewModel = hiltViewModel()
        val context = LocalContext.current
        val scrollState = rememberScrollState()

        Effect(viewModel)

        val choosePicture = remember { mutableStateOf(CHOOSE.NONE) }
        val imageSelected = remember { mutableStateOf(false) }
        val galleryLaunched = remember { mutableStateOf(false) }

        var imageTypeByView by remember {
            mutableStateOf(ImageTypeForView.Upload)
        }

        val permissionsToRequest by lazy {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                }

                Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> {
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
            if (uri != null && !imageSelected.value) {
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.upload_toast),
                    Toast.LENGTH_SHORT
                ).show()
                imageSelected.value = true
                val imageBitmap = uriToBitmap(context, uri)
                imageBitmap?.let {
                    val rotatedBitmap = rotateBitmapIfRequired(context, uri, imageBitmap)
//                    val resizeBitmap = resizeBitmap(rotatedBitmap)
                    viewModel.sendAction(UploadContract.Action.SelectImage(rotatedBitmap))
                }


                imageTypeByView = ImageTypeForView.Gallery
                imageSelected.value = false
                galleryLaunched.value = false
            } else {
                imageSelected.value = false
                galleryLaunched.value = false
//                imageTypeByView = ImageTypeForView.Upload
                Log.d("TAG", "Selected image uri is null")
            }
        }

        // 카메라
        val takePhotoFromCameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { photo ->
            if (photo != null) {
//                val resizeImage = resizeBitmap(photo)
                viewModel.sendAction(UploadContract.Action.SelectImage(photo))
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
                    navController.navigate(Screens.CameraScreen.route)
//                    takePhotoFromCameraLauncher.launch()
                } else {
                    // 권한이 거부되었을 때의 처리 로직
                    Log.e("UploadScreen", "camera permission denied")
                }
            }
        )

        val requestMultiplePermissionsLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach { (permission, isGranted) ->
                if (!isGranted || galleryLaunched.value || imageSelected.value) return@forEach
                when (permission) {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_IMAGES -> {
                        getPhotoFromGalleryLauncher.launch("image/*")
                        galleryLaunched.value = true
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
//                        takePhotoFromCameraLauncher.launch()
                        navController.navigate(Screens.CameraScreen.route)
                    }
                    choosePicture.value = CHOOSE.NONE
                }

                CHOOSE.GALLERY -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        getPhotoFromGalleryLauncher.launch("image/*")
                    } else {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            requestMultiplePermissionsLauncher.launch(permissionsToRequest)
                        } else {
                            getPhotoFromGalleryLauncher.launch("image/*")
                        }
                    }
                    choosePicture.value = CHOOSE.NONE
                }

                else -> {}
            }
        }


        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            Image(
                painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
                    .size(100.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.upload_title),
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    fontSize = 90.sp,
                    color = colorResource(id = R.color.main_black),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(70.dp))

                (viewModel.state.characterImage.value())?.toBitmap()?.asImageBitmap()?.let {
                    Image(
                        bitmap = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(300.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .clickable {
                                choosePicture.value = CHOOSE.CAMERA
                            }
                            .background(colorResource(id = R.color.main_orange).copy(alpha = 0.2f))
                            .padding(80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_camera),
                            contentDescription = null,
                            modifier = Modifier.size(150.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(id = R.string.upload_camera),
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 45.sp,
                            color = colorResource(id = R.color.main_black)
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Column(
                        modifier = Modifier
                            .clickable {
                                choosePicture.value = CHOOSE.GALLERY
                            }
                            .background(colorResource(id = R.color.main_orange).copy(alpha = 0.2f))
                            .padding(80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_gallery),
                            contentDescription = null,
                            modifier = Modifier.size(150.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(id = R.string.upload_gallery),
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 45.sp,
                            color = colorResource(id = R.color.main_black)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))


                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(
                            color = colorResource(id = R.color.main_orange),
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(
                        text = "Tips",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        lineHeight = 40.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(id = R.string.upload_tip1),
                    color = colorResource(id = R.color.main_black),
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.upload_tip2),
                    color = colorResource(id = R.color.main_black),
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp
                )
            }
        }


    }

    private fun rotateBitmapIfRequired(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        val realPath = getRealPathFromUri(context, uri)
        realPath?.let {
            val exif = ExifInterface(it)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            return when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
                else -> bitmap
            }
        }
        return bitmap
    }

//    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
//        val width = bitmap.width
//        val height = bitmap.height
//        val maxWidth = 700
//        val maxHeight = 860
//
//        val widthRatio = maxWidth.toFloat() / width
//        val heightRatio = maxHeight.toFloat() / height
//        val scaleFactor = Math.min(widthRatio, heightRatio)
//
//        val scaledWidth = (width * scaleFactor).toInt()
//        val scaledHeight = (height * scaleFactor).toInt()
//
//        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
//    }

    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        var realPath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            realPath = it.getString(columnIndex)
        }
        return realPath
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix().apply { postRotate(degree.toFloat()) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
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