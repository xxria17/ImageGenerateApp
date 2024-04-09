package com.dhxxn17.domain.usecase

import android.graphics.Bitmap
import com.dhxxn17.domain.repository.SelectRepository
import javax.inject.Inject

class SelectUserDataUseCase @Inject constructor(
    private val selectRepository: SelectRepository
) {
    suspend operator fun invoke(myImage: Bitmap) {
        selectRepository.saveUserImage(myImage)
    }
}