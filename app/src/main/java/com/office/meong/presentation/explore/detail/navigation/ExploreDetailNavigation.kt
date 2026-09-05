package com.office.meong.presentation.explore.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data class ExploreDetail(val placeId: Long) : Route

fun NavController.navigateToExploreDetail(
    placeId: Long,
    navOptions: NavOptions? = null,
) = navigate(ExploreDetail(placeId), navOptions)
