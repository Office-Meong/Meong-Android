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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.R
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.util.UiState
import com.office.meong.core.designsystem.component.button.MeongPillButton
import com.office.meong.core.designsystem.component.image.StableImage
import com.office.meong.core.designsystem.component.indicator.MeongLoadingIndicator
import com.office.meong.core.designsystem.component.view.LoadErrorViewAction
import com.office.meong.core.designsystem.component.view.MeongLoadErrorView
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetInfo
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability
import com.office.meong.presentation.sharedcomponent.CourseEmptyContent
import com.office.meong.presentation.home.component.HomeCourseItem
import com.office.meong.presentation.home.component.HomeTooltipBalloon
import com.office.meong.presentation.home.model.HomeCourseSummaryUiModel
import com.office.meong.presentation.sharedcomponent.PetProfileCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HomeRoute(
    paddingValues: PaddingValues,
    navigateToCreateCourse: () -> Unit = {},
    navigateToDetailCourse: (Long) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.retryLoad()
    }

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is HomeSideEffect.ShowSnackBar -> {
                globalUiEventHolder.showSnackbar(
                    SnackbarState(
                        message = it.message
                    )
                )
            }
        }
    }

    HomeScreen(
        paddingValues = paddingValues,
        petInfo = state.petInfo,
        homeCourseSummaries = state.homeCourseSummaries,
        onRetryCourses = viewModel::retryLoad,
        onRetryPetInfo = viewModel::retryPetInfo,
        navigateToCreateCourse = navigateToCreateCourse,
        navigateToDetailCourse = navigateToDetailCourse
    )
}

@Composable
private fun HomeScreen(
    paddingValues: PaddingValues,
    petInfo: UiState<PetInfo>,
    homeCourseSummaries: UiState<ImmutableList<HomeCourseSummaryUiModel>>,
    onRetryCourses: () -> Unit,
    onRetryPetInfo: () -> Unit,
    navigateToCreateCourse: () -> Unit,
    navigateToDetailCourse: (Long) -> Unit,
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

        when (petInfo) {
            is UiState.Loading -> {
                MeongLoadingIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(196.dp)
                )
            }

            is UiState.Failure -> {
                MeongLoadErrorView(
                    action = LoadErrorViewAction.Retry(
                        onRetryClick = onRetryPetInfo
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(196.dp)
                )
            }

            is UiState.Empty -> Unit

            is UiState.Success -> {
                val pet = petInfo.data

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    HomeTooltipBalloon(
                        text = "와 함께할\n" +
                                "워케이션을 준비해볼까요?",
                        emphasizeText = pet.name,
                    )

                    StableImage(
                        drawableResId = R.drawable.img_character,
                        modifier = Modifier
                            .size(120.dp),
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
                    petName = pet.name,
                    imageUrl = pet.imageUrl,
                    tags = persistentListOf(
                        pet.sizeCategory.label,
                        "활동량 ${pet.activityLevel.label}",
                        "사회성 ${pet.sociability.label}",
                        pet.healthStatus.label
                    ),
                    isBordered = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }
        }

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

        when (homeCourseSummaries) {
            is UiState.Loading -> {
                MeongLoadingIndicator(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            is UiState.Failure -> {
                MeongLoadErrorView(
                    action = LoadErrorViewAction.Retry(
                        onRetryClick = onRetryCourses
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            is UiState.Empty -> {
                CourseEmptyContent(
                    onClickPillButton = navigateToCreateCourse,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }

            is UiState.Success -> {
                if (homeCourseSummaries.data.isEmpty()) {
                    CourseEmptyContent(
                        onClickPillButton = navigateToCreateCourse,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(
                            items = homeCourseSummaries.data,
                            key = { item -> item.id }
                        ) { item ->
                            HomeCourseItem(
                                region = item.region.label,
                                tripPeriod = item.tripPeriod,
                                title = item.name,
                                grade = item.averageGrade,
                                places = item.places,
                                onClickCourseItem = { navigateToDetailCourse(item.id) }
                            )
                        }

                        item {
                            MeongPillButton(
                                text = "새 코스 만들기",
                                prefixIcon = R.drawable.ic_plus,
                                onClick = navigateToCreateCourse
                            )

                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
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
            homeCourseSummaries = UiState.Success(persistentListOf()),
            onRetryCourses = {},
            onRetryPetInfo = {},
            petInfo = UiState.Success(
                PetInfo(
                    id = 1,
                    name = "몽몽이",
                    breed = "푸들",
                    weightKg = 10.0,
                    birthDate = "2020-01-01",
                    isNeutered = true,
                    imageUrl = "",
                    sizeCategory = PetSizeCategory.SMALL,
                    activityLevel = PetActivityLevel.MEDIUM,
                    sociability = PetSociability.NORMAL,
                    healthStatus = PetHealthStatus.HEALTHY
                )
            ),
            navigateToCreateCourse = {},
            navigateToDetailCourse = {}
        )
    }
}
