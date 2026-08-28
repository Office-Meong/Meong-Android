package com.office.meong.presentation.course.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.place.PlaceType
import com.office.meong.presentation.sharedcomponent.MeongPlaceCard

@Composable
fun DetailCourseAccommodationSection(
    placeName: String,
    location: String,
    isFavorite: Boolean,
    onChangeAccommodationClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    grade: String? = null,
    thumbnailUrl: String? = null,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "숙소",
                style = MeongTheme.typography.label.label14Sb,
                color = MeongTheme.colors.gray900
            )

            Text(
                text = "숙소 변경",
                style = MeongTheme.typography.label.label14Sb,
                color = MeongTheme.colors.gray900,
                modifier = Modifier.noRippleClickable(onClick = onChangeAccommodationClick)
            )
        }

        Spacer(Modifier.height(4.dp))

        MeongPlaceCard(
            placeName = placeName,
            location = location,
            grade = grade,
            isFavorite = isFavorite,
            onFavoriteClick = onFavoriteClick,
            placeType = PlaceType.ACCOMMODATION,
            thumbnailUrl = thumbnailUrl,
            modifier = Modifier.noRippleClickable(onClick = onClick)
        )
    }
}

@Preview
@Composable
private fun DetailCourseAccommodationSectionPreview() {
    MeongTheme {
        DetailCourseAccommodationSection(
            placeName = "프렌즈애견펜션",
            location = "강원 강릉시 하남길 117-4",
            grade = "A",
            isFavorite = true,
            onChangeAccommodationClick = {},
            onFavoriteClick = {}
        )
    }
}
