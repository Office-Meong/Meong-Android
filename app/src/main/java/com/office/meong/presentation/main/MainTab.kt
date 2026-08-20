package com.office.meong.presentation.main

import androidx.annotation.DrawableRes
import com.office.meong.R
import com.office.meong.core.navigation.MainTabRoute
import com.office.meong.presentation.explore.Explore
import com.office.meong.presentation.favorite.navigation.Favorite
import com.office.meong.presentation.home.navigation.Home
import com.office.meong.presentation.course.my.navigation.MyCourse
import com.office.meong.presentation.mypage.navigation.MyPage

enum class MainTab(
    @param:DrawableRes val selectedIcon: Int,
    @param:DrawableRes val unselectedIcon: Int,
    val contentDescription: String,
    val route: MainTabRoute,
) {
    HOME(
        selectedIcon = R.drawable.ic_home_filled,
        unselectedIcon = R.drawable.ic_home,
        contentDescription = "홈",
        route = Home,
    ),

    EXPLORE(
        selectedIcon = R.drawable.ic_explore_filled,
        unselectedIcon = R.drawable.ic_explore,
        contentDescription = "장소 탐색",
        route = Explore,
    ),

    MY_COURSE(
        selectedIcon = R.drawable.ic_course_filled,
        unselectedIcon = R.drawable.ic_course,
        contentDescription = "내 코스",
        route = MyCourse,
    ),

    FAVORITE(
        selectedIcon = R.drawable.ic_favorite_filled,
        unselectedIcon = R.drawable.ic_favorite,
        contentDescription = "관심 장소",
        route = Favorite,
    ),

    MY_PAGE(
        selectedIcon = R.drawable.ic_person_filled,
        unselectedIcon = R.drawable.ic_person,
        contentDescription = "마이페이지",
        route = MyPage,
    );
    companion object {
        fun find(predicate: (MainTabRoute) -> Boolean): MainTab? {
            return entries.find { predicate(it.route) }
        }

        fun contains(predicate: (MainTabRoute) -> Boolean): Boolean {
            return entries.any { predicate(it.route) }
        }
    }
}
