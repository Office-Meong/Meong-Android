package com.office.meong.presentation.course.detail.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.office.meong.core.designsystem.component.dialog.MeongDialog
import com.office.meong.core.designsystem.component.dialog.action.MeongConfirmAction
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun DetailCourseDeleteDialog(
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    MeongDialog(
        onDismiss = onDismiss,
        confirmAction = MeongConfirmAction(
            onClick = onDelete,
            backgroundColor = MeongTheme.colors.red,
            text = "삭제"
        ),
        modifier = modifier
    )
}

