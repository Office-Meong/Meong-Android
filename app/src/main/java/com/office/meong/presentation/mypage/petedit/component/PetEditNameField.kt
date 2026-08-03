package com.office.meong.presentation.mypage.petedit.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.component.textfield.MeongTextField
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun PetEditNameField(
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

        MeongTextField(
            state = state,
            placeholder = "반려견 이름을 입력해주세요",
            modifier = Modifier.fillMaxWidth()
        )
    }
}
