package com.office.meong.presentation.course.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import com.office.meong.presentation.course.detail.model.DetailCourseRouteIndicatorType

@Composable
fun DetailCourseRouteIndicator(
    routeLength: String,
    onRouteClick: () -> Unit,
    modifier: Modifier = Modifier,
    routeIndicatorType: DetailCourseRouteIndicatorType = DetailCourseRouteIndicatorType.START
) {
    val routeText = when (routeIndicatorType) {
        DetailCourseRouteIndicatorType.START -> "첫 장소까지 약 $routeLength km"
        DetailCourseRouteIndicatorType.BETWEEN -> "이동 거리 약 $routeLength km"
        DetailCourseRouteIndicatorType.END -> "숙소까지 약 $routeLength km"
    }

    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            ImageVector.vectorResource(R.drawable.ic_arrow_down),
            null,
            Modifier.size(16.dp),
            MeongTheme.colors.gray700
        )

        Spacer(Modifier.width(2.dp))

        Text(
            text = routeText,
            style = MeongTheme.typography.body.body12M,
            color = MeongTheme.colors.gray600
        )

        Spacer(Modifier.width(6.dp))

        VerticalDivider(
            thickness = 1.dp,
            color = MeongTheme.colors.gray200,
            modifier = Modifier.fillMaxHeight().padding(vertical = 6.dp)
        )

        Spacer(Modifier.width(6.dp))

        Row(
            modifier = Modifier
                .noRippleClickable(onClick = onRouteClick).padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "카카오맵으로 경로 보기",
                style = MeongTheme.typography.label.label12Sb,
                color = MeongTheme.colors.gray900
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                MeongTheme.colors.gray900
            )
        }
    }
}

@Preview
@Composable
private fun DetailCourseRouteIndicatorPreview() {
    MeongTheme {
        DetailCourseRouteIndicator(
            routeLength = "1.2",
            onRouteClick = {},
            routeIndicatorType = DetailCourseRouteIndicatorType.START
        )
    }
}
