package com.office.meong.core.model.trigger

import androidx.compose.runtime.Immutable

@Immutable
data class SnackbarState(
    val message: String = "",
    val bottomPadding: Int = 30
)
