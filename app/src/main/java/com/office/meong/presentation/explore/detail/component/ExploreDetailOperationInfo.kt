package com.office.meong.presentation.explore.detail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.designsystem.theme.MeongTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ExploreDetailOperationInfo(
    todayHours: String,
    weeklyHours: ImmutableList<String>,
    closedDays: String,
    parkingInfo: String,
    phoneNumber: String,
    modifier: Modifier = Modifier
) {
    var isTimeExpanded by remember { mutableStateOf(false) }

    ExploreDetailSectionContainer(title = "운영·편의 정보", modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MeongTheme.colors.gray100, RoundedCornerShape(12.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "운영 시간",
                    style = MeongTheme.typography.label.label12Sb,
                    color = MeongTheme.colors.gray500
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isTimeExpanded = !isTimeExpanded },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = todayHours,
                        style = MeongTheme.typography.body.body14M,
                        color = MeongTheme.colors.gray900
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    if (weeklyHours.isNotEmpty()) {
                        Icon(
                            imageVector = if (isTimeExpanded) ImageVector.vectorResource(id = R.drawable.ic_chevron_up) else ImageVector.vectorResource(id = R.drawable.ic_chevron_down),
                            contentDescription = "시간 펼치기",
                            tint = MeongTheme.colors.gray900,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                if (weeklyHours.isNotEmpty()) {
                    AnimatedVisibility(visible = isTimeExpanded) {
                        Column(
                            modifier = Modifier.padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            weeklyHours.forEach { dayTime ->
                                Text(
                                    text = dayTime,
                                    style = MeongTheme.typography.body.body14M,
                                    color = MeongTheme.colors.gray900
                                )
                            }
                        }
                    }
                }
            }
            HorizontalDivider(color = MeongTheme.colors.gray100)
            ExploreDetailInfoItem(title = "휴무일", content = closedDays)
            HorizontalDivider(color = MeongTheme.colors.gray100)
            ExploreDetailInfoItem(title = "주차", content = parkingInfo)
            HorizontalDivider(color = MeongTheme.colors.gray100)
            ExploreDetailInfoItem(title = "전화번호", content = phoneNumber)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreDetailOperationInfoPreview() {
    MeongTheme {
        ExploreDetailOperationInfo(
            todayHours = "월 10:30 - 21:00",
            weeklyHours = persistentListOf(
                "화 10:30 - 21:00",
                "수 10:30 - 21:00",
                "목 10:30 - 21:00",
                "금 10:30 - 21:00",
                "토 10:30 - 21:00",
                "일 정기휴무 (매주 일요일)"
            ),
            closedDays = "매주 일요일",
            parkingInfo = "가능",
            phoneNumber = "033-000-0000",
            modifier = Modifier.padding(20.dp)
        )
    }
}
