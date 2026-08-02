package com.office.meong.presentation.explore.navigation

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
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import com.office.meong.presentation.explore.navigation.component.ExploreChipArea
import com.office.meong.presentation.explore.navigation.component.ExploreSearchBar
import com.office.meong.presentation.explore.navigation.model.ExplorePlaceType
import com.office.meong.presentation.explore.navigation.model.ExploreRegion

@Composable
fun ExploreRoute(
    paddingValues: PaddingValues
) {
    ExploreScreen(
        paddingValues = paddingValues
    )
}

@Composable
private fun ExploreScreen(
    paddingValues: PaddingValues
) {
    val searchState = rememberTextFieldState()
    var selectedRegion by remember { mutableStateOf(ExploreRegion.ALL) }
    var selectedType by remember { mutableStateOf(ExplorePlaceType.ALL) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MeongTheme.colors.gray50)
            .padding(paddingValues)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ExploreSearchBar(
            state = searchState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ExploreChipArea(
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

            //TODO: 민성 컴포 붙히기 + Loading state시 Skeleton 붙히기
        }
    }
}

@Preview
@Composable
private fun ExploreScreenPreview() {
    MeongTheme {
        ExploreScreen(
            paddingValues = PaddingValues()
        )
    }
}