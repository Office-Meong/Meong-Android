package com.office.meong.presentation.course.create.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object CreateCourse: Route

fun NavController.navigateToCreateCourse(navOptions: NavOptions? = null) =
    navigate(CreateCourse, navOptions)

