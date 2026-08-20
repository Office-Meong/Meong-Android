package com.office.meong.core.model.place

import androidx.annotation.DrawableRes
import com.office.meong.R

enum class PlaceType(
    val label: String,
    @param:DrawableRes val iconRes: Int,
    val apiValue: String?,
) {
    WORKSPACE("업무장소", R.drawable.ic_work_filled, "WORK_PLACE"),
    RESTAURANT("음식점", R.drawable.ic_food_filled, "FOOD"),
    SIGHTSEEING("관광·산책", R.drawable.ic_park_filled, "TOUR"),
    ACCOMMODATION("숙소", R.drawable.ic_bed_filled, "STAY"),
    OTHER("기타", R.drawable.ic_etc_filled, null);

    companion object {
        fun from(value: String): PlaceType = when (value) {
            "STAY" -> ACCOMMODATION
            "WORK_PLACE" -> WORKSPACE
            "FOOD" -> RESTAURANT
            "TOUR", "WALK" -> SIGHTSEEING
            else -> OTHER
        }
    }
}
