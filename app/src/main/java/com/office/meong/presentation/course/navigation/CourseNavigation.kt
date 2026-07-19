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
import kotlinx.serialization.Serializable

@Serializable
data object CourseGraph

fun NavController.navigateToCourseGraph(
    navOptions: NavOptions? = null
) = navigate(CourseGraph, navOptions)

fun NavGraphBuilder.courseGraph(
    paddingValues: PaddingValues,
) {
    navigation<CourseGraph>(
        startDestination = CreateCourse
    ) {
        composable<CreateCourse> {
            CreateCourseRoute()
        }
    }
}