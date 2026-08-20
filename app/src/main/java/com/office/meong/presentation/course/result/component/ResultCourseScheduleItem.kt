package com.office.meong.presentation.course.result.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.place.LodgingType
import com.office.meong.core.model.place.PlaceType
import com.office.meong.presentation.course.result.model.RouteIndicatorType
import com.office.meong.presentation.sharedcomponent.MeongPlaceCard
import com.office.meong.presentation.sharedcomponent.skeleton.MeongPlaceCardSkeleton
import com.office.meong.presentation.sharedcomponent.skeleton.meongShimmerTheme
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer

@Composable
fun ResultCourseScheduleItem(
    count: Int,
    placeName: String,
    grade: String,
    isLastItem: Boolean,
    placeType: PlaceType,
    isFavorite: Boolean,
    isLoading: Boolean,
    shimmer: Shimmer,
    onFavoriteClick: () -> Unit,
    onRouteClick: () -> Unit,
    modifier: Modifier = Modifier,
    location: String = "",
    thumbnailUrl: String? = null,
    lodgingType: LodgingType? = null,
    routeLength: String = "0.0"
) {
    val lineColor = MeongTheme.colors.gray100

    var markerSize by remember { mutableStateOf(IntSize.Zero) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                if (!isLastItem) {
                    val timelineCenterX = markerSize.width / 2f
                    drawLine(
                        color = lineColor,
                        start = Offset(timelineCenterX, markerSize.height / 2f),
                        end = Offset(timelineCenterX, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
    ) {
        TimelineMarker(
            count = count,
            modifier = Modifier.onSizeChanged { markerSize = it }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (isLoading) {
                MeongPlaceCardSkeleton(shimmer = shimmer)
            } else {
                MeongPlaceCard(
                    placeName = placeName,
                    location = location,
                    grade = grade,
                    isFavorite = isFavorite,
                    onFavoriteClick = onFavoriteClick,
                    placeType = placeType,
                    thumbnailUrl = thumbnailUrl,
                    lodgingType = lodgingType,
                )
            }

            if (!isLastItem) {
                RouteIndicator(
                    routeLength = routeLength,
                    onRouteClick = onRouteClick,
                    routeIndicatorType = RouteIndicatorType.BETWEEN
                )
            }
        }
    }
}

@Composable
private fun TimelineMarker(
    count: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = MeongTheme.colors.gray700, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = count.toString(),
            style = MeongTheme.typography.label.label12Sb.copy(letterSpacing = 0.sp),
            color = MeongTheme.colors.white,
            modifier = Modifier.layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val squareSize = maxOf(placeable.width, placeable.height)
                val gap = 3.dp.roundToPx() * 2
                val circleSize = squareSize + gap

                layout(circleSize, circleSize) {
                    placeable.placeRelative(
                        x = (circleSize - placeable.width) / 2,
                        y = (circleSize - placeable.height) / 2
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun ResultCourseScheduleItemPreview() {
    MeongTheme {
        ResultCourseScheduleItem(
            count = 1,
            placeName = "프렌즈애견펜션",
            grade = "A",
            isLastItem = false,
            placeType = PlaceType.WORKSPACE,
            isFavorite = false,
            isLoading = false,
            shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View, theme = meongShimmerTheme()),
            onFavoriteClick = {},
            onRouteClick = {}
        )
    }
}

@Preview
@Composable
private fun ResultCourseScheduleItemLoadingPreview() {
    MeongTheme {
        ResultCourseScheduleItem(
            count = 1,
            placeName = "프렌즈애견펜션",
            grade = "A",
            isLastItem = false,
            placeType = PlaceType.WORKSPACE,
            isFavorite = false,
            isLoading = true,
            shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View, theme = meongShimmerTheme()),
            onFavoriteClick = {},
            onRouteClick = {}
        )
    }
}
