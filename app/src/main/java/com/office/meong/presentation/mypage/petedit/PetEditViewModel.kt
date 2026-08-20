package com.office.meong.presentation.mypage.petedit

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetInfo
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability
import com.office.meong.data.pet.model.toInfo
import com.office.meong.data.pet.repository.PetRepository
import com.office.meong.data.presigned.repository.PresignedRepository
import com.office.meong.presentation.mypage.petedit.model.toPetInputModel
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
class PetEditViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val presignedRepository: PresignedRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(PetEditState())
    val state: StateFlow<PetEditState> = _state.asStateFlow()

    private val _sideEffect = Channel<PetEditSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchDog()
    }

    fun retryFetchDog() {
        fetchDog()
    }

    private fun fetchDog() {
        viewModelScope.launch {
            _state.update { it.copy(pet = UiState.Loading) }

            petRepository.getDogs()
                .onSuccess { dogs ->
                    val pet = dogs.firstOrNull()?.toInfo()
                    pet?.let(::applyPet)

                    _state.update { currentState ->
                        currentState.copy(
                            pet = pet?.let { UiState.Success(it) } ?: UiState.Empty
                        )
                    }
                }
                .onFailure {
                    _state.update { currentState ->
                        currentState.copy(pet = UiState.Failure(LoadErrorHandleAction.Retry))
                    }
                }
        }
    }

    private fun applyPet(pet: PetInfo) {
        val current = _state.value
        current.nameTextFieldState.setTextAndPlaceCursorAtEnd(pet.name)
        current.breedTextFieldState.setTextAndPlaceCursorAtEnd(pet.breed)
        current.weightTextFieldState.setTextAndPlaceCursorAtEnd(pet.weightKg.toString())
        current.birthDateTextFieldState.setTextAndPlaceCursorAtEnd(pet.birthDate.filter { it.isDigit() })

        _state.update {
            it.copy(
                imageUrl = pet.imageUrl,
                isNeutered = pet.isNeutered,
                selectedSize = pet.sizeCategory,
                selectedActivity = pet.activityLevel,
                selectedSociability = pet.sociability,
                selectedHealth = pet.healthStatus,
            )
        }
    }

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
                _sideEffect.send(PetEditSideEffect.ShowSnackBar("이미지 업로드에 실패했어요"))
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
        val dogId = (currentState.pet as? UiState.Success)?.data?.id ?: return

        if (currentState.nameTextFieldState.text.isBlank()) {
            _state.update { it.copy(hasAttemptedSave = true) }
            return
        }
        if (!currentState.isSaveEnabled) return

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            petRepository.updateDog(dogId, currentState.toPetInputModel())
                .onSuccess {
                    _state.update { it.copy(isSaving = false) }
                    _sideEffect.send(PetEditSideEffect.NavigateUp)
                }
                .onFailure {
                    _state.update { it.copy(isSaving = false) }
                    _sideEffect.send(PetEditSideEffect.ShowSnackBar("반려견 정보 저장에 실패했어요"))
                }
        }
    }
}
