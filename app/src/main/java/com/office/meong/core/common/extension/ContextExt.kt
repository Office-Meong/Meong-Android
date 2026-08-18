package com.office.meong.core.common.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

fun Context.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}

fun Context.openKakaoMap(placeName: String) {
    val query = Uri.encode(placeName)
    openKakaoMapUri(
        appUri = "kakaomap://search?q=$query",
        webUri = "https://map.kakao.com/link/search/$query"
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
