package com.office.meong.presentation.course.result.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.place.PlaceType

@Composable
fun ResultCourseEditScheduleItem(
    placeType: PlaceType,
    placeName: String,
    onDragStart: () -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val currentOnDragStart = rememberUpdatedState(onDragStart)
    val currentOnDrag = rememberUpdatedState(onDrag)
    val currentOnDragEnd = rememberUpdatedState(onDragEnd)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ResultCoursePlaceSummaryItem(
            placeType = placeType.label,
            placeName = placeName,
            modifier = Modifier
                .weight(1f)
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_drag_indicator),
            contentDescription = "드래그",
            tint = MeongTheme.colors.gray700,
            modifier = Modifier
                .size(16.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { currentOnDragStart.value() },
                        onDragEnd = { currentOnDragEnd.value() },
                        onDragCancel = { currentOnDragEnd.value() },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            currentOnDrag.value(dragAmount)
                        }
                    )
                }
        )
    }
}

@Preview
@Composable
private fun ResultCourseEditScheduleItemPreview() {
    MeongTheme {
        ResultCourseEditScheduleItem(
            placeType = PlaceType.RESTAURANT,
            placeName = "프렌즈애견펜션",
            onDragStart = {},
            onDrag = {},
            onDragEnd = {}
        )
    }
}
