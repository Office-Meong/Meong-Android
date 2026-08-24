package com.office.meong.core.common.extension

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import timber.log.Timber

fun Context.openUrl(url: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    } catch (e: ActivityNotFoundException) {
        Timber.w(e, "URL을 열 수 없습니다: $url")
    }
}

fun Context.openKakaoMap(placeName: String) {
    val query = Uri.encode(placeName)
    openKakaoMapUri(
        appUri = "kakaomap://search?q=$query",
        webUri = "https://map.kakao.com/link/search/$query"
    )
}

fun Context.openKakaoMap(placeName: String, latitude: Double, longitude: Double) {
    val encodedName = Uri.encode(placeName)
    openKakaoMapUri(
        appUri = "kakaomap://look?p=$latitude,$longitude",
        webUri = "https://map.kakao.com/link/map/$encodedName,$latitude,$longitude"
    )
}

fun Context.openKakaoMapRoute(
    originName: String,
    originLatitude: Double,
    originLongitude: Double,
    destinationName: String,
    destinationLatitude: Double,
    destinationLongitude: Double,
    type: String? = "CAR"
) {
    val encodedOrigin = Uri.encode(originName)
    val encodedDestination = Uri.encode(destinationName)
    openKakaoMapUri(
        appUri = "kakaomap://route?sp=$originLatitude,$originLongitude&ep=$destinationLatitude,$destinationLongitude&by=${type}",
        webUri = "https://map.kakao.com/link/from/$encodedOrigin,$originLatitude,$originLongitude/to/$encodedDestination,$destinationLatitude,$destinationLongitude"
    )
}

private fun Context.openKakaoMapUri(appUri: String, webUri: String) {
    val appIntent = Intent(Intent.ACTION_VIEW, appUri.toUri())

    val intent = if (appIntent.resolveActivity(packageManager) != null) {
        appIntent
    } else {
        Intent(Intent.ACTION_VIEW, webUri.toUri())
    }

    startActivity(intent)
}
