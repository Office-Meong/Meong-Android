package com.office.meong.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.common.util.selectableEntries
import com.office.meong.core.designsystem.component.topbar.MeongTopbar
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability
import com.office.meong.presentation.auth.component.PetProfileChipGroup
import com.office.meong.presentation.auth.component.PetProfileImagePicker
import com.office.meong.presentation.auth.component.SignUpBottomButton
import com.office.meong.presentation.auth.component.SignUpTextField
import com.office.meong.presentation.auth.model.SignUpUiState

@Composable
fun SignUpRoute(
    paddingValues: PaddingValues
) {
    val nameTextFieldState = rememberTextFieldState()

    SignUpScreen(
        paddingValues = paddingValues,
        uiState = SignUpUiState(),
        nameTextFieldState = nameTextFieldState,
        onBackClick = {},
        onImageClick = {},
        onSizeSelect = {},
        onActivitySelect = {},
        onSociabilitySelect = {},
        onHealthSelect = {},
        onSaveClick = {}
    )
}

@Composable
private fun SignUpScreen(
    paddingValues: PaddingValues,
    uiState: SignUpUiState,
    nameTextFieldState: TextFieldState,
    onBackClick: () -> Unit,
    onImageClick: () -> Unit,
    onSizeSelect: (PetSizeCategory) -> Unit,
    onActivitySelect: (PetActivityLevel) -> Unit,
    onSociabilitySelect: (PetSociability) -> Unit,
    onHealthSelect: (PetHealthStatus) -> Unit,
    onSaveClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeongTheme.colors.white)
            .padding(paddingValues)
    ) {
        MeongTopbar(onBackClick = onBackClick)

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

            PetProfileImagePicker(
                imageUrl = uiState.imageUrl,
                onClick = onImageClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            SignUpTextField(
                state = nameTextFieldState
            )

            Spacer(modifier = Modifier.height(24.dp))

            PetProfileChipGroup(
                title = "반려견 크기",
                items = selectableEntries(PetSizeCategory.UNKNOWN).toTypedArray(),
                selectedItem = uiState.selectedSize,
                itemLabel = { it.label },
                onItemSelected = onSizeSelect
            )
            Spacer(modifier = Modifier.height(24.dp))

            PetProfileChipGroup(
                title = "활동량",
                items = selectableEntries(PetActivityLevel.UNKNOWN).toTypedArray(),
                selectedItem = uiState.selectedActivity,
                itemLabel = { it.label },
                onItemSelected = onActivitySelect
            )
            Spacer(modifier = Modifier.height(24.dp))

            PetProfileChipGroup(
                title = "사회성",
                items = selectableEntries(PetSociability.UNKNOWN).toTypedArray(),
                selectedItem = uiState.selectedSociability,
                itemLabel = { it.label },
                onItemSelected = onSociabilitySelect
            )
            Spacer(modifier = Modifier.height(24.dp))

            PetProfileChipGroup(
                title = "건강 상태",
                items = selectableEntries(PetHealthStatus.UNKNOWN).toTypedArray(),
                selectedItem = uiState.selectedHealth,
                itemLabel = { it.label },
                onItemSelected = onHealthSelect
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

        SignUpBottomButton(
            isEnabled = uiState.isSaveEnabled,
            onClick = onSaveClick
        )
    }
}

@Preview
@Composable
private fun SignUpScreenPreview() {
    MeongTheme {
        SignUpRoute(
            paddingValues = PaddingValues()
        )
    }
}