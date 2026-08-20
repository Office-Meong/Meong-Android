package com.office.meong.presentation.mypage.petedit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.designsystem.component.image.UrlImage
import com.office.meong.core.designsystem.component.indicator.MeongLoadingIndicator
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun PetEditImagePicker(
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Box(
        modifier = modifier
            .size(120.dp)
            .clickable(
                enabled = !isLoading,
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        if (imageUrl.isNullOrBlank()) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_empty_pet_holder),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(120.dp)
            )
        } else {
            UrlImage(
                url = imageUrl,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(40.dp)
                .clip(CircleShape)
                .background(MeongTheme.colors.gray100),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_camera_filled),
                contentDescription = "사진 변경",
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
        }

        if (isLoading) {
            MeongLoadingIndicator(
                modifier = Modifier.clip(CircleShape),
                backgroundColor = MeongTheme.colors.white.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview
@Composable
private fun PetEditImagePickerPreview() {
    MeongTheme {
        PetEditImagePicker(
            imageUrl = null,
            onClick = {}
        )
    }
}
