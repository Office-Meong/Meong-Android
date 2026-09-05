package com.office.meong.presentation.course.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.office.meong.presentation.course.create.CreateCourseRoute
import com.office.meong.presentation.course.create.navigation.CreateCourse
import com.office.meong.presentation.course.create.navigation.navigateToCreateCourse
import com.office.meong.presentation.course.detail.DetailCourseRoute
import com.office.meong.presentation.course.detail.navigation.DetailCourse
import com.office.meong.presentation.course.detail.navigation.navigateToDetailCourse
import com.office.meong.presentation.course.my.MyCourseRoute
import com.office.meong.presentation.course.my.navigation.MyCourse
import com.office.meong.presentation.course.result.ResultCourseRoute
import com.office.meong.presentation.course.result.navigation.ResultCourse
import com.office.meong.presentation.course.result.navigation.navigateToResultCourse
import com.office.meong.presentation.explore.detail.navigation.navigateToExploreDetail
import com.office.meong.presentation.home.navigation.navigateToHome
import kotlinx.serialization.Serializable

@Serializable
data object CourseGraph

fun NavController.navigateToCourseGraph(
    navOptions: NavOptions? = null
) = navigate(CourseGraph, navOptions)

fun NavGraphBuilder.courseNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    navigation<CourseGraph>(
        startDestination = MyCourse,
    ) {
        composable<CreateCourse> {
            CreateCourseRoute(
                paddingValues = paddingValues,
                navigateUp = navController::navigateUp,
                navigateToResultCourse = { courseId -> navController.navigateToResultCourse(courseId) }
            )
        }

        composable<MyCourse> {
            MyCourseRoute(
                paddingValues = paddingValues,
                navigateToDetailCourse = navController::navigateToDetailCourse,
                navigateToCreateCourse = { navController.navigateToCreateCourse() },
            )
        }

        composable<DetailCourse> {
            DetailCourseRoute(
                paddingValues = paddingValues,
                navigateUp = navController::navigateUp,
                navigateToExploreDetail = { placeId -> navController.navigateToExploreDetail(placeId) }
            )
        }

        composable<ResultCourse> {
            ResultCourseRoute(
                paddingValues = paddingValues,
                navigateUp = navController::navigateUp,
                navigateToEntryScreen = {
                    navController.popBackStack(CreateCourse, inclusive = true)
                },
                navigateToDetailCourse = { courseId ->
                    navController.navigateToDetailCourse(
                        courseId = courseId,
                        navOptions = navOptions { popUpTo(0) { inclusive = true } }
                    )
                },
                navigateToHome = {
                    navController.navigateToHome(
                        navOptions { popUpTo(0) { inclusive = true } }
                    )
                },
                navigateToExploreDetail = { placeId -> navController.navigateToExploreDetail(placeId) }
            )
        }
    }
}
