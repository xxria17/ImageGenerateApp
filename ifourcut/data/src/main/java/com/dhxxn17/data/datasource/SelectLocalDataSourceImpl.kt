package com.dhxxn17.data.datasource

import com.dhxxn17.data.model.SelectData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class SelectLocalDataSourceImpl @Inject constructor(): SelectLocalDataSource {
    private val _selectDataFlow = MutableSharedFlow<SelectData>(replay = 1)
    override val selectDataFlow: SharedFlow<SelectData>
        get() = _selectDataFlow

    override suspend fun updateSelectData(update: SelectData) {
        _selectDataFlow.emit(update)
    }
}