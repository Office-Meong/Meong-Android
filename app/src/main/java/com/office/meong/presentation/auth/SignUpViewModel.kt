package com.office.meong.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability
import com.office.meong.data.pet.repository.PetRepository
import com.office.meong.data.presigned.repository.PresignedRepository
import com.office.meong.presentation.auth.model.SignUpSideEffect
import com.office.meong.presentation.auth.model.SignUpUiState
import com.office.meong.presentation.auth.model.toPetInputModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val presignedRepository: PresignedRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpUiState())
    val state: StateFlow<SignUpUiState> = _state.asStateFlow()

    private val _sideEffect = Channel<SignUpSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onImageSelected(uriString: String) {
        viewModelScope.launch {
            _state.update { it.copy(isImageUploading = true) }

            presignedRepository.uploadImage(
                uriString = uriString,
                fileName = "${UUID.randomUUID()}.webp"
            ).onSuccess { url ->
                _state.update { it.copy(imageUrl = url, isImageUploading = false) }
            }.onFailure {
                _state.update { it.copy(isImageUploading = false) }
                _sideEffect.send(SignUpSideEffect.ShowSnackBar("이미지 업로드에 실패했어요"))
            }
        }
    }

    fun onNeuteredToggle(isNeutered: Boolean) {
        _state.update { it.copy(isNeutered = isNeutered) }
    }

    fun onSizeSelect(size: PetSizeCategory) {
        _state.update { it.copy(selectedSize = size) }
    }

    fun onActivitySelect(activity: PetActivityLevel) {
        _state.update { it.copy(selectedActivity = activity) }
    }

    fun onSociabilitySelect(sociability: PetSociability) {
        _state.update { it.copy(selectedSociability = sociability) }
    }

    fun onHealthSelect(health: PetHealthStatus) {
        _state.update { it.copy(selectedHealth = health) }
    }

    fun onSaveClick() {
        val currentState = _state.value

        if (currentState.nameTextFieldState.text.isBlank()) {
            _state.update { it.copy(hasAttemptedSave = true) }
            return
        }
        if (!currentState.isSaveEnabled) return

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            petRepository.createDog(currentState.toPetInputModel())
                .onSuccess {
                    _state.update { it.copy(isSaving = false) }
                    _sideEffect.send(SignUpSideEffect.NavigateToHome)
                }
                .onFailure {
                    _state.update { it.copy(isSaving = false) }
                    _sideEffect.send(SignUpSideEffect.ShowSnackBar("반려견 정보 저장에 실패했어요"))
                }
        }
    }
}
