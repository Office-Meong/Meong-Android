package com.office.meong.presentation.course.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.office.meong.presentation.course.create.CreateCourseRoute
import com.office.meong.presentation.course.create.navigation.CreateCourse
import com.office.meong.presentation.course.my.navigation.MyCourse
import com.office.meong.presentation.course.result.ResultCourseRoute
import com.office.meong.presentation.course.result.navigation.ResultCourse
import kotlinx.serialization.Serializable

@Serializable
data object CourseGraph

fun NavController.navigateToCourseGraph(
    navOptions: NavOptions? = null
) = navigate(CourseGraph, navOptions)

fun NavGraphBuilder.courseNavGraph(
    paddingValues: PaddingValues,
) {
    navigation<CourseGraph>(
        startDestination = ResultCourse
    ) {
        composable<CreateCourse> {
            CreateCourseRoute()
        }

        composable<ResultCourse> {
            ResultCourseRoute(
                paddingValues = paddingValues,
                navigateUp = {}
            )
        }
    }
}
