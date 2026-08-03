package com.office.meong.presentation.explore.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.component.topbar.MeongTopbar
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.explore.navigation.component.detail.ExploreDetailCongestionInfo
import com.office.meong.presentation.explore.navigation.component.detail.ExploreDetailOperationInfo
import com.office.meong.presentation.explore.navigation.component.detail.ExploreDetailAccessibilityInfo
import com.office.meong.presentation.explore.navigation.component.detail.ExploreDetailActionRow
import com.office.meong.presentation.explore.navigation.component.detail.ExploreDetailHeader
import com.office.meong.presentation.explore.navigation.component.detail.ExploreDetailPetCompanionInfo
import com.office.meong.presentation.explore.navigation.component.detail.ExploreDetailPetWorkIndex
import com.office.meong.presentation.explore.navigation.model.ExploreDetailUiState

@Composable
fun ExploreDetailRoute(
    onBackClick: () -> Unit,
    paddingValues: PaddingValues
) {
    val uiState = ExploreDetailUiState.Dummy

    ExploreDetailScreen(
        paddingValues = paddingValues,
        uiState = uiState,
        onBackClick = onBackClick,
        onKakaoMapClick = { /* TODO: 카카오맵 연동 */ },
        onFavoriteClick = { /* TODO: 좋아요 API 호출 */ }
    )
}

@Composable
private fun ExploreDetailScreen(
    paddingValues: PaddingValues,
    uiState: ExploreDetailUiState,
    onBackClick: () -> Unit,
    onKakaoMapClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MeongTheme.colors.white)
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MeongTopbar(onBackClick = onBackClick)

            ExploreDetailHeader(
                typeText = uiState.typeText,
                title = uiState.title,
                address = uiState.address,
                imageUrl = uiState.imageUrl
            )

            ExploreDetailActionRow(
                onKakaoMapClick = onKakaoMapClick,
                onFavoriteClick = onFavoriteClick,
                isFavorite = uiState.isFavorite
            )

            ExploreDetailPetWorkIndex(
                grade = uiState.grade
            )

            ExploreDetailPetCompanionInfo(
                isAllowed = uiState.isAllowed,
                condition = uiState.condition,
                allowedSpace = uiState.allowedSpace,
                notice = uiState.notice
            )

            ExploreDetailOperationInfo(
                todayHours = uiState.todayHours,
                weeklyHours = uiState.weeklyHours,
                closedDays = uiState.closedDays,
                parkingInfo = uiState.parkingInfo,
                phoneNumber = uiState.phoneNumber
            )

            ExploreDetailCongestionInfo(
                congestionLevel = uiState.congestionLevel,
                tooltipText = uiState.tooltipText
            )

            ExploreDetailAccessibilityInfo(
                accessibilityTags = uiState.accessibilityTags
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

}

@Preview
@Composable
private fun ExploreDetailScreenPreview() {
    MeongTheme {
        ExploreDetailRoute(
            paddingValues = PaddingValues(),
            onBackClick = {}
        )
    }
}