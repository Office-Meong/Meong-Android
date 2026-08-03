package com.office.meong.presentation.course.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.place.PlaceType
import com.office.meong.presentation.course.my.component.MyCourseItem
import com.office.meong.presentation.course.my.model.MyCoursePlaceCategory
import kotlinx.collections.immutable.toPersistentList

@Composable
fun MyCourseRoute(
    paddingValues: PaddingValues,
    navigateToDetailCourse: () -> Unit = {}
) {
    MyCourseScreen(
        paddingValues = paddingValues,
        myCourseCount = 10,
        navigateToDetailCourse = navigateToDetailCourse
    )
}

@Composable
private fun MyCourseScreen(
    paddingValues: PaddingValues,
    myCourseCount: Int,
    navigateToDetailCourse: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MeongTheme.colors.gray50
            )
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = "내 코스",
            style = MeongTheme.typography.title.title20Sb,
            color = MeongTheme.colors.gray900,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "총 ${myCourseCount}개",
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray700,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = (1..10).toList(),
            ) {
                MyCourseItem(
                    location = "강릉",
                    tripPeriod = "2박 3일 (2026.8.10 - 2026.8.12)",
                    title = "몽몽이랑 여름 힐링 워케이션",
                    grade = "B",
                    places = mapOf(
                        PlaceType.WORKSPACE to 2,
                        PlaceType.RESTAURANT to 5,
                        PlaceType.SIGHTSEEING to 4,
                        PlaceType.OTHER to 1
                    ).map { (type, count) ->
                        MyCoursePlaceCategory(type, count)
                    }.toPersistentList(),
                    onClickCourseItem = navigateToDetailCourse
                )
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Preview
@Composable
private fun MyCourseScreenPreview() {
    MeongTheme {
        MyCourseScreen(
            myCourseCount = 10,
            paddingValues = PaddingValues()
        )
    }
}