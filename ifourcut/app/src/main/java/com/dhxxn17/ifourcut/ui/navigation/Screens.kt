package com.dhxxn17.ifourcut.ui.navigation

const val IMAGE_URL_ARG = "imageUrl"

sealed class Screens(val route: String) {

    object IntroScreen: Screens("intro_screen")

    object StartScreen: Screens("start_screen")

    object SelectScreen: Screens("select_screen")

    object UploadScreen: Screens("upload_screen")

    object LoadingScreen: Screens("loading_screen")

    object CompleteScreen: Screens("complete_screen/{$IMAGE_URL_ARG}") {
        fun withImageUrl(imageUrl: String): String {
            return this.route.replace(IMAGE_URL_ARG, imageUrl)
        }
    }

    object CameraScreen: Screens("camera_screen")
}
