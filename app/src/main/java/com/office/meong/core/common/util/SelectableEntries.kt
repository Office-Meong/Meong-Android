package com.office.meong.core.common.util

inline fun <reified T> selectableEntries(excluding: T): List<T> where T : Enum<T> =
    enumValues<T>().toList().minus(excluding)
