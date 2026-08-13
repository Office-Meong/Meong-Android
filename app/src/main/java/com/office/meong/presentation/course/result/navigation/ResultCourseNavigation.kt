package com.office.meong.presentation.course.result.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data class ResultCourse(val courseId: Long) : Route

fun NavController.navigateToResultCourse(
    courseId: Long,
    navOptions: NavOptions? = null
) = navigate(ResultCourse(courseId), navOptions)
