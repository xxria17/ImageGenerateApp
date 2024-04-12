package com.dhxxn17.domain.usecase

import android.graphics.drawable.Drawable
import com.dhxxn17.domain.repository.SelectRepository
import javax.inject.Inject

class SelectCharacterDataUseCase @Inject constructor(
    private val selectRepository: SelectRepository
){
    suspend operator fun invoke(type: String, image: Drawable) {
        selectRepository.saveCharacterData(type, image)
    }
}