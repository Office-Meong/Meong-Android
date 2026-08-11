package com.office.meong.core.network.model

import timber.log.Timber

fun <T> BaseResponse<T>.getOrThrow(): T {
    if (!success) throw ApiException(message)

    return data ?: run {
        Timber.w("BaseResponse: success=true인데 data가 null (message=$message)")
        throw ApiException(message)
    }
}
