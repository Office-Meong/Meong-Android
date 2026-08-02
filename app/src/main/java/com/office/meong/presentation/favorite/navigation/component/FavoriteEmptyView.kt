package com.office.meong.presentation.favorite.navigation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.component.button.MeongPillButton
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun FavoriteEmptyView (
    onSearchClick: () -> Unit,
    modifier : Modifier = Modifier
){
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MeongTheme.colors.gray50
            ),
    ){
        Spacer(modifier = Modifier.height(133.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "저장된 관심 장소가 없어요",
                style = MeongTheme.typography.title.title16Sb,
                color = MeongTheme.colors.gray800,
            )

            Text(
                text = "마음에 드는 워케이션 장소를 탐색해 보세요!",
                style = MeongTheme.typography.body.body14M,
                color = MeongTheme.colors.gray500
            )

            Spacer(modifier = Modifier.padding(12.dp))

            MeongPillButton(
                text = "검색어",
                onClick = onSearchClick
            )
        }
    }
}


@Preview
@Composable
private fun FavoriteSearchChipPreview() {
    MeongTheme {

    }

        FavoriteEmptyView(
            onSearchClick = {}
        )
    }
