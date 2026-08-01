package com.office.meong.presentation.course.result.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object ResultCourse: Route

fun NavController.navigateToResultCourse(navOptions: NavOptions? = null) =
    navigate(ResultCourse, navOptions)
