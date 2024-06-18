package com.dhxxn17.ifourcut.ui.page.complete

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.common.generateFileName
import com.dhxxn17.ifourcut.common.saveBitmapImage
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.base.findActivity
import com.dhxxn17.ifourcut.ui.navigation.Screens
import java.io.File
import java.io.FileOutputStream

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

        if (viewModel.state.showDialog.value()) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.sendAction(CompleteContract.Action.SetShowDialog(false))
                },
                title = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                    ) {
                        Text(
                            text = "사진을 다운로드 받아보세요!",
                            fontSize = 35.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                text = {
                    Box(
                        modifier = Modifier.size(600.dp)
                            .background(Color.White)
                            .padding(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        viewModel.state.qrImage.value()?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        } ?: run {
                            CircularProgressIndicator()
                        }
                    }
                },
                confirmButton = {

                },
                dismissButton = {
                },
                containerColor = Color.White
            )
        }

        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {

            viewModel.state.image.value()?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f/ 4f)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Fit
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(30.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Column(
                    modifier = Modifier
                        .background(color = Color.Black.copy(0.3f), shape = CircleShape)
                        .clickable {
                            navController.popBackStack(
                                Screens.ListScreen.route,
                                false
                            )
                        }
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.undo),
                        contentDescription = "",
                        modifier = Modifier
                            .size(100.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }

                Column(
                    modifier = Modifier
                        .background(color = Color.Black.copy(0.3f), shape = CircleShape)
                        .clickable {
                            navController.popBackStack(Screens.UploadScreen.route, false)
                        }
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_rephoto),
                        contentDescription = "",
                        modifier = Modifier
                            .size(100.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }

                Row(
                    modifier = Modifier
                        .background(color = Color.Black.copy(0.3f), shape = CircleShape)
                        .clickable {
                            viewModel.state.image
                                .value()
                                ?.let {
                                    val logoImg = addOnlyLogo(context, it)
                                    viewModel.sendAction(
                                        CompleteContract.Action.RequestQRCode(
                                            logoImg
                                        )
                                    )
                                }

                        }
                        .padding(10.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = "",
                        modifier = Modifier
                            .size(100.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }

            }
        }

    }

    private fun shareImage(bitmap: Bitmap, activity: Activity, origin: Bitmap) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, getImageUri(activity, bitmap, origin))
            type = "image/*"
        }
        activity.startActivity(Intent.createChooser(shareIntent, "이미지 공유하기"))
    }

    private fun getImageUri(activity: Activity, bitmap: Bitmap, origin: Bitmap): Uri {
        val cachePath = File(activity.cacheDir, "images")
        cachePath.mkdirs()
        val fileName = generateFileName()
        val stream = FileOutputStream("$cachePath/${fileName}.png")
        addLogo(activity, bitmap, origin).compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()
        val imagePath = File(activity.cacheDir, "images").absolutePath
        val imageFile = File(imagePath, "${fileName}.png")
        return FileProvider.getUriForFile(
            activity,
            "${activity.packageName}.provider",
            imageFile
        )
    }

    private fun addOnlyLogo(context: Context, imageBitmap: Bitmap): Bitmap {
        // 로고 이미지 로드
        val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.aizac_logo_url)

        // 로고 비율 계산
        val logoAspectRatio = logoBitmap.width.toFloat() / logoBitmap.height.toFloat()

        // 원본 이미지 크기
        val imageWidth = imageBitmap.width
        val imageHeight = imageBitmap.height

        // 여백 크기
        val padding = 50

        // 로고 크기 계산 (이미지 크기의 1/5)
        val scaleFactor = 4
        val maxWidth = imageWidth / scaleFactor
        val maxHeight = imageHeight / scaleFactor
        var logoWidth: Float = maxWidth.toFloat()
        var logoHeight: Float = maxWidth / logoAspectRatio

        if (logoHeight > maxHeight) {
            logoHeight = maxHeight.toFloat()
            logoWidth = maxHeight * logoAspectRatio
        }

        // 로고 비트맵 스케일링
        val scaledLogo =
            Bitmap.createScaledBitmap(logoBitmap, logoWidth.toInt(), logoHeight.toInt(), false)

        // 결과 비트맵 생성
        val resultBitmap = Bitmap.createBitmap(imageWidth, imageHeight, imageBitmap.config)
        val canvas = Canvas(resultBitmap)

        // 원본 이미지 그리기
        canvas.drawBitmap(imageBitmap, 0f, 0f, null)

        // 로고 위치 계산 (오른쪽 하단)
        val logoX = imageWidth - logoWidth.toInt() - padding
        val logoY = imageHeight - logoHeight.toInt() - padding

        // 로고 그리기
        canvas.drawBitmap(scaledLogo, logoX.toFloat(), logoY.toFloat(), null)
        return resultBitmap
    }

    private fun addLogo(context: Context, imageBitmap: Bitmap, origin: Bitmap): Bitmap {
        val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.aizac_logo)

        val logoAspectRatio = logoBitmap.width.toFloat() / logoBitmap.height.toFloat()

        val imageWidth = imageBitmap.width
        val imageHeight = imageBitmap.height

        val padding = 50

        val scaleFactor = 4
        val maxWidth = imageWidth / scaleFactor
        val maxHeight = imageHeight / scaleFactor
        var logoWidth: Float = maxWidth.toFloat()
        var logoHeight: Float = maxWidth / logoAspectRatio

        if (logoHeight > maxHeight) {
            logoHeight = maxHeight.toFloat()
            logoWidth = maxHeight * logoAspectRatio
        }

        val scaledLogo =
            Bitmap.createScaledBitmap(logoBitmap, logoWidth.toInt(), logoHeight.toInt(), false)

        val resultBitmap = Bitmap.createBitmap(imageWidth, imageHeight, imageBitmap.config)
        val canvas = Canvas(resultBitmap)

        canvas.drawBitmap(imageBitmap, 0f, 0f, null)

        val logoX = imageWidth - logoWidth.toInt() - padding
        val logoY = imageHeight - logoHeight.toInt() - padding

        canvas.drawBitmap(scaledLogo, logoX.toFloat(), logoY.toFloat(), null)

        val circularScaleFactor = 2
        val circularMaxWidth = imageWidth / circularScaleFactor
        val circularMaxHeight = imageHeight / circularScaleFactor

        val widthRatio = circularMaxWidth.toFloat() / origin.width
        val heightRatio = circularMaxHeight.toFloat() / origin.height
        val scaleFactor2 = Math.min(widthRatio, heightRatio)

        val scaledWidth = (origin.width * scaleFactor2).toInt()
        val scaledHeight = (origin.height * scaleFactor2).toInt()

        val scaledCircularImage =
            Bitmap.createScaledBitmap(origin, scaledWidth, scaledHeight, false)

        val size = Math.min(scaledWidth, scaledHeight)
        val squareBitmap = Bitmap.createBitmap(scaledCircularImage, 0, 0, size, size)

        val circularImageBitmap = getCircularBitmap(squareBitmap)

        val circularPaddingBottom = 150
        val circularPaddingLeft = 20
        val circularImageX = circularPaddingLeft
        val circularImageY = imageHeight - circularImageBitmap.height - circularPaddingBottom

        canvas.drawBitmap(
            circularImageBitmap,
            circularImageX.toFloat(),
            circularImageY.toFloat(),
            null
        )

        return resultBitmap
    }

    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = 0xff424242
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        val radius = bitmap.width.coerceAtMost(bitmap.height) / 2.0f

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color.toInt()
        canvas.drawOval(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    private fun addLogoAndSave(context: Context, imageBitmap: Bitmap) {
        val resultBitmap = addOnlyLogo(context, imageBitmap)

        // 이미지 저장 함수 호출
        saveBitmapImage(resultBitmap, context)
    }

}