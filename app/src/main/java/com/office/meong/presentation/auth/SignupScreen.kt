package com.office.meong.presentation.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.extension.focusScrollable
import com.office.meong.core.common.util.selectableEntries
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.component.textfield.BirthDateInputTransformation
import com.office.meong.core.designsystem.component.textfield.BirthDateOutputTransformation
import com.office.meong.core.designsystem.component.topbar.MeongTopbar
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.presentation.auth.model.SignUpSideEffect
import com.office.meong.presentation.auth.model.SignUpUiState
import com.office.meong.presentation.mypage.petedit.action.PetEditActions
import com.office.meong.presentation.mypage.petedit.component.PetEditChipGroup
import com.office.meong.presentation.mypage.petedit.component.PetEditImagePicker
import com.office.meong.presentation.mypage.petedit.component.PetEditNeuteredToggle
import com.office.meong.presentation.mypage.petedit.component.PetEditTextField
import kotlinx.collections.immutable.toPersistentList

@Composable
fun SignUpRoute(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit = {},
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is SignUpSideEffect.NavigateToHome -> navigateToHome()
            is SignUpSideEffect.ShowSnackBar -> {
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

    SignUpScreen(
        paddingValues = paddingValues,
        state = state,
        actions = actions
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SignUpScreen(
    paddingValues: PaddingValues,
    state: SignUpUiState,
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
        MeongTopbar(isBackVisible = false)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "함께 워케이션을 떠날\n반려견 정보를 알려주세요.",
                style = MeongTheme.typography.title.title20Sb,
                color = MeongTheme.colors.gray900
            )

            Spacer(modifier = Modifier.height(32.dp))

            PetEditImagePicker(
                imageUrl = state.imageUrl,
                onClick = actions::onImageClick,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                isLoading = state.isImageUploading
            )

            Spacer(modifier = Modifier.height(32.dp))

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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                fieldModifier = Modifier.focusScrollable()
            )

            Spacer(modifier = Modifier.height(20.dp))

            PetEditTextField(
                label = "생년월일 (선택)",
                description = "예: 2021-03-15",
                placeholder = "20210315",
                state = state.birthDateTextFieldState,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
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
private fun SignUpScreenPreview() {
    MeongTheme {
        SignUpScreen(
            paddingValues = PaddingValues(),
            state = SignUpUiState(),
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
