package com.office.meong.core.common.util

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * 사용자가 트리거한 새로고침 인디케이터가 너무 빨리 사라져 "깜빡"하는 것을 막는다.
 * [startedAtMs] 이후 경과가 [minMs] 미만이면 그 차이만큼만 대기한다(응답이 늦으면 대기 없음).
 */
suspend fun awaitMinDuration(startedAtMs: Long, minMs: Long = 500L) {
    val elapsed = System.currentTimeMillis() - startedAtMs
    if (elapsed < minMs) delay((minMs - elapsed).milliseconds)
}
