package com.office.meong.presentation.home.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.common.extension.styleableBackground
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.home.model.HomePlaceCategory
import com.office.meong.core.model.place.PlaceType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HomeCourseItem(
    region: String,
    tripPeriod: String,
    title: String,
    grade: String,
    places: ImmutableList<HomePlaceCategory>,
    onClickCourseItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .styleableBackground(
                color = MeongTheme.colors.white,
                shape = RoundedCornerShape(20.dp),
                padding = 20.dp
            )
            .noRippleClickable(
                onClick = onClickCourseItem
            )
    ) {
        HomeCourseTopSection(
            region = region,
            tripPeriod = tripPeriod,
            title = title,
            grade = grade
        )

        Spacer(modifier = Modifier.height(14.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = MeongTheme.colors.gray100,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        HomeCourseCategorySection(
            places = places,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = MeongTheme.colors.gray100,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        HomeCourseBottomSection(
            totalCount = places.size,
        )

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
private fun HomeCourseTopSection(
    region: String,
    tripPeriod: String,
    title: String,
    grade: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = region,
                style = MeongTheme.typography.body.body14M,
                color = MeongTheme.colors.gray600
            )

            VerticalDivider(
                thickness = 1.dp,
                color = MeongTheme.colors.gray200,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 5.dp)
            )

            Text(
                text = tripPeriod,
                style = MeongTheme.typography.body.body14M,
                color = MeongTheme.colors.gray600
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            style = MeongTheme.typography.title.title16Sb,
            color = MeongTheme.colors.gray900
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_pet_filled),
                contentDescription = null,
                tint = MeongTheme.colors.gray700,
                modifier = Modifier
                    .size(16.dp)
            )

            Text(
                text = "펫-워크 평균 등급",
                style = MeongTheme.typography.label.label12Sb,
                color = MeongTheme.colors.gray700
            )

            MeongChip(
                chipText = "${grade}등급",
                isActivated = true,
            )
        }
    }
}

@Composable
private fun HomeCourseCategorySection(
    places: ImmutableList<HomePlaceCategory>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        places.forEach { item ->
            HomeCourseCategoryBadgeItem(
                label = item.type.label,
                iconRes = item.type.iconRes,
                count = item.count
            )
        }
    }
}

@Composable
private fun HomeCourseCategoryBadgeItem(
    label: String,
    @DrawableRes iconRes: Int,
    count: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box {
            Box(
                modifier = Modifier.styleableBackground(
                    color = MeongTheme.colors.gray50,
                    shape = CircleShape,
                    padding = 20.dp
                ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(iconRes),
                    contentDescription = label,
                    tint = MeongTheme.colors.gray400
                )
            }

            if (count > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(
                            color = MeongTheme.colors.gray700,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = count.toString(),
                        style = MeongTheme.typography.label.label12Sb,
                        color = MeongTheme.colors.white,
                        modifier = Modifier
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)
                                val maxSize = maxOf(placeable.width, placeable.height)

                                layout(maxSize, maxSize) {
                                    placeable.placeRelative(
                                        x = (maxSize - placeable.width) / 2,
                                        y = (maxSize - placeable.height) / 2
                                    )
                                }
                            }
                            .padding(3.dp)
                    )
                }
            }
        }
        Text(
            text = label,
            style = MeongTheme.typography.label.label12Sb,
            color = MeongTheme.colors.gray700
        )
    }
}

@Composable
private fun HomeCourseBottomSection(
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "총 ${totalCount}곳 장소 포함",
            style = MeongTheme.typography.body.body12M,
            color = MeongTheme.colors.gray500
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "상세보기",
            style = MeongTheme.typography.label.label12Sb,
            color = MeongTheme.colors.gray700
        )

        Spacer(modifier = Modifier.size(2.dp))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = MeongTheme.colors.gray700
        )
    }
}

@Preview
@Composable
private fun HomeCourseItemPreview() {
    MeongTheme {
        HomeCourseItem(
            region = "강릉",
            tripPeriod = "2박 3일 (2026.8.10 - 2026.8.12)",
            title = "몽몽이랑 여름 힐링 워케이션",
            grade = "B",
            places = persistentListOf(
                HomePlaceCategory(
                    type = PlaceType.WORKSPACE,
                    count = 2
                ),
                HomePlaceCategory(
                    type = PlaceType.RESTAURANT,
                    count = 5
                ),
                HomePlaceCategory(
                    type = PlaceType.SIGHTSEEING,
                    count = 4
                ),
                HomePlaceCategory(
                    type = PlaceType.OTHER,
                    count = 1
                )
            ),
            onClickCourseItem = {}
        )
    }
}
