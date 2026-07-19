package com.office.meong.presentation.course.create.component.setting

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private val DefaultWorkcationStyles = persistentListOf(
    "업무는 최소한으로, 여행을 마음껏 즐길래요",
    "일과 여행, 적당히 균형을 맞출래요",
    "일에 몰입할 수 있는 환경이 필요해요",
)

@Composable
fun CreateCourseDropdownSelector(
    title: String,
    placeholder: String,
    options: ImmutableList<String>,
    selectedOption: String?,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOptionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CreateCourseSettingTitle(title = title)

        CreateCourseDropdownHeader(
            text = selectedOption ?: placeholder,
            isPlaceholder = selectedOption == null,
            isExpanded = isExpanded,
            onClick = { onExpandedChange(!isExpanded) },
        )

        if (isExpanded) {
            CreateCourseDropdownOptionList(
                options = options,
                onOptionClick = onOptionClick,
            )
        }
    }
}

@Composable
private fun CreateCourseDropdownHeader(
    text: String,
    isPlaceholder: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconRotation = animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 220),
        label = "CreateCourseDropdownIconRotation",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MeongTheme.colors.gray50,
                shape = RoundedCornerShape(10.dp),
            )
            .noRippleClickable(onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = MeongTheme.typography.body.body14M,
            color = if (isPlaceholder) MeongTheme.colors.gray500 else MeongTheme.colors.gray900,
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_down),
            contentDescription = if (isExpanded) "접기" else "펼치기",
            modifier = Modifier
                .size(20.dp)
                .graphicsLayer {
                    rotationZ = iconRotation.value
                },
            tint = MeongTheme.colors.gray900,
        )
    }
}

@Composable
private fun CreateCourseDropdownOptionList(
    options: ImmutableList<String>,
    onOptionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MeongTheme.colors.gray50,
                shape = RoundedCornerShape(10.dp),
            )
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEach { option ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable { onOptionClick(option) }
                    .padding(8.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = option,
                    style = MeongTheme.typography.body.body14M,
                    color = MeongTheme.colors.gray900,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateCourseDropdownSelectorPreview() {
    MeongTheme {
        CreateCourseDropdownSelector(
            title = "워케이션 스타일",
            placeholder = "선호하는 워케이션 스타일을 선택해주세요",
            options = DefaultWorkcationStyles,
            selectedOption = null,
            isExpanded = true,
            onExpandedChange = {},
            onOptionClick = {},
        )
    }
}
