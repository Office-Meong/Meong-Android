package com.office.meong.presentation.course.detail.component

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
import com.office.meong.core.model.place.PlaceType
import com.office.meong.presentation.course.detail.model.DetailCourseRouteIndicatorType
import com.office.meong.presentation.sharedcomponent.MeongPlaceCard

@Composable
fun DetailCourseScheduleItem(
    count: Int,
    placeName: String,
    placeType: PlaceType,
    isLastItem: Boolean,
    onFavoriteClick: () -> Unit,
    onRouteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var markerSize by remember { mutableStateOf(IntSize.Zero) }
    val lineColor = MeongTheme.colors.gray100

    Row(
        modifier = modifier.fillMaxWidth().drawBehind {
            if (!isLastItem) {
                drawLine(
                    color = lineColor,
                    start = Offset(markerSize.width / 2f, markerSize.height / 2f),
                    end = Offset(markerSize.width / 2f, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    ) {
        Box(
            modifier = Modifier.onSizeChanged { markerSize = it }.background(MeongTheme.colors.gray700, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                count.toString(),
                style = MeongTheme.typography.label.label12Sb.copy(letterSpacing = 0.sp),
                color = MeongTheme.colors.white,
                modifier = Modifier.layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    val circleSize = maxOf(placeable.width, placeable.height) + 6.dp.roundToPx()
                    layout(circleSize, circleSize) { placeable.placeRelative((circleSize - placeable.width) / 2, (circleSize - placeable.height) / 2) }
                }
            )
        }

        Spacer(Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f).padding(bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MeongPlaceCard(
                placeName = placeName,
                location = "강원 강릉시 하남길 117-4",
                grade = "A",
                isFavorite = false,
                onFavoriteClick = onFavoriteClick,
                placeType = placeType
            )

            if (!isLastItem) {
                DetailCourseRouteIndicator("1.2", onRouteClick, routeIndicatorType = DetailCourseRouteIndicatorType.BETWEEN)
            }
        }
    }
}

@Preview
@Composable
private fun DetailCourseScheduleItemPreview() {
    MeongTheme {
        DetailCourseScheduleItem(
            count = 1,
            placeName = "멍멍이 카페",
            placeType = PlaceType.WORKSPACE,
            isLastItem = false,
            onFavoriteClick = {},
            onRouteClick = {}
        )
    }
}
