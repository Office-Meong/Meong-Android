package com.office.meong.core.designsystem.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.disabled
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun MeongSmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    containerColor: Color = MeongTheme.colors.gray700,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val styleState = rememberUpdatedStyleState(interactionSource) {
        it.isEnabled = isEnabled
    }
    val defaultContentColor = MeongTheme.colors.white

    val disabledBgColor = MeongTheme.colors.gray50
    val disabledContentColor = MeongTheme.colors.gray700

    Box(
        modifier = modifier
            .styleable(styleState) {
                background(containerColor)
                shape(RoundedCornerShape(8.dp))
                contentColor(defaultContentColor)
                contentPadding(vertical = 10.dp, horizontal = 44.dp)

                disabled {
                    background(disabledBgColor)
                    contentColor(disabledContentColor)
                }
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MeongTheme.typography.label.label14Sb,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun MeongSmallButtonPreview() {
    MeongTheme {
        Column {
            MeongSmallButton(
                text = "취소",
                onClick = {},
                isEnabled = false
            )

            MeongSmallButton(
                text = "확인",
                onClick = {},
                isEnabled = true
            )
        }
    }
}
