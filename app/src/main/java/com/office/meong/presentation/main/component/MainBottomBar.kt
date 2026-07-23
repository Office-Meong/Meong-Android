package com.office.meong.presentation.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.main.MainTab
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MainBottomBar(
    isVisible: Boolean,
    tabs: ImmutableList<MainTab>,
    currentTab: MainTab?,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strokeColor = MeongTheme.colors.gray100

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) },
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = MeongTheme.colors.white,
                )
                .drawBehind {
                    drawLine(
                        color = strokeColor,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1f,
                    )
                }
                .selectableGroup(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                key(tab.route) {
                    MainNavigationBarItem(
                        tab = tab,
                        selected = tab == currentTab,
                        onClick = { onTabSelected(tab) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MainNavigationBarItem(
    selected: Boolean,
    tab: MainTab,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconRes = if (selected) tab.selectedIcon else tab.unselectedIcon
    val selectedColor = if (selected) Color.Unspecified else MeongTheme.colors.gray500

    Column (
        modifier = modifier
            .padding(vertical = 13.dp)
            .noRippleClickable(onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = tab.contentDescription,
            tint = selectedColor,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = tab.contentDescription,
            style = MeongTheme.typography.label.label10Sb,
            color = MeongTheme.colors.gray500,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun MainBottomBarPreview() {
    MeongTheme {
        val dummyTabs: ImmutableList<MainTab> = persistentListOf(
            MainTab.HOME,
            MainTab.EXPLORE,
            MainTab.MY_COURSE,
            MainTab.FAVORITE,
            MainTab.MY_PAGE
        )

        val currentTab by remember { mutableStateOf(MainTab.HOME) }

        MainBottomBar(
            isVisible = true,
            tabs = dummyTabs,
            currentTab = currentTab,
            onTabSelected = {}
        )
    }
}
