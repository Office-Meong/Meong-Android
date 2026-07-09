package com.office.meong.core.trigger

import androidx.compose.runtime.staticCompositionLocalOf
import com.office.meong.core.model.trigger.RefreshState

val LocalRefreshState = staticCompositionLocalOf<RefreshState> {
    error("RefreshState not provided")
}
