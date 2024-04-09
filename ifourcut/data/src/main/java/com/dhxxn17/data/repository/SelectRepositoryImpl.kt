package com.dhxxn17.data.repository

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.dhxxn17.data.datasource.SelectLocalDataSource
import com.dhxxn17.data.mapper.asData
import com.dhxxn17.data.mapper.asDomain
import com.dhxxn17.domain.model.SelectedData
import com.dhxxn17.domain.repository.SelectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class SelectRepositoryImpl @Inject constructor(
    private val dataSource: SelectLocalDataSource
): SelectRepository {

    override val selectDataFlow: SharedFlow<SelectedData>
        get() = dataSource.selectDataFlow
            .map {  it.asDomain() }
            .shareIn(
                scope = CoroutineScope(Dispatchers.IO),
                replay = 1,
                started = SharingStarted.WhileSubscribed()
            )

    override suspend fun saveCharacterData(type: String, image: Drawable) {
        val selectedCharacter = SelectedData(type, image, null)
        dataSource.updateSelectData(selectedCharacter.asData())
    }

    override suspend fun saveUserImage(myImage: Bitmap) {
        val currentData = selectDataFlow.firstOrNull() ?: return
        val updatedCharacter = currentData.copy(myImage = myImage)
        dataSource.updateSelectData(updatedCharacter.asData())
    }

    override suspend fun getAllData(): Flow<SelectedData> {
        return selectDataFlow
    }
}