package com.office.meong.presentation.course.detail.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.common.extension.styleableBackground
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.place.PlaceType

@Composable
fun DetailCourseEditScheduleItem(
    placeType: PlaceType,
    placeName: String,
    onDragStart: () -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    onChangePlaceClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnDragStart = rememberUpdatedState(onDragStart)
    val currentOnDrag = rememberUpdatedState(onDrag)
    val currentOnDragEnd = rememberUpdatedState(onDragEnd)

    var isExpanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .indication(interactionSource, LocalIndication.current)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .styleableBackground(
                    color = MeongTheme.colors.white,
                    shape = RoundedCornerShape(10.dp)
                )
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { isExpanded = !isExpanded }
                    .padding(vertical = 14.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MeongChip(
                    chipText = placeType.label,
                    isActivated = false
                )

                Text(
                    text = placeName,
                    style = MeongTheme.typography.label.label14Sb,
                    color = MeongTheme.colors.gray900
                )
            }

            if (isExpanded) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MeongTheme.colors.gray100
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Text(
                        text = "다른 장소로 변경",
                        style = MeongTheme.typography.label.label12Sb,
                        color = MeongTheme.colors.gray900,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .noRippleClickable {
                                isExpanded = false
                                onChangePlaceClick()
                            }
                            .padding(vertical = 14.dp, horizontal = 61.dp)
                    )

                    VerticalDivider(
                        thickness = 1.dp,
                        color = MeongTheme.colors.gray100,
                        modifier = Modifier.fillMaxHeight()
                    )

                    Text(
                        text = "삭제",
                        style = MeongTheme.typography.label.label12Sb,
                        color = MeongTheme.colors.red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .noRippleClickable {
                                isExpanded = false
                                onDeleteClick()
                            }
                            .padding(vertical = 14.dp, horizontal = 38.dp)
                    )
                }
            }
        }

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_drag_indicator),
            contentDescription = "드래그",
            tint = MeongTheme.colors.gray700,
            modifier = Modifier
                .align(Alignment.CenterVertically)
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
private fun DetailCourseEditScheduleItemPreview() {
    MeongTheme {
        DetailCourseEditScheduleItem(
            placeType = PlaceType.RESTAURANT,
            placeName = "프렌즈애견펜션",
            onDragStart = {},
            onDrag = {},
            onDragEnd = {},
            onChangePlaceClick = {},
            onDeleteClick = {}
        )
    }
}
