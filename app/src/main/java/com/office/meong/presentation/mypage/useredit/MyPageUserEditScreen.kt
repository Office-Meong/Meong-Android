package com.office.meong.presentation.mypage.useredit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.R
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.extension.focusScrollable
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.common.util.UiState
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.component.indicator.MeongLoadingIndicator
import com.office.meong.core.designsystem.component.view.LoadErrorViewAction
import com.office.meong.core.designsystem.component.view.MeongLoadErrorView
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.presentation.sharedcomponent.CircularImagePicker
import com.office.meong.presentation.sharedcomponent.LabeledTextField

@Composable
fun MyPageUserEditRoute(
    paddingValues: PaddingValues,
    onCloseClick: () -> Unit = {},
    viewModel: UserEditViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is UserEditSideEffect.NavigateUp -> onCloseClick()
            is UserEditSideEffect.ShowSnackBar -> {
                globalUiEventHolder.showSnackbar(SnackbarState(message = it.message))
            }
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.onImageSelected(it.toString()) }
    }

    when (state.user) {
        is UiState.Loading -> {
            MeongLoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }

        is UiState.Failure -> {
            MeongLoadErrorView(
                action = LoadErrorViewAction.Retry(onRetryClick = viewModel::retryFetchUserInfo),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }

        is UiState.Empty -> Unit

        is UiState.Success -> {
            MyPageUserEditScreen(
                paddingValues = paddingValues,
                state = state,
                onCloseClick = onCloseClick,
                onImageClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onSaveClick = viewModel::onSaveClick
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MyPageUserEditScreen(
    paddingValues: PaddingValues,
    state: UserEditState,
    onCloseClick: () -> Unit,
    onImageClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeongTheme.colors.white)
            .padding(paddingValues)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "몽에서 사용할\n프로필을 수정해주세요.",
                    style = MeongTheme.typography.title.title20Sb,
                    color = MeongTheme.colors.gray900
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                    contentDescription = "닫기",
                    tint = MeongTheme.colors.gray500,
                    modifier = Modifier
                        .size(24.dp)
                        .noRippleClickable(onClick = onCloseClick)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            CircularImagePicker(
                imageUrl = state.imageUrl,
                onClick = onImageClick,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                isLoading = state.isImageUploading,
                emptyIconRes = R.drawable.ic_empty_user_holder
            )

            Spacer(modifier = Modifier.height(24.dp))

            LabeledTextField(
                label = "닉네임",
                description = "10자 이내로 입력해주세요",
                placeholder = "닉네임을 입력해주세요",
                state = state.nicknameTextFieldState,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                inputTransformation = InputTransformation.maxLength(10),
                errorMessage = state.nicknameErrorMessage,
                fieldModifier = Modifier.focusScrollable()
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

        AnimatedVisibility(
            visible = !WindowInsets.isImeVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            MeongButton(
                text = "저장하기",
                isEnabled = state.isSaveEnabled,
                onClick = onSaveClick,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
            )
        }
    }
}

@Preview
@Composable
private fun MyPageUserEditScreenPreview() {
    MeongTheme {
        MyPageUserEditScreen(
            paddingValues = PaddingValues(),
            state = UserEditState(),
            onCloseClick = {},
            onImageClick = {},
            onSaveClick = {}
        )
    }
}
