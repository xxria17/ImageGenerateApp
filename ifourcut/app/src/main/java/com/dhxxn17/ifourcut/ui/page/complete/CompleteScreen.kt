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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.BuildConfig
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.common.generateFileName
import com.dhxxn17.ifourcut.common.saveBitmapImage
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.base.findActivity
import com.dhxxn17.ifourcut.ui.navigation.Screens
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
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

        val interactionSource = remember {
            MutableInteractionSource()
        }
        val ripple = rememberRipple(bounded = false)

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
                            .background(color = Color.Black, shape = CircleShape)
                            .indication(interactionSource, ripple)
                            .clickable {
                                navController.popBackStack(
                                    Screens.ListScreen.route,
                                    false
                                )
                            }
                            .size(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "",
                            modifier = Modifier
                                .size(27.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }


                    Spacer(modifier = Modifier.weight(1f))

                }

                Spacer(modifier = Modifier.height(24.dp))

                viewModel.state.image.value()?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth(),
//                        contentScale = ContentScale.FillWidth
                    )

                }

//                Spacer(modifier = Modifier.weight(1f))

//                BannersAds()

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 50.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Row(
                        modifier = Modifier
                            .background(color = Color(0xFF0C2081), shape = CircleShape)
                            .clickable {
                                navController.popBackStack(Screens.UploadScreen.route, false)
                            }
                            .padding(20.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_rephoto),
                            contentDescription = "",
                            modifier = Modifier
                                .size(45.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .background(color = Color(0xFF0C2081), shape = CircleShape)
                            .clickable {
                                viewModel.state.image
                                    .value()
                                    ?.let {
                                        addLogoAndSave(context, it)
                                        Toast
                                            .makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT)
                                            .show()

                                    }
                            }
                            .padding(20.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_download),
                            contentDescription = "",
                            modifier = Modifier
                                .size(45.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .background(color = Color(0xFF0C2081), shape = CircleShape)
                            .clickable {
                                viewModel.state.image
                                    .value()
                                    ?.let {
                                        viewModel.state.originFaceImg.value()?.let { _origin ->
                                            shareImage(it, context.findActivity(), _origin)
                                        }
                                    }
                            }
                            .padding(20.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = "",
                            modifier = Modifier
                                .size(45.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }


                }


            }
        }

    }

    @Composable
    fun BannersAds() {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = BuildConfig.ADMOB_AD_BANNER_ID_TEST
                    loadAd(AdRequest.Builder().build())
                }
            },
            update = { adView ->
                adView.loadAd(AdRequest.Builder().build())
            }
        )
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
        val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.aizac_logo)

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
        val scaledLogo = Bitmap.createScaledBitmap(logoBitmap, logoWidth.toInt(), logoHeight.toInt(), false)

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
        // 로고 이미지 로드
        val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.aizac_logo)

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
        val scaledLogo = Bitmap.createScaledBitmap(logoBitmap, logoWidth.toInt(), logoHeight.toInt(), false)

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

        // 원본 이미지 크기의 1/5 크기로 동그란 이미지 만들기
        val circularScaleFactor = 2
        val circularMaxWidth = imageWidth / circularScaleFactor
        val circularMaxHeight = imageHeight / circularScaleFactor
        var circularWidth: Float = circularMaxWidth.toFloat()
        var circularHeight: Float = circularMaxWidth.toFloat()

        // 원본 비율 유지하면서 크기 조정
        if (circularHeight > circularMaxHeight) {
            circularHeight = circularMaxHeight.toFloat()
            circularWidth = circularMaxHeight.toFloat()
        }

        val scaledCircularImage = Bitmap.createScaledBitmap(origin, circularWidth.toInt(), circularHeight.toInt(), false)
        val circularImageBitmap = getCircularBitmap(scaledCircularImage)

        // 동그란 이미지 위치 계산 (왼쪽 하단)
        val circularPaddingBottom = 150
        val circularPaddingLeft = 20
        val circularImageX = circularPaddingLeft
        val circularImageY = imageHeight - circularImageBitmap.height - circularPaddingBottom

        // 동그란 이미지 그리기
        canvas.drawBitmap(circularImageBitmap, circularImageX.toFloat(), circularImageY.toFloat(), null)

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