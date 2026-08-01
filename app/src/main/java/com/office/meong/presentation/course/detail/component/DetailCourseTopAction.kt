package com.office.meong.presentation.course.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun DetailCourseTopAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "코스 삭제"
) {
    Row(
        modifier = modifier
            .background(
                color = MeongTheme.colors.white,
                shape = RoundedCornerShape(10.dp)
            )
            .noRippleClickable(onClick = onClick)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_trash),
            contentDescription = null,
            tint = MeongTheme.colors.red,
            modifier = Modifier
                .size(16.dp)
        )

        Text(
            text = text,
            style = MeongTheme.typography.label.label12Sb,
            color = MeongTheme.colors.red
        )
    }
}

@Preview
@Composable
private fun DetailCourseTopActionPreview() {
    MeongTheme {
        DetailCourseTopAction(
            onClick = {}
        )
    }
}