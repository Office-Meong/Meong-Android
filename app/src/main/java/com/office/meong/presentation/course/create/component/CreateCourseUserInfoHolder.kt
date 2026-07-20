package com.office.meong.presentation.course.create.component

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

val fakeChipData: List<String> = listOf("소형견","활동량 보통", "사회성 보통")

@Composable
fun CreateCourseUserInfoHolder(
    url: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MeongTheme.colors.white,
    borderColor: Color = MeongTheme.colors.gray100
) {
    Row(
        modifier = modifier
            .styleable {
                fillWidth()
                shape(RoundedCornerShape(20.dp))
                background(color = backgroundColor)
                border(width = 1.dp, color = borderColor)
                contentPadding(22.dp)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        if (url.isNotEmpty()) {
            UrlImage(
                url = "",
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
                text = "몽몽이",
                style = MeongTheme.typography.body.body14M,
                color = MeongTheme.colors.gray900
            )

            FlowRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                fakeChipData.forEach { data ->
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
private fun CreateCourseUserInfoHolderPreview() {
    MeongTheme {
        CreateCourseUserInfoHolder(
            url = "",
            modifier = Modifier
        )
    }
}