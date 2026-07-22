package com.office.meong.core.crypto

enum class AadContext(
    val raw: String
) {
    ACCESS_TOKEN("access_token"),
    REFRESH_TOKEN("refresh_token"),
}
