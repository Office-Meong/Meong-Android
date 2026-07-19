package com.office.meong.presentation.course.create.component.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.component.chip.ChipType
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.theme.MeongTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private val DefaultChips = persistentListOf("강릉", "춘천", "원주")

@Composable
fun CreateCourseChipSelector(
    title: String,
    chips: ImmutableList<String>,
    selectedChips: ImmutableList<String>,
    onChipClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedBackgroundColor: Color = MeongTheme.colors.gray600,
    selectedContentColor: Color = MeongTheme.colors.white,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CreateCourseSettingTitle(title = title)

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            chips.forEach { chip ->
                MeongChip(
                    chipText = chip,
                    chipType = ChipType.LARGE,
                    isSelected = chip in selectedChips,
                    selectedBackgroundColor = selectedBackgroundColor,
                    selectedContentColor = selectedContentColor,
                    modifier = Modifier.noRippleClickable { onChipClick(chip) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateCourseChipSelectorPreview() {
    MeongTheme {
        CreateCourseChipSelector(
            title = "지역 선택",
            chips = DefaultChips,
            selectedChips = persistentListOf(DefaultChips[0]),
            onChipClick = {},
        )
    }
}
