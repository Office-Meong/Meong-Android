package com.office.meong.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.designsystem.component.button.MeongPillButton
import com.office.meong.core.designsystem.component.image.StableImage
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.home.component.HomeCourseEmptyContent
import com.office.meong.presentation.home.component.HomeCourseItem
import com.office.meong.presentation.home.component.HomeTooltipBalloon
import com.office.meong.presentation.home.model.HomePlaceCategory
import com.office.meong.presentation.home.model.PlaceType
import com.office.meong.presentation.sharedcomponent.PetProfileCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HomeRoute(
    paddingValues: PaddingValues
) {
    HomeScreen(
        paddingValues = paddingValues,
        myCourseList = persistentListOf(
            "테스트1",
            "테스트2",
            "테스트3",
            "테스트4",
            "테스트5",
        )
    )
}

@Composable
private fun HomeScreen(
    paddingValues: PaddingValues,
    myCourseList: ImmutableList<String>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeongTheme.colors.gray50)
            .padding(paddingValues = paddingValues)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeTooltipBalloon(
                text = "와 함께할\n" +
                        "워케이션을 준비해볼까요?",
                emphasizeText = "몽몽이",
            )

            StableImage(
                drawableResId = R.drawable.img_character,
                modifier = Modifier
                    .size(width = 92.dp, height = 120.dp),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = "반려견 정보",
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray700,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        PetProfileCard(
            petName = "몽몽이",
            imageUrl = "",
            tags = persistentListOf("소형견","활동량 보통", "사회성 보통"),
            isBordered = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "최근에 만든 코스",
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray700,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (myCourseList.isEmpty()) {
            HomeCourseEmptyContent(
                onClickPillButton = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    count = 5,
                ) {
                    HomeCourseItem(
                        location = "강릉",
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

                item {
                    MeongPillButton(
                        text = "새 코스 만들기",
                        isPrimary = false,
                        prefixIcon = R.drawable.ic_plus,
                        onClick = {}
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MeongTheme {
        HomeScreen(
            paddingValues = PaddingValues(),
            myCourseList = persistentListOf(
                "테스트1",

            )
        )
    }
}
