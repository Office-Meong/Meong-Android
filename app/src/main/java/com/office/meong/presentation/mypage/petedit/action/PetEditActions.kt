package com.office.meong.presentation.mypage.petedit.action

import androidx.compose.runtime.Stable
import com.office.meong.data.pet.model.PetActivityLevel
import com.office.meong.data.pet.model.PetHealthStatus
import com.office.meong.data.pet.model.PetSizeCategory
import com.office.meong.data.pet.model.PetSociability

@Stable
interface PetEditActions {
    fun onImageClick()
    fun onNeuteredToggle(isNeutered: Boolean)
    fun onSizeSelect(size: PetSizeCategory)
    fun onActivitySelect(activity: PetActivityLevel)
    fun onSociabilitySelect(sociability: PetSociability)
    fun onHealthSelect(health: PetHealthStatus)
    fun onSaveClick()
}
