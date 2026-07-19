package com.office.meong.presentation.course.my.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.MainTabRoute
import kotlinx.serialization.Serializable

@Serializable
data object MyCourse : MainTabRoute

fun NavController.navigateToMyCourse(navOptions: NavOptions? = null) =
    navigate(MyCourse, navOptions)
