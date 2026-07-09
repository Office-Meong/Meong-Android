package com.office.meong.core.trigger

import androidx.compose.runtime.staticCompositionLocalOf
import com.office.meong.core.model.trigger.GlobalUiEventHolder

val LocalGlobalUiEventTrigger = staticCompositionLocalOf<GlobalUiEventHolder> {
    error("No GlobalUiEvent Trigger provided")
}
