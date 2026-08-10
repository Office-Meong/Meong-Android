package com.office.meong.presentation.mypage.petedit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun PetEditNeuteredToggle(
    isNeutered: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "중성화 여부",
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray900
        )

        Switch(
            checked = isNeutered,
            onCheckedChange = onCheckedChange
        )
    }
}
