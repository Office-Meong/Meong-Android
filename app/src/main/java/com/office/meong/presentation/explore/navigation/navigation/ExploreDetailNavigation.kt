package com.office.meong.presentation.explore.navigation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.office.meong.core.navigation.Route
import com.office.meong.presentation.explore.navigation.ExploreDetailRoute
import kotlinx.serialization.Serializable

@Serializable
data object ExploreDetail
    : Route
//TODO : 카드 id 값 서버에서 내려올 시 변경
// @Serializable
// data class ExploreDetail(val placeId: Long)

fun NavController.navigateToExploreDetail(navOptions: NavOptions? = null) {
    navigate(ExploreDetail, navOptions)
}

fun NavGraphBuilder.exploreDetailNavGraph(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit
) {
    composable<ExploreDetail> {
        ExploreDetailRoute(
            paddingValues = paddingValues,
            onBackClick = onBackClick
        )
    }
}