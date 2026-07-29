package com.office.meong.core.model.place

import androidx.annotation.DrawableRes
import com.office.meong.R

enum class PlaceType(
    val label: String,
    @param:DrawableRes val iconRes: Int,
) {
    WORKSPACE("업무장소", R.drawable.ic_work_filled),
    RESTAURANT("음식점", R.drawable.ic_food_filled),
    SIGHTSEEING("관광·산책", R.drawable.ic_park_filled),
    ACCOMMODATION("숙소", R.drawable.ic_bed_filled),
    OTHER("기타", R.drawable.ic_etc_filled)
}
