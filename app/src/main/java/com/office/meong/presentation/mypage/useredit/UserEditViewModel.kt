package com.office.meong.presentation.mypage.useredit

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.data.presigned.repository.PresignedRepository
import com.office.meong.data.user.model.UserInfoModel
import com.office.meong.data.user.repository.UserRepository
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
class UserEditViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val presignedRepository: PresignedRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(UserEditState())
    val state: StateFlow<UserEditState> = _state.asStateFlow()

    private val _sideEffect = Channel<UserEditSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchUserInfo()
    }

    fun retryFetchUserInfo() {
        fetchUserInfo()
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            _state.update { it.copy(user = UiState.Loading) }

            userRepository.getUserInfo()
                .onSuccess { user ->
                    applyUser(user)
                    _state.update { it.copy(user = UiState.Success(user)) }
                }
                .onFailure {
                    _state.update { it.copy(user = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
        }
    }

    private fun applyUser(user: UserInfoModel) {
        _state.value.nicknameTextFieldState.setTextAndPlaceCursorAtEnd(user.nickname)
        _state.update { it.copy(imageUrl = user.profileImageUrl?.ifBlank { null }) }
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
                _sideEffect.send(UserEditSideEffect.ShowSnackBar("이미지 업로드에 실패했어요"))
            }
        }
    }

    fun onSaveClick() {
        val currentState = _state.value

        if (currentState.nicknameTextFieldState.text.isBlank()) {
            _state.update { it.copy(hasAttemptedSave = true) }
            return
        }
        if (!currentState.isSaveEnabled) return

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            userRepository.patchUser(
                nickname = currentState.nicknameTextFieldState.text.toString(),
                profileImageUrl = currentState.imageUrl.orEmpty()
            ).onSuccess {
                _state.update { it.copy(isSaving = false) }
                _sideEffect.send(UserEditSideEffect.NavigateUp)
            }.onFailure {
                _state.update { it.copy(isSaving = false) }
                _sideEffect.send(UserEditSideEffect.ShowSnackBar("프로필 저장에 실패했어요"))
            }
        }
    }
}
