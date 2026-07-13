package com.office.meong.core.common.util

val <T> UiState<T>.successData: T?
    get() = (this as? UiState.Success)?.data
