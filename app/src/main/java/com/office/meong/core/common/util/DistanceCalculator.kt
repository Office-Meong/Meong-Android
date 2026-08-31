package com.office.meong.core.common.util

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val EARTH_RADIUS_KM = 6371.0

/** 두 좌표 사이의 직선거리(km, Haversine)를 계산한다. 서버가 실제 경로 거리를 못 내려줄 때의 추정치로만 사용한다. */
fun calculateStraightLineDistanceKm(
    startLatitude: Double,
    startLongitude: Double,
    endLatitude: Double,
    endLongitude: Double,
): Double {
    val dLat = Math.toRadians(endLatitude - startLatitude)
    val dLon = Math.toRadians(endLongitude - startLongitude)
    val a = (
        sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(startLatitude)) * cos(Math.toRadians(endLatitude)) *
            sin(dLon / 2) * sin(dLon / 2)
        ).coerceIn(0.0, 1.0) // 부동소수점 오차로 1을 살짝 넘으면 sqrt(1-a)가 NaN이 될 수 있어 방어
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return EARTH_RADIUS_KM * c
}
