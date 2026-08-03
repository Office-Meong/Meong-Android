package com.office.meong.presentation.course.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object DetailCourse: Route

fun NavController.navigateToDetailCourse(
    navOptions: NavOptions? = null
) = navigate(DetailCourse, navOptions)