package com.office.meong.core.model.region

import kotlinx.serialization.Serializable

@Serializable
enum class Region(val label: String) {
    GANGNEUNG("강릉"),
    CHUNCHEON("춘천"),
    WONJU("원주")
}
