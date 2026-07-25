package com.office.meong.presentation.explore.navigation.component.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun ExploreDetailActionRow(
    onKakaoMapClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .background(color = MeongTheme.colors.white, shape = RoundedCornerShape(8.dp))
                .border(1.dp, MeongTheme.colors.gray100, RoundedCornerShape(8.dp))
                .noRippleClickable(onClick = onKakaoMapClick)
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "카카오맵에서 보기",
                style = MeongTheme.typography.label.label14Sb,
                color = MeongTheme.colors.gray900
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

        Row(
            modifier = Modifier
                .background(color = MeongTheme.colors.white, shape = RoundedCornerShape(8.dp))
                .border(1.dp, MeongTheme.colors.gray100, RoundedCornerShape(8.dp))
                .noRippleClickable(onClick = onFavoriteClick)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isFavorite) ImageVector.vectorResource(id = R.drawable.ic_favorite_filled) else ImageVector.vectorResource(id = R.drawable.ic_favorite),
                contentDescription = "관심",
                tint = if (isFavorite) MeongTheme.colors.red else MeongTheme.colors.gray900
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "관심",
                style = MeongTheme.typography.label.label14Sb,
                color = MeongTheme.colors.gray900
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreDetailActionRowPreview() {
    MeongTheme {
        ExploreDetailActionRow(
            onKakaoMapClick = {},
            onFavoriteClick = {},
            isFavorite = false,
        )
    }
}