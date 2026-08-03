package com.office.meong.presentation.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.component.image.UrlImage
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.mypage.model.MyPageInfoType
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MyPageUserInfoHolder(
    modifier: Modifier = Modifier,
    onPetInfoClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(
                color = MeongTheme.colors.white,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        MyPageUserInfoItem(
            infoType = MyPageInfoType.USER,
            infoTitle = "홍길동",
            infoContent = "길동@gmail.com",
            imageUrl = null
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = MeongTheme.colors.gray100,
            modifier = Modifier
                .fillMaxWidth()
        )

        MyPageUserInfoItem(
            infoType = MyPageInfoType.PET,
            infoTitle = "몽몽이",
            infoContent = "",
            onClick = onPetInfoClick
        )
    }
}

@Composable
private fun MyPageUserInfoItem(
    infoType: MyPageInfoType,
    infoTitle: String,
    infoContent: String,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = infoType.info,
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray700
        )

        Row(
            modifier = Modifier
                .noRippleClickable(onClick = onClick),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageUrl != null) {
                UrlImage(
                    url = imageUrl,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    imageVector = ImageVector.vectorResource(
                        if (infoType == MyPageInfoType.USER) R.drawable.ic_empty_user_holder else R.drawable.ic_empty_pet_holder
                    ),
                    contentDescription = "empty holder",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Column {
                Text(
                    text = infoTitle,
                    style = MeongTheme.typography.label.label14Sb,
                    color = MeongTheme.colors.gray900
                )

                Spacer(modifier = Modifier.height(2.dp))

                if (infoType == MyPageInfoType.USER) {
                    Text(
                        text = infoContent,
                        style = MeongTheme.typography.body.body12M,
                        color = MeongTheme.colors.gray500
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        persistentListOf("소형견", "활동량 보통", "사회성").forEachIndexed { index, item ->
                            Text(
                                text = item,
                                style = MeongTheme.typography.body.body12M,
                                color = MeongTheme.colors.gray500,
                            )

                            if (index < item.lastIndex) {
                                VerticalDivider(
                                    thickness = 1.dp,
                                    color = MeongTheme.colors.gray200,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (infoType == MyPageInfoType.PET) {
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                    tint = MeongTheme.colors.gray500,
                    modifier = Modifier
                        .size(20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun MyPageUserInfoHolderPreview() {
    MeongTheme {
        MyPageUserInfoHolder()
    }
}