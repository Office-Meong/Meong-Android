package com.office.meong.presentation.course.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data class DetailCourse(
    val courseId: Long,
): Route

fun NavController.navigateToDetailCourse(
    courseId: Long,
    navOptions: NavOptions? = null
) = navigate(DetailCourse(courseId), navOptions)