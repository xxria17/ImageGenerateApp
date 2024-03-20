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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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


        // 카메라/갤러리 선택 했을 때 카메라/갤러리 열기
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
                    text = "사진을 추가해 주세요",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp,
                    color = Color(0xff242323),
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
                            .padding(50.dp)

                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_camera),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "카메라",
                            fontSize = 17.sp,
                            color = Color(0xff242323)
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Column(
                        modifier = Modifier
                            .clickable {
                                choosePicture.value = CHOOSE.GALLERY
                            }
                            .background(Color.White.copy(alpha = 0.5f))
                            .padding(50.dp)

                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_gallery),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "갤러리",
                            fontSize = 17.sp,
                            color = Color(0xff242323)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))


                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(
                            color = Color(0xffe190aa),
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
                    text = "아래 기준에 맞는 사진을 업로드하면 \n 더욱 자연스러운 결과를 얻을 수 있어요.",
                    color = Color(0xff242323),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "- 정면에서 촬영된 사진 \n - 이마가 잘 보이는 사진 \n - 안경을 벗은 사진",
                    color = Color(0xff242323),
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