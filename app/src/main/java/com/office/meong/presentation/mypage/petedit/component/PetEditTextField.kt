package com.office.meong.presentation.mypage.petedit.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.component.textfield.MeongTextField
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun PetEditTextField(
    label: String,
    description: String,
    placeholder: String,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    errorMessage: String? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray900
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = description,
            style = MeongTheme.typography.label.label12Sb,
            color = MeongTheme.colors.gray500
        )

        Spacer(modifier = Modifier.height(8.dp))

        MeongTextField(
            state = state,
            placeholder = placeholder,
            keyboardOptions = keyboardOptions,
            inputTransformation = inputTransformation,
            outputTransformation = outputTransformation,
            isError = errorMessage != null,
            fieldModifier = fieldModifier,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = errorMessage,
                style = MeongTheme.typography.label.label12Sb,
                color = MeongTheme.colors.red
            )
        }
    }
}
