package com.dhxxn17.data.datasource

import com.dhxxn17.data.model.SelectData
import kotlinx.coroutines.flow.SharedFlow

interface SelectLocalDataSource {
    val selectDataFlow: SharedFlow<SelectData>

    suspend fun updateSelectData(update: SelectData)
}