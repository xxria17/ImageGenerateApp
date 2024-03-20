package com.dhxxn17.ifourcut.ui.page.upload

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class UploadScreen(
    private val navController: NavController,
    private val imageUrl: String
) : BaseScreen() {

    @Composable
    override fun CreateContent() {
        val viewModel: UploadViewModel = viewModel()
        val showDialog = remember { mutableStateOf(false) }
        val choosePicture = remember { mutableStateOf(CHOOSE.NONE) }

        val imageUrl by remember {
            mutableStateOf(imageUrl)
        }

        val galleryImage by remember {
            mutableStateOf(viewModel.state.galleryImage)
        }

        val pictureImage by remember {
            mutableStateOf(viewModel.state.cameraImage)
        }

        var imageTypeByView by remember {
            mutableStateOf(ImageTypeForView.Upload)
        }

        LaunchedEffect(viewModel.state.galleryImage) {
            galleryImage.setValue(viewModel.state.galleryImage.value())
        }

        LaunchedEffect(viewModel.state.cameraImage) {
            pictureImage.setValue(viewModel.state.cameraImage.value())
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
            if (uri != null) {
                viewModel.sendAction(UploadContract.Action.SetGalleryImage(uri))
                imageTypeByView = ImageTypeForView.Gallery
            } else {
                imageTypeByView = ImageTypeForView.Upload
            }
        }

        // 카메라
        val takePhotoFromCameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { photo ->
            if (photo != null) {
                val baos = ByteArrayOutputStream()
                photo.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    baos
                )
                val byteArray = baos.toByteArray()
                val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
                viewModel.sendAction(UploadContract.Action.SetCameraImage(encoded))
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
                } else {
                    // 권한이 거부되었을 때의 처리 로직
                    Log.e("UploadScreen", "camera permission denied")
                }
            }
        )

        val requestMultiplePermissionsLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                when (it.key) {
                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        getPhotoFromGalleryLauncher.launch("image/*")
                    }

                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO -> {
                        getPhotoFromGalleryLauncher.launch("image/*")
                    }
                }
            }
        }



        when (choosePicture.value) {
            CHOOSE.CAMERA -> {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    // 권한이 없을 경우, 사용자에게 권한 요청
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                } else {
                    takePhotoFromCameraLauncher.launch()
                }
            }
            CHOOSE.GALLERY -> {
                if (checkPermissionByVersion(context = context).not()) {
                    requestMultiplePermissionsLauncher.launch(permissionsToRequest)
                } else {
                    getPhotoFromGalleryLauncher.launch("image/*")
                }
            }
            else -> {}
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
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
                            navController.popBackStack()
                        }
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Generate",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xffFF9800),
                    modifier = Modifier.clickable {
                        val gallery = galleryImage.value()
                        val camera = pictureImage.value()
                        val combine = imageUrl
                        val encodedUrl =
                            URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString())
                        navController.navigate(
                            Screens.CompleteScreen.withImageUrl(
                                encodedUrl
                            )
                        )
                    }
                )
            }

            when(imageTypeByView) {
                ImageTypeForView.PhotoShoot -> {
                    val decodedImage = decodeBase64ToBitmap(viewModel.state.cameraImage.value())
                    decodedImage?.let { data ->
                        Image(
                            bitmap = data.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
                ImageTypeForView.Gallery -> {
                    Image(
                        painter = rememberAsyncImagePainter(galleryImage.value()),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                ImageTypeForView.Upload -> {
                    SelectImageView {
                        showDialog.value = true
                    }
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

    private fun decodeBase64ToBitmap(encodedImage: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            // Base64 문자열이 유효하지 않은 경우 예외 처리
            null
        }
    }

    @Composable
    fun SelectImageView(onClickListener: () -> Unit){
        Column(
            modifier = Modifier
                .aspectRatio(1f)
                .background(
                    color = Color(0xffFF9800).copy(alpha = 0.08f),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    onClickListener.invoke()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_upload),
                contentDescription = "",
                modifier = Modifier.size(45.dp),
                colorFilter = ColorFilter.tint(Color(0xffFF9800))
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Upload",
                fontSize = 20.sp,
                color = Color(0xffFF9800),
                fontWeight = FontWeight.SemiBold
            )
        }
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