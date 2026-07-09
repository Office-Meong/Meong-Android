package com.office.meong.presentation.main

import androidx.annotation.DrawableRes
import com.office.meong.core.navigation.MainTabRoute

enum class MainTab(
    @param:DrawableRes val selectedIcon: Int,
    @param:DrawableRes val unselectedIcon: Int,
    val contentDescription: String,
    val route: MainTabRoute,
) {
    /*HOME(
        selectedIcon = R.drawable.ic_home,
        unselectedIcon = R.drawable.ic_home_filled,
        contentDescription = "home",
        route = null,
    ),*/
    ;
    companion object {

        fun find(predicate: (MainTabRoute) -> Boolean): MainTab? {
            return entries.find { predicate(it.route) }
        }

        fun contains(predicate: (MainTabRoute) -> Boolean): Boolean {
            return entries.any { predicate(it.route) }
        }
    }
}