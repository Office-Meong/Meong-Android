package com.office.meong.presentation.mypage.petedit

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.R
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.extension.focusScrollable
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.selectableEntries
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.component.indicator.MeongLoadingIndicator
import com.office.meong.core.designsystem.component.textfield.BirthDateInputTransformation
import com.office.meong.core.designsystem.component.textfield.BirthDateOutputTransformation
import com.office.meong.core.designsystem.component.view.LoadErrorViewAction
import com.office.meong.core.designsystem.component.view.MeongLoadErrorView
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability
import com.office.meong.presentation.mypage.petedit.action.PetEditActions
import com.office.meong.presentation.mypage.petedit.component.PetEditChipGroup
import com.office.meong.presentation.mypage.petedit.component.PetEditImagePicker
import com.office.meong.presentation.mypage.petedit.component.PetEditNeuteredToggle
import com.office.meong.presentation.mypage.petedit.component.PetEditTextField
import kotlinx.collections.immutable.toPersistentList

@Composable
fun MyPagePetEditRoute(
    paddingValues: PaddingValues,
    onCloseClick: () -> Unit = {},
    viewModel: PetEditViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is PetEditSideEffect.NavigateUp -> onCloseClick()
            is PetEditSideEffect.ShowSnackBar -> {
                globalUiEventHolder.showSnackbar(SnackbarState(message = it.message))
            }
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.onImageSelected(it.toString()) }
    }

    val actions = remember(viewModel, photoPickerLauncher) {
        object : PetEditActions {
            override fun onImageClick() {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
            override fun onNeuteredToggle(isNeutered: Boolean) = viewModel.onNeuteredToggle(isNeutered)
            override fun onSizeSelect(size: PetSizeCategory) = viewModel.onSizeSelect(size)
            override fun onActivitySelect(activity: PetActivityLevel) = viewModel.onActivitySelect(activity)
            override fun onSociabilitySelect(sociability: PetSociability) =
                viewModel.onSociabilitySelect(sociability)
            override fun onHealthSelect(health: PetHealthStatus) = viewModel.onHealthSelect(health)
            override fun onSaveClick() = viewModel.onSaveClick()
        }
    }

    when (state.pet) {
        is UiState.Loading -> {
            MeongLoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }

        is UiState.Failure -> {
            MeongLoadErrorView(
                action = LoadErrorViewAction.Retry(onRetryClick = viewModel::retryFetchDog),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }

        is UiState.Empty -> Unit

        is UiState.Success -> {
            MyPagePetEditScreen(
                paddingValues = paddingValues,
                state = state,
                onCloseClick = onCloseClick,
                actions = actions
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MyPagePetEditScreen(
    paddingValues: PaddingValues,
    state: PetEditState,
    onCloseClick: () -> Unit,
    actions: PetEditActions
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
                    text = "함께 워케이션을 떠날\n반려견 정보를 알려주세요.",
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

            PetEditImagePicker(
                imageUrl = state.imageUrl,
                onClick = actions::onImageClick,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                isLoading = state.isImageUploading
            )

            Spacer(modifier = Modifier.height(24.dp))

            PetEditTextField(
                label = "반려견 이름",
                description = "15자 이내로 입력해주세요",
                placeholder = "반려견 이름을 입력해주세요",
                state = state.nameTextFieldState,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                inputTransformation = InputTransformation.maxLength(15),
                errorMessage = state.nameErrorMessage,
                fieldModifier = Modifier.focusScrollable()
            )

            Spacer(modifier = Modifier.height(20.dp))

            PetEditTextField(
                label = "품종 (선택)",
                description = "예: 말티즈",
                placeholder = "반려견 품종을 입력해주세요",
                state = state.breedTextFieldState,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                fieldModifier = Modifier.focusScrollable()
            )

            Spacer(modifier = Modifier.height(20.dp))

            PetEditTextField(
                label = "몸무게 (kg) (선택)",
                description = "예: 3.5",
                placeholder = "반려견 몸무게를 입력해주세요",
                state = state.weightTextFieldState,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                fieldModifier = Modifier.focusScrollable()
            )

            Spacer(modifier = Modifier.height(20.dp))

            PetEditTextField(
                label = "생년월일 (선택)",
                description = "예: 2021-03-15",
                placeholder = "20210315",
                state = state.birthDateTextFieldState,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                inputTransformation = BirthDateInputTransformation,
                outputTransformation = BirthDateOutputTransformation,
                fieldModifier = Modifier.focusScrollable()
            )

            Spacer(modifier = Modifier.height(20.dp))

            PetEditNeuteredToggle(
                isNeutered = state.isNeutered,
                onCheckedChange = actions::onNeuteredToggle
            )

            Spacer(modifier = Modifier.height(20.dp))

            PetEditChipGroup(
                title = "반려견 크기",
                items = selectableEntries(PetSizeCategory.UNKNOWN).toPersistentList(),
                selectedItem = state.selectedSize,
                onItemSelected = actions::onSizeSelect
            )
            Spacer(modifier = Modifier.height(20.dp))

            PetEditChipGroup(
                title = "활동량",
                items = selectableEntries(PetActivityLevel.UNKNOWN).toPersistentList(),
                selectedItem = state.selectedActivity,
                onItemSelected = actions::onActivitySelect
            )
            Spacer(modifier = Modifier.height(20.dp))

            PetEditChipGroup(
                title = "사회성",
                items = selectableEntries(PetSociability.UNKNOWN).toPersistentList(),
                selectedItem = state.selectedSociability,
                onItemSelected = actions::onSociabilitySelect
            )
            Spacer(modifier = Modifier.height(20.dp))

            PetEditChipGroup(
                title = "건강 상태",
                items = selectableEntries(PetHealthStatus.UNKNOWN).toPersistentList(),
                selectedItem = state.selectedHealth,
                onItemSelected = actions::onHealthSelect
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
                onClick = actions::onSaveClick,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
            )
        }
    }
}

@Preview
@Composable
private fun MyPagePetEditScreenPreview() {
    MeongTheme {
        MyPagePetEditScreen(
            paddingValues = PaddingValues(),
            state = PetEditState(),
            onCloseClick = {},
            actions = remember {
                object : PetEditActions {
                    override fun onImageClick() {}
                    override fun onNeuteredToggle(isNeutered: Boolean) {}
                    override fun onSizeSelect(size: PetSizeCategory) {}
                    override fun onActivitySelect(activity: PetActivityLevel) {}
                    override fun onSociabilitySelect(sociability: PetSociability) {}
                    override fun onHealthSelect(health: PetHealthStatus) {}
                    override fun onSaveClick() {}
                }
            }
        )
    }
}
