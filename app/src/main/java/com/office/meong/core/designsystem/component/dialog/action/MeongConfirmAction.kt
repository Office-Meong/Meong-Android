package com.office.meong.core.designsystem.component.dialog.action

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.office.meong.core.designsystem.component.button.MeongSmallButton
import com.office.meong.core.designsystem.theme.MeongTheme

class MeongConfirmAction(
    private val text: String = "확인",
    private val backgroundColor: Color? = null,
    private val onClick: () -> Unit
): DialogAction {
    @Composable
    override fun invoke(modifier: Modifier) {
        MeongSmallButton(
            text = text,
            onClick = onClick,
            isEnabled = true,
            containerColor = backgroundColor ?: MeongTheme.colors.gray700,
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun MeongConfirmActionPreview() {
    MeongTheme {
        MeongConfirmAction(onClick = {}, backgroundColor = MeongTheme.colors.gray700)
            .invoke(modifier = Modifier)
    }
}
