package com.office.meong.presentation.mypage.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun MyPageSectionItem(
    itemName: String,
    modifier: Modifier = Modifier,
    trailingText: String? = null,
    showChevron: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .padding(vertical = 14.dp)
            .let {
                if (onClick != null) it.noRippleClickable(onClick = onClick) else it },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = itemName,
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray700
        )

        Spacer(modifier = Modifier.weight(1f))

        if (trailingText != null) {
            Text(
                text = "v${trailingText}",
                style = MeongTheme.typography.label.label12Sb,
                color = MeongTheme.colors.gray500,
                modifier = Modifier
                    .padding(end = 7.dp)
            )
        }

        if (showChevron) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = MeongTheme.colors.gray500,
                modifier = Modifier
                    .size(20.dp)
            )
        }
    }
}

@Preview
@Composable
private fun MyPageSectionItemPreview() {
    MeongTheme {
        MyPageSectionItem(
            itemName = "서비스 이용약관",
            onClick = {}
        )
    }
}