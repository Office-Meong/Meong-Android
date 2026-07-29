package com.office.meong.presentation.sharedcomponent.skeleton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.styleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongShimmerColors
import com.office.meong.core.designsystem.theme.MeongTheme
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun meongShimmerTheme() = defaultShimmerTheme.copy(
    blendMode = BlendMode.SrcOver,
    shaderColors = listOf(
        MeongShimmerColors.base,
        MeongShimmerColors.highlight,
        MeongShimmerColors.base,
    )
)

@Composable
fun MeongPlaceCardSkeleton(
    shimmer: Shimmer,
    modifier: Modifier = Modifier
) {
    val backgroundColor = MeongTheme.colors.white
    val placeholderColor = MeongShimmerColors.base
    val density = LocalDensity.current

    val chipHeight = with(density) { MeongTheme.typography.label.label12Sb.lineHeight.toDp() } + 6.dp
    val titleHeight = with(density) { MeongTheme.typography.title.title16Sb.lineHeight.toDp() }
    val locationTextHeight = with(density) { MeongTheme.typography.body.body12M.lineHeight.toDp() }

    @Composable
    fun Modifier.placeholder(shape: RoundedCornerShape) = this
        .shimmer(shimmer)
        .styleable {
            shape(shape)
            background(placeholderColor)
        }

    Row(
        modifier = modifier
            .styleable {
                fillWidth()
                background(backgroundColor)
                shape(RoundedCornerShape(20.dp))
                contentPadding(20.dp)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(bottom = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(width = 52.dp, height = chipHeight)
                    .placeholder(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(width = 120.dp, height = titleHeight)
                    .placeholder(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = locationTextHeight)
                    .placeholder(RoundedCornerShape(4.dp))
            )
        }

        Box(
            modifier = Modifier
                .size(84.dp)
                .placeholder(RoundedCornerShape(12.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MeongPlaceCardSkeletonPreview() {
    MeongTheme {
        val shimmer = rememberShimmer(
            shimmerBounds = ShimmerBounds.View,
            theme = meongShimmerTheme()
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(3) {
                MeongPlaceCardSkeleton(shimmer = shimmer)
            }
        }
    }
}
