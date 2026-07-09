package com.office.meong.core.model.trigger

import androidx.compose.runtime.Stable
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface RefreshableTab

@Stable
class RefreshState {
    private val _refreshEvent = MutableSharedFlow<RefreshableTab>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val refreshEvent: SharedFlow<RefreshableTab> = _refreshEvent.asSharedFlow()

    suspend fun trigger(tab: RefreshableTab) {
        _refreshEvent.emit(tab)
    }
}
