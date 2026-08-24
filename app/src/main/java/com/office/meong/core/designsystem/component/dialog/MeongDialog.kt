package com.office.meong.core.designsystem.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.component.dialog.action.DialogAction
import com.office.meong.core.designsystem.component.dialog.action.MeongCancelAction
import com.office.meong.core.designsystem.component.dialog.action.MeongConfirmAction
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun MeongDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    subDescription: String? = null,
    confirmAction: DialogAction = MeongConfirmAction(onClick = onDismiss),
    cancelAction: DialogAction? = null,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = false
    ),
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = properties,
    ) {
        MeongDialogContent(
            modifier = modifier,
            onDismiss = onDismiss,
            title = title,
            subDescription = subDescription,
            confirmAction = confirmAction,
            cancelAction = cancelAction,
        )
    }
}

@Composable
private fun MeongDialogContent(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    title: String? = null,
    subDescription: String? = null,
    confirmAction: DialogAction = MeongConfirmAction(onClick = onDismiss),
    cancelAction: DialogAction? = null,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MeongTheme.colors.black.copy(alpha = 0.7f))
            .noRippleClickable(onClick = onDismiss)
            .padding(horizontal = 50.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable(onClick = {})
                .background(
                    color = MeongTheme.colors.white,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(
                    horizontal = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            if (title != null) {
                Text(
                    text = title,
                    color = MeongTheme.colors.gray900,
                    style = MeongTheme.typography.body.body14M,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = subDescription.orEmpty(),
                color = MeongTheme.colors.gray500,
                style = MeongTheme.typography.body.body12M,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (cancelAction != null) {
                    cancelAction(modifier = Modifier.weight(1f))
                }

                confirmAction(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



@Preview
@Composable
private fun MeongDialogPreview() {
    MeongTheme {
        MeongDialogContent(
            onDismiss = {},
            title = "이전으로 돌아가시겠어요?",
            subDescription = "저장하지 않은 코스는 사라져요",

            cancelAction = MeongCancelAction(
                text = "취소",
                onClick = {}
            ),
            confirmAction = MeongConfirmAction(
                text = "로그아웃",
                onClick = {},
                backgroundColor = MeongTheme.colors.primary
            ),
        )
    }
}
