package com.office.meong.presentation.sharedcomponent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.component.image.UrlImage
import com.office.meong.core.designsystem.theme.MeongTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PetProfileCard(
    petName: String,
    imageUrl: String?,
    tags: ImmutableList<String>,
    modifier: Modifier = Modifier,
    isBordered: Boolean = false,
    backgroundColor: Color = MeongTheme.colors.white,
    borderColor: Color = MeongTheme.colors.gray100,
) {
    Row(
        modifier = modifier
            .styleable {
                fillWidth()
                shape(RoundedCornerShape(20.dp))
                background(color = backgroundColor)
                if (isBordered) border(width = 1.dp, color = borderColor)
                contentPadding(22.dp)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            UrlImage(
                url = imageUrl,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_empty_pet_holder),
                contentDescription = "empty pet holder",
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = petName,
                style = MeongTheme.typography.body.body14M,
                color = MeongTheme.colors.gray900
            )

            FlowRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tags.forEach { data ->
                    MeongChip(
                        chipText = data,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PetProfileCardPreview() {
    MeongTheme {
        PetProfileCard(
            petName = "몽몽이",
            imageUrl = "",
            tags = persistentListOf("소형견","활동량 보통", "사회성 보통"),
            modifier = Modifier
        )
    }
}
