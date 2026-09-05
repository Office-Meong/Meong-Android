package com.office.meong.presentation.explore.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun ExploreEmptyContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MeongTheme.colors.gray50),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Spacer(modifier = Modifier.weight(133f))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_info),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp),
            tint = MeongTheme.colors.gray200
        )

        Text(
            text = "검색 결과가 없어요",
            style = MeongTheme.typography.title.title16Sb,
            color = MeongTheme.colors.gray800
        )

        Text(
            text = "다른 조건으로 검색해보세요",
            style = MeongTheme.typography.body.body14M,
            color = MeongTheme.colors.gray500
        )

        Spacer(modifier = Modifier.weight(287f))
    }
}

@Preview
@Composable
private fun ExploreEmptyContentPreview() {
    MeongTheme {
        ExploreEmptyContent()
    }
}