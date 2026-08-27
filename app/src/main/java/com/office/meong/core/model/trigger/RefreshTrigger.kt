package com.office.meong.core.model.trigger

import androidx.compose.runtime.Stable
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * 현재 보고 있는 탭을 다시 눌렀을 때 신호를 흘려보낸다.
 * NavHost 는 현재 탭 화면 하나만 컴포즈하므로 어느 탭인지 구분할 필요가 없어 [Unit] 을 쓴다.
 */
@Stable
class RefreshState {
    private val _events = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val events: SharedFlow<Unit> = _events.asSharedFlow()

    fun trigger() {
        _events.tryEmit(Unit)
    }
}
