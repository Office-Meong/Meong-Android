package com.office.meong.presentation.explore.detail.component

import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.explore.detail.model.ExploreWalkCourseUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ExploreDetailWalkCourseSection(
    courses: ImmutableList<ExploreWalkCourseUiModel>,
    onCourseClick: (ExploreWalkCourseUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    if (courses.isEmpty()) return

    ExploreDetailSectionContainer(title = "주변 산책 코스", modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            courses.forEach { course ->
                ExploreDetailWalkCourseItem(course = course, onClick = { onCourseClick(course) })
            }
        }
    }
}

@Composable
private fun ExploreDetailWalkCourseItem(
    course: ExploreWalkCourseUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MeongTheme.colors.gray100, RoundedCornerShape(12.dp))
            .noRippleClickable(onClick)
            .padding(16.dp)
    ) {
        Text(
            text = course.courseName,
            style = MeongTheme.typography.title.title16Sb,
            color = MeongTheme.colors.gray900
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_location_filled),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "장소에서 %.1fkm · 코스 길이 %.1fkm".format(course.distanceFromPlaceKm, course.distanceKm),
                style = MeongTheme.typography.body.body12M,
                color = MeongTheme.colors.gray600
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreDetailWalkCourseSectionPreview() {
    MeongTheme {
        ExploreDetailWalkCourseSection(
            courses = listOf(
                ExploreWalkCourseUiModel(
                    id = 1L,
                    courseName = "경포호 둘레길",
                    distanceKm = 4.2,
                    distanceFromPlaceKm = 0.8,
                    latitude = 37.795,
                    longitude = 128.907,
                ),
                ExploreWalkCourseUiModel(
                    id = 2L,
                    courseName = "안목해변 산책로",
                    distanceKm = 2.5,
                    distanceFromPlaceKm = 1.5,
                    latitude = 37.772,
                    longitude = 128.947,
                ),
            ).toImmutableList(),
            onCourseClick = {},
            modifier = Modifier.padding(20.dp)
        )
    }
}
