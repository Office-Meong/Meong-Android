package com.office.meong.presentation.mypage.petedit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.common.util.selectableEntries
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability
import com.office.meong.presentation.mypage.petedit.action.PetEditActions
import com.office.meong.presentation.mypage.petedit.component.PetEditBottomButton
import com.office.meong.presentation.mypage.petedit.component.PetEditChipGroup
import com.office.meong.presentation.mypage.petedit.component.PetEditImagePicker
import com.office.meong.presentation.mypage.petedit.component.PetEditNeuteredToggle
import com.office.meong.presentation.mypage.petedit.component.PetEditTextField
import com.office.meong.presentation.mypage.petedit.model.PetEditUiState
import kotlinx.collections.immutable.toPersistentList

@Composable
fun MyPagePetEditRoute(
    paddingValues: PaddingValues,
    onCloseClick: () -> Unit = {}
) {
    val nameTextFieldState = rememberTextFieldState(initialText = "몽몽이")
    val breedTextFieldState = rememberTextFieldState()
    val weightTextFieldState = rememberTextFieldState()
    val birthDateTextFieldState = rememberTextFieldState()
    var isNeuteredState by remember { mutableStateOf(false) }

    val actions = remember {
        object : PetEditActions {
            override fun onImageClick() {
                // TODO: 이미지 선택 처리
            }
            override fun onNeuteredToggle(isNeutered: Boolean) {
                isNeuteredState = isNeutered
            }
            override fun onSizeSelect(size: PetSizeCategory) {
                // TODO: 크기 선택 처리
            }
            override fun onActivitySelect(activity: PetActivityLevel) {
                // TODO: 활동량 선택 처리
            }
            override fun onSociabilitySelect(sociability: PetSociability) {
                // TODO: 사회성 선택 처리
            }
            override fun onHealthSelect(health: PetHealthStatus) {
                // TODO: 건강 상태 선택 처리
            }
            override fun onSaveClick() {
                // TODO: 저장 처리
            }
        }
    }

    MyPagePetEditScreen(
        paddingValues = paddingValues,
        uiState = PetEditUiState(
            petName = nameTextFieldState.text.toString(),
            breed = breedTextFieldState.text.toString(),
            weightKg = weightTextFieldState.text.toString(),
            birthDate = birthDateTextFieldState.text.toString(),
            isNeutered = isNeuteredState,
            selectedSize = PetSizeCategory.SMALL,
            selectedActivity = PetActivityLevel.MEDIUM,
            selectedSociability = PetSociability.NORMAL,
            selectedHealth = PetHealthStatus.HEALTHY
        ),
        nameTextFieldState = nameTextFieldState,
        breedTextFieldState = breedTextFieldState,
        weightTextFieldState = weightTextFieldState,
        birthDateTextFieldState = birthDateTextFieldState,
        onCloseClick = onCloseClick,
        actions = actions
    )
}

@Composable
private fun MyPagePetEditScreen(
    paddingValues: PaddingValues,
    uiState: PetEditUiState,
    nameTextFieldState: TextFieldState,
    breedTextFieldState: TextFieldState,
    weightTextFieldState: TextFieldState,
    birthDateTextFieldState: TextFieldState,
    onCloseClick: () -> Unit,
    actions: PetEditActions
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeongTheme.colors.white)
            .padding(paddingValues)
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
                imageUrl = uiState.imageUrl,
                onClick = actions::onImageClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            PetEditTextField(
                label = "반려견 이름",
                description = "15자 이내로 입력해주세요",
                placeholder = "반려견 이름을 입력해주세요",
                state = nameTextFieldState
            )

            Spacer(modifier = Modifier.height(20.dp))

            PetEditTextField(
                label = "품종",
                description = "예: 말티즈",
                placeholder = "반려견 품종을 입력해주세요",
                state = breedTextFieldState
            )

            Spacer(modifier = Modifier.height(20.dp))

            PetEditTextField(
                label = "몸무게 (kg)",
                description = "예: 3.5",
                placeholder = "반려견 몸무게를 입력해주세요",
                state = weightTextFieldState,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(modifier = Modifier.height(20.dp))

            PetEditTextField(
                label = "생년월일",
                description = "예: 2021-03-15",
                placeholder = "YYYY-MM-DD",
                state = birthDateTextFieldState,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(20.dp))

            PetEditNeuteredToggle(
                isNeutered = uiState.isNeutered,
                onCheckedChange = actions::onNeuteredToggle
            )

            Spacer(modifier = Modifier.height(20.dp))

            PetEditChipGroup(
                title = "반려견 크기",
                items = selectableEntries(PetSizeCategory.UNKNOWN).toPersistentList(),
                selectedItem = uiState.selectedSize,
                onItemSelected = actions::onSizeSelect
            )
            Spacer(modifier = Modifier.height(20.dp))

            PetEditChipGroup(
                title = "활동량",
                items = selectableEntries(PetActivityLevel.UNKNOWN).toPersistentList(),
                selectedItem = uiState.selectedActivity,
                onItemSelected = actions::onActivitySelect
            )
            Spacer(modifier = Modifier.height(20.dp))

            PetEditChipGroup(
                title = "사회성",
                items = selectableEntries(PetSociability.UNKNOWN).toPersistentList(),
                selectedItem = uiState.selectedSociability,
                onItemSelected = actions::onSociabilitySelect
            )
            Spacer(modifier = Modifier.height(20.dp))

            PetEditChipGroup(
                title = "건강 상태",
                items = selectableEntries(PetHealthStatus.UNKNOWN).toPersistentList(),
                selectedItem = uiState.selectedHealth,
                onItemSelected = actions::onHealthSelect
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

        PetEditBottomButton(
            isEnabled = uiState.isSaveEnabled,
            onClick = actions::onSaveClick
        )
    }
}

@Preview
@Composable
private fun MyPagePetEditScreenPreview() {
    MeongTheme {
        MyPagePetEditRoute(
            paddingValues = PaddingValues()
        )
    }
}
