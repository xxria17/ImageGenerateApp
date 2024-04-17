package com.dhxxn17.ifourcut.ui.navigation

const val IMAGE_URL_ARG = "imageUrl"
const val TYPE_ARG = "type"

sealed class Screens(val route: String) {

    object IntroScreen: Screens("intro_screen")

    object ListScreen: Screens("list_screen")

    object PrincessScreen: Screens("princess_screen")

    object HeroScreen: Screens("hero_screen")

    object SelectScreen: Screens("select_screen/{$TYPE_ARG}") {
        fun withType(type: String): String {
            return this.route.replace(TYPE_ARG, type)
        }
    }

    object UploadScreen: Screens("upload_screen")

    object LoadingScreen: Screens("loading_screen")

    object CompleteScreen: Screens("complete_screen/")

    object CameraScreen: Screens("camera_screen")

    object CameraCompleteScreen: Screens("camera_complete_screen")
}
