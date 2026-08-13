package com.office.meong.presentation.course.result.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.place.PlaceType
import com.office.meong.presentation.course.result.model.RouteIndicatorType
import com.office.meong.presentation.course.result.model.ScheduleUiModel

@Composable
fun ResultCourseScheduleSection(
    dayNumber: String,
    tripDay: String,
    accommodation: ScheduleUiModel?,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onRouteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        DateNavigator(
            dayNumber = dayNumber,
            tripDay = tripDay,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick
        )

        if (accommodation != null) {
            Spacer(modifier = Modifier.height(16.dp))

            ResultCoursePlaceSummaryItem(
                placeType = accommodation.placeType.label,
                placeName = accommodation.placeName
            )

            Spacer(modifier = Modifier.height(10.dp))

            RouteIndicator(
                routeLength = "1.2",
                onRouteClick = onRouteClick,
                routeIndicatorType = RouteIndicatorType.START,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun DateNavigator(
    dayNumber: String,
    tripDay: String,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
            contentDescription = "이전",
            modifier = Modifier
                .size(16.dp)
                .noRippleClickable(onClick = onPreviousClick),
            tint = MeongTheme.colors.gray900,
        )

        Spacer(modifier = Modifier.width(10.dp))

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "${dayNumber}일차",
                style = MeongTheme.typography.label.label14Sb,
                color = MeongTheme.colors.gray900,
            )

            VerticalDivider(
                thickness = 1.dp,
                color = MeongTheme.colors.gray200,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 5.dp)
            )

            Text(
                text = tripDay,
                style = MeongTheme.typography.label.label14Sb,
                color = MeongTheme.colors.gray700,
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
            contentDescription = "이후",
            modifier = Modifier
                .size(16.dp)
                .noRippleClickable(onClick = onNextClick),
            tint = MeongTheme.colors.gray900,
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ResultCourseScheduleSectionPreview() {
    MeongTheme {
        ResultCourseScheduleSection(
            dayNumber = "2",
            tripDay = "8.11",
            accommodation = ScheduleUiModel(id = "0", placeType = PlaceType.ACCOMMODATION, placeName = "프렌즈애견펜션", grade = "A"),
            onPreviousClick = {},
            onNextClick = {},
            onRouteClick = {}
        )
    }
}
