package com.office.meong.presentation.course.create.component.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.course.create.model.CreateCourseRangeInputType

@Composable
fun CreateCourseRangeInput(
    type: CreateCourseRangeInputType,
    modifier: Modifier = Modifier,
    startText: String? = null,
    endText: String? = null,
    onClick: () -> Unit = {},
    onStartClick: () -> Unit = onClick,
    onEndClick: () -> Unit = onClick,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CreateCourseSettingTitle(
            title = type.title,
            hasInfoIcon = type.hasInfoIcon,
            tooltipMessage = type.tooltipMessage,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CreateCourseRangeInputBox(
                text = startText ?: type.startPlaceholder,
                isPlaceholder = startText == null,
                modifier = Modifier.weight(1f),
                onClick = onStartClick,
            )

            Text(
                text = "-",
                modifier = Modifier.width(8.dp),
                style = MeongTheme.typography.title.title20Sb,
                color = MeongTheme.colors.gray500,
                textAlign = TextAlign.Center,
            )

            CreateCourseRangeInputBox(
                text = endText ?: type.endPlaceholder,
                isPlaceholder = endText == null,
                modifier = Modifier.weight(1f),
                onClick = onEndClick,
            )
        }
    }
}

@Composable
private fun CreateCourseRangeInputBox(
    text: String,
    isPlaceholder: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = MeongTheme.colors.gray50,
                shape = RoundedCornerShape(10.dp),
            )
            .noRippleClickable(onClick),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = text,
            style = MeongTheme.typography.body.body14M,
            color = if (isPlaceholder) MeongTheme.colors.gray500 else MeongTheme.colors.gray900,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

@Preview
@Composable
private fun CreateCourseSettingContentPreview() {
    MeongTheme {
        CreateCourseRangeInput(
            type = CreateCourseRangeInputType.WORKCATION_PERIOD,
            onClick = {},
            //onClick = { onRangeClick(CreateCourseRangeInputType.WORKCATION_PERIOD) },
        )
    }
}
