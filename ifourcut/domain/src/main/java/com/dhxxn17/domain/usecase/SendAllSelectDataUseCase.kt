package com.dhxxn17.domain.usecase

import com.dhxxn17.domain.model.SelectedData
import com.dhxxn17.domain.repository.SelectRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SendAllSelectDataUseCase @Inject constructor(
    private val selectRepository: SelectRepository
) {
    suspend operator fun invoke(): SelectedData {
        return selectRepository.getAllData().first()
    }
}