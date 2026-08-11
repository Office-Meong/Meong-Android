package com.office.meong.core.cache

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class InMemoryCache<T> @Inject constructor() {
    private val mutex = Mutex()
    private val _data = MutableStateFlow<T?>(null)
    val data: StateFlow<T?> = _data.asStateFlow()

    suspend fun getOrFetch(fetcher: suspend () -> T): T {
        _data.value?.let { return it }
        return mutex.withLock {
            _data.value ?: fetcher().also { _data.value = it }
        }
    }

    suspend fun set(value: T) {
        mutex.withLock { _data.value = value }
    }

    suspend fun invalidate() {
        mutex.withLock { _data.value = null }
    }
}
