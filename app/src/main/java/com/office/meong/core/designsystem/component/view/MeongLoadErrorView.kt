package com.office.meong.core.designsystem.component.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.designsystem.component.button.MeongPillButton
import com.office.meong.core.designsystem.component.button.MeongPillButtonStyle
import com.office.meong.core.designsystem.component.topbar.MeongTopbar
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun MeongLoadErrorView(
    action: LoadErrorViewAction,
    modifier: Modifier = Modifier,
) {
    val content = action.content

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MeongTheme.colors.white),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            action.onBackClick?.let { onBackClick ->
                MeongTopbar(
                    isBackVisible = true,
                    onBackClick = onBackClick,
                )
            }

            Spacer(Modifier.weight(2f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = content.title,
                    color = MeongTheme.colors.gray900,
                    style = MeongTheme.typography.title.title20Sb,
                    textAlign = TextAlign.Center,
                )

                content.description?.let { description ->
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = description,
                        color = MeongTheme.colors.gray500,
                        style = MeongTheme.typography.body.body14M,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                MeongPillButton(
                    text = content.buttonText,
                    style = MeongPillButtonStyle.Primary,
                    onClick = action.onActionButtonClick,
                )
            }

            Spacer(Modifier.weight(3f))
        }
    }
}

@Stable
sealed class LoadErrorViewAction private constructor(
    val handleAction: LoadErrorHandleAction,
    val onActionButtonClick: () -> Unit,
    open val onBackClick: (() -> Unit)?,
) {
    data class Retry(
        val onRetryClick: () -> Unit,
        override val onBackClick: (() -> Unit)? = null,
    ) : LoadErrorViewAction(
        handleAction = LoadErrorHandleAction.Retry,
        onActionButtonClick = onRetryClick,
        onBackClick = onBackClick,
    )

    data class Back(
        override val onBackClick: () -> Unit,
    ) : LoadErrorViewAction(
        handleAction = LoadErrorHandleAction.Back,
        onActionButtonClick = onBackClick,
        onBackClick = onBackClick,
    )

    data class NotFound(
        override val onBackClick: () -> Unit,
    ) : LoadErrorViewAction(
        handleAction = LoadErrorHandleAction.NotFound,
        onActionButtonClick = onBackClick,
        onBackClick = onBackClick,
    )
}

private data class LoadErrorContent(
    val title: String,
    val description: String?,
    val buttonText: String,
)

private val LoadErrorViewAction.content: LoadErrorContent
    get() = when (handleAction) {
        LoadErrorHandleAction.Retry -> LoadErrorContent(
            title = "일시적인 오류가 발생해\n내용을 불러오지 못했어요.",
            description = null,
            buttonText = "다시 시도",
        )

        LoadErrorHandleAction.Back -> LoadErrorContent(
            title = "정보를 불러오지 못했어요.",
            description = "이전 화면으로 돌아가 다시 확인해주세요.",
            buttonText = "이전 화면으로 돌아가기",
        )

        LoadErrorHandleAction.NotFound -> LoadErrorContent(
            title = "요청한 내용을 찾을 수 없어요",
            description = "삭제되었거나 더 이상\n제공되지 않는 내용이에요",
            buttonText = "이전 화면으로 돌아가기",
        )
    }

@Preview(name = "Retry")
@Composable
private fun MeongLoadErrorRetryViewPreview() {
    MeongTheme {
        MeongLoadErrorView(
            action = LoadErrorViewAction.Retry(
                onRetryClick = {},
            ),
        )
    }
}

@Preview(name = "Back")
@Composable
private fun MeongLoadErrorBackViewPreview() {
    MeongTheme {
        MeongLoadErrorView(
            action = LoadErrorViewAction.Back(
                onBackClick = {},
            ),
        )
    }
}

@Preview(name = "Not Found")
@Composable
private fun MeongLoadErrorNotFoundViewPreview() {
    MeongTheme {
        MeongLoadErrorView(
            action = LoadErrorViewAction.NotFound(
                onBackClick = {},
            ),
        )
    }
}
