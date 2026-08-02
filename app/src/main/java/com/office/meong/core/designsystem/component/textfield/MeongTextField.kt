package com.office.meong.core.designsystem.component.textfield

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun MeongTextField(
    state: TextFieldState,
    placeholder: String,
    modifier: Modifier = Modifier,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    shape: Shape = RoundedCornerShape(10.dp),
    containerColor: Color = MeongTheme.colors.gray50,
    textColor: Color = MeongTheme.colors.gray900,
    tint: Color = MeongTheme.colors.gray400,
    onTrailingIconClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val borderColor = MeongTheme.colors.red

    BasicTextField(
        state = state,
        enabled = enabled,
        readOnly = readOnly,
        inputTransformation = inputTransformation,
        outputTransformation = outputTransformation,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = lineLimits,
        textStyle = MeongTheme.typography.body.body14M.copy(color = textColor),
        cursorBrush = SolidColor(if (isError) MeongTheme.colors.red else MeongTheme.colors.primary),
        interactionSource = interactionSource,
        decorator = { innerTextField ->
            Row(
                modifier = modifier
                    .styleable {
                        fillWidth()
                        background(containerColor)
                        shape(shape)
                        if (isError) border(1.dp, color = borderColor)
                        contentPadding(vertical = 12.dp, horizontal = 16.dp)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = leadingIcon),
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MeongTheme.typography.body.body14M,
                            color = MeongTheme.colors.gray500
                        )
                    }

                    innerTextField()
                }

                if (trailingIcon != null) {
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = ImageVector.vectorResource(id = trailingIcon),
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier
                            .size(20.dp)
                            .noRippleClickable(
                                onClick = onTrailingIconClick
                            )
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun MeongTextFieldPreview() {
    MeongTheme {
        MeongTextField(
            state = TextFieldState(),
            placeholder = "내용을 입력해주세요",
            leadingIcon = R.drawable.ic_search,
            trailingIcon = R.drawable.ic_close_filled,
            isError = true
        )
    }
}
