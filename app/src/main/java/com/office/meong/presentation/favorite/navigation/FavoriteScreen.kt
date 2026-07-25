package com.office.meong.presentation.favorite.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.favorite.navigation.component.FavoriteChipArea
import com.office.meong.presentation.favorite.navigation.component.FavoritePlaceType
import com.office.meong.presentation.favorite.navigation.component.FavoriteRegion


@Composable
fun FavoriteRoute(
    paddingValues: PaddingValues
) {
    FavoriteScreen(
        paddingValues = paddingValues
    )
}
@Composable
private fun FavoriteScreen(
    paddingValues: PaddingValues
) {
    var selectedRegion by remember { mutableStateOf(FavoriteRegion.ALL) }
    var selectedType by remember { mutableStateOf(FavoritePlaceType.ALL) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MeongTheme.colors.white)
            .padding(paddingValues)
    ) {
        Text(
            text = "관심 장소",
            style = MeongTheme.typography.title.title20Sb,
            color = MeongTheme.colors.gray900,
            modifier = Modifier.padding(start = 20.dp, top = 10.dp)
        )

        Spacer(modifier = Modifier.height(34.dp))

        FavoriteChipArea(
            selectedRegion = selectedRegion,
            selectedType = selectedType,
            onRegionSelected = { selectedRegion = it },
            onTypeSelected = { selectedType = it },
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MeongTheme.colors.gray100,
            thickness = 1.dp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MeongTheme.colors.gray50)
                .weight(1f),
            contentPadding = PaddingValues(all = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item{
                Text(
                    text ="총\${0}개의 장소",
                    color = MeongTheme.colors.gray700,
                    style = MeongTheme.typography.label.label14Sb
                )
            }

            //TODO: 민성 컴포 붙히기 + Loading state시 EmptyView 붙히기
        }

    }

}


@Preview
@Composable
private fun FavoriteScreenPreview() {
    MeongTheme {
        FavoriteScreen(
            paddingValues = PaddingValues()
        )
    }
}