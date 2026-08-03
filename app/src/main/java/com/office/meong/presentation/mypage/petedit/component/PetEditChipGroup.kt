package com.office.meong.presentation.mypage.petedit.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.component.chip.ChipType
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.mypage.petedit.model.PetProfileAttribute
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> PetEditChipGroup(
    title: String,
    items: ImmutableList<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) where T : Enum<T>, T : PetProfileAttribute {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray900
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                MeongChip(
                    chipText = item.label,
                    chipType = ChipType.LARGE,
                    isSelected = item == selectedItem,
                    modifier = Modifier.clickable { onItemSelected(item) }
                )
            }
        }
    }
}
