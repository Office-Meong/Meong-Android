package com.office.meong.presentation.explore.detail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.designsystem.component.chip.ChipType
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun ExploreDetailCongestionInfo(
    congestionLevel: String,
    tooltipText: String,
    modifier: Modifier = Modifier
) {
    var showTooltip by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "예측 혼잡도",
                style = MeongTheme.typography.label.label16Sb,
                color = MeongTheme.colors.gray700
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_info),
                contentDescription = "혼잡도 정보",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { showTooltip = !showTooltip },
                tint = MeongTheme.colors.gray500
            )
        }

        AnimatedVisibility(visible = showTooltip) {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .background(MeongTheme.colors.gray800, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = tooltipText,
                    style = MeongTheme.typography.body.body14M,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "방문 전 현장 상황을 확인해보세요",
            style = MeongTheme.typography.body.body14M,
            color = MeongTheme.colors.gray500
        )
        Spacer(modifier = Modifier.height(12.dp))

        MeongChip(
            chipText = congestionLevel,
            chipType = ChipType.SMALL
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreDetailCongestionInfoPreview() {
    MeongTheme {
        ExploreDetailCongestionInfo(
            congestionLevel = "보통",
            tooltipText = "한국관광공사 정보를 바탕으로 하루 한 번 초기화돼요",
            modifier = Modifier.padding(20.dp)
        )
    }
}
