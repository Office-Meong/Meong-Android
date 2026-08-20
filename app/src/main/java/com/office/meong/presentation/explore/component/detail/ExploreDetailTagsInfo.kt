package com.office.meong.presentation.explore.component.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.component.chip.ChipType
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun ExploreDetailAccessibilityInfo(
    accessibilityTags: List<String>,
    modifier: Modifier = Modifier
) {
    if (accessibilityTags.isEmpty()) return

    ExploreDetailSectionContainer(title = "접근성 정보", modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            accessibilityTags.forEach { tag ->
                MeongChip(
                    chipText = tag,
                    chipType = ChipType.SMALL
                )
            }
        }
    }
}

@Composable
fun ExploreDetailPetCompanionInfo(
    isAllowed: String,
    condition: String,
    allowedSpace: String,
    notice: String,
    modifier: Modifier = Modifier
) {
    ExploreDetailSectionContainer(title = "반려견 동반 정보", modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MeongTheme.colors.gray100, RoundedCornerShape(12.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExploreDetailInfoItem(title = "동반 가능 여부", content = isAllowed)
            HorizontalDivider(color = MeongTheme.colors.gray100)
            ExploreDetailInfoItem(title = "조건", content = condition)
            HorizontalDivider(color = MeongTheme.colors.gray100)
            ExploreDetailInfoItem(title = "이용 가능 공간", content = allowedSpace)
            HorizontalDivider(color = MeongTheme.colors.gray100)
            ExploreDetailInfoItem(title = "유의사항", content = notice)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreDetailTagsInfoPreview() {
    MeongTheme {
        Column(modifier = Modifier.padding(20.dp)) {
            ExploreDetailPetCompanionInfo(
                isAllowed = "동반 가능",
                condition = "소형견 가능, 목줄 필수",
                allowedSpace = "야외 테라스",
                notice = "이동 시 목줄을 착용해주세요"
            )
            ExploreDetailAccessibilityInfo(
                accessibilityTags = listOf("경사로 있음", "유모차 이동 가능"),
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}
