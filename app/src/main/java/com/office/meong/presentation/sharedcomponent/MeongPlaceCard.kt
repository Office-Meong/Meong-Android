package com.office.meong.presentation.sharedcomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.styleable
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
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.place.PlaceType

@Composable
fun MeongPlaceCard(
    placeName: String,
    location: String,
    placeType: PlaceType,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    grade: String? = null,
    isBordered: Boolean = false,
    isSelected: Boolean = false
) {
    val backgroundColor = MeongTheme.colors.white
    val iconBackgroundColor = MeongTheme.colors.gray100
    val borderColor = MeongTheme.colors.gray100
    val selectedBorderColor = MeongTheme.colors.primary

    Row(
        modifier = modifier
            .styleable {
                fillWidth()
                background(backgroundColor)
                shape(RoundedCornerShape(20.dp))
                contentPadding(20.dp)
                if (isBordered) border(1.dp, color = if (isSelected) selectedBorderColor else borderColor)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MeongChip(
                    chipText = placeType.label,
                    isActivated = false
                )

                if (grade != null) {
                    MeongChip(
                        chipText = "펫-워크 $grade",
                        isActivated = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = placeName,
                style = MeongTheme.typography.title.title16Sb,
                color = MeongTheme.colors.gray900
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_location_filled),
                    contentDescription = "위치",
                    tint = MeongTheme.colors.gray600,
                    modifier = Modifier.size(16.dp)
                )

                Text(
                    text = location,
                    style = MeongTheme.typography.body.body12M,
                    color = MeongTheme.colors.gray600
                )
            }
        }

        Box(
            modifier = Modifier
                .styleable {
                    background(iconBackgroundColor)
                    shape(RoundedCornerShape(12.dp))
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.padding(26.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(placeType.iconRes),
                    contentDescription = null,
                    tint = MeongTheme.colors.gray300
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .noRippleClickable(onClick = onFavoriteClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFavorite) ImageVector.vectorResource(R.drawable.ic_favorite_stroke_filled) else ImageVector.vectorResource(R.drawable.ic_favorite_stroke),
                    contentDescription = "즐겨찾기",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MeongPlaceCardPreview() {
    MeongTheme {
        MeongPlaceCard(
            placeName = "몽멍이 카페",
            location = "서울시 강남구",
            grade = "A",
            placeType = PlaceType.RESTAURANT,
            isFavorite = true,
            isBordered = true,
            isSelected = true,
            onFavoriteClick = {}
        )
    }
}
