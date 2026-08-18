package com.office.meong.core.cache

import android.util.LruCache

suspend fun <K : Any, V : Any> LruCache<K, V>.getOrFetch(key: K, fetch: suspend () -> V): V =
    get(key) ?: fetch().also { put(key, it) }
