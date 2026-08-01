package com.office.meong.presentation.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun SignUpTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "반려견 이름",
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray900
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "15자 이내로 입력해주세요",
            style = MeongTheme.typography.label.label12Sb,
            color = MeongTheme.colors.gray500
        )
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .background(MeongTheme.colors.gray50, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            textStyle = MeongTheme.typography.body.body14M.copy(color = MeongTheme.colors.gray900),
            decorator = { innerTextField ->
                if (state.text.isEmpty()) {
                    Text(
                        text = "반려견 이름을 입력해주세요",
                        style = MeongTheme.typography.body.body14M,
                        color = MeongTheme.colors.gray400
                    )
                }
                innerTextField()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpTextFieldPreview() {
    MeongTheme {
        SignUpTextField(
            state = TextFieldState(),
            modifier = Modifier.padding(20.dp)
        )
    }
}