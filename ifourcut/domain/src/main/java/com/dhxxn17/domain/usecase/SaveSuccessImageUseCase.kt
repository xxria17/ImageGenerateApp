package com.dhxxn17.domain.usecase

import com.dhxxn17.domain.repository.SelectRepository
import javax.inject.Inject

class SaveSuccessImageUseCase @Inject constructor(
    private val selectRepository: SelectRepository
){
    suspend operator fun invoke(image: ByteArray?) {
        selectRepository.saveCompleteImage(image)
    }
}