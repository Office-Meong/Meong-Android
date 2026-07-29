package com.office.meong.presentation.course.result.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun ResultCoursePlaceSummaryItem(
    placeType: String,
    placeName: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = MeongTheme.colors.white

    Row(
        modifier = modifier
            .styleable {
                fillWidth()
                background(backgroundColor)
                shape(RoundedCornerShape(10.dp))
                contentPadding(vertical = 14.dp, horizontal = 16.dp)
            },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MeongChip(
            chipText = placeType,
            isActivated = false
        )

        Text(
            text = placeName,
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray900
        )
    }
}

@Preview
@Composable
private fun ResultCoursePlaceSummaryItemPreview() {
    MeongTheme {
        ResultCoursePlaceSummaryItem(
            placeType = "숙소",
            placeName = "프렌즈애견펜션"
        )
    }
}

