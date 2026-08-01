package com.office.meong.core.designsystem.component.dialog.action

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.office.meong.core.designsystem.component.button.MeongSmallButton
import com.office.meong.core.designsystem.theme.MeongTheme

class MeongCancelAction(
    private val text: String = "취소",
    private val onClick: () -> Unit
): DialogAction {
    @Composable
    override fun invoke(modifier: Modifier) {
        MeongSmallButton(
            text = text,
            onClick = onClick,
            isEnabled = false,
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun MeongCancelActionPreview() {
    MeongTheme {
        MeongCancelAction(onClick = {})
            .invoke(modifier = Modifier)
    }
}
