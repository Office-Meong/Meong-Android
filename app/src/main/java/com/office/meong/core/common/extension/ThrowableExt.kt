package com.office.meong.core.common.extension

import com.office.meong.core.common.model.LoadErrorHandleAction
import retrofit2.HttpException

fun Throwable.isHttpNotFound(): Boolean = this is HttpException && code() == 404

fun Throwable.isHttpUnauthorized(): Boolean = this is HttpException && (code() == 400 || code() == 401 || code() == 403)

fun Throwable.toLoadErrorHandleAction(defaultAction: LoadErrorHandleAction): LoadErrorHandleAction =
    if (isHttpNotFound()) LoadErrorHandleAction.NotFound else defaultAction
