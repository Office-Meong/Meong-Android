package com.office.meong.presentation.favorite.navigation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.component.tag.MeongTag
import com.office.meong.core.designsystem.theme.MeongTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FavoriteChipArea(
    selectedRegion: FavoriteRegion,
    selectedType: FavoritePlaceType,
    onRegionSelected: (FavoriteRegion) -> Unit,
    onTypeSelected: (FavoritePlaceType) -> Unit,
    modifier: Modifier = Modifier
) {
    val regionItems = FavoriteRegion.entries.toImmutableList()
    val typeItems = FavoritePlaceType.entries.toImmutableList()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FilterRow(
            label = "지역",
            items = regionItems,
            selectedItem = selectedRegion,
            onItemSelected = onRegionSelected,
            itemLabel = { it.label }
        )

        FilterRow(
            label = "유형",
            items = typeItems,
            selectedItem = selectedType,
            onItemSelected = onTypeSelected,
            itemLabel = { it.label }
        )
    }
}

@Composable
private fun <T> FilterRow(
    label: String,
    items: ImmutableList<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MeongTheme.colors.gray700,
            style = MeongTheme.typography.label.label14Sb
        )

        Spacer(modifier = Modifier.width(16.dp))

        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 20.dp)
        ) {
            items(
                items = items,
                key = { item -> item.hashCode() }
            ) { item ->
                MeongTag(
                    text = itemLabel(item),
                    isSelected = item == selectedItem,
                    onClick = { onItemSelected(item) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteChipAreaPreview() {
    MeongTheme {
        FavoriteChipArea(
            selectedRegion = FavoriteRegion.ALL,
            selectedType = FavoritePlaceType.ALL,
            onRegionSelected = {},
            onTypeSelected = {}
        )
    }
}