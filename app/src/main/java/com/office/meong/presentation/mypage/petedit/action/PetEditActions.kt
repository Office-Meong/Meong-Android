package com.office.meong.presentation.mypage.petedit.action

import androidx.compose.runtime.Stable
import com.office.meong.presentation.mypage.petedit.model.PetActivity
import com.office.meong.presentation.mypage.petedit.model.PetHealth
import com.office.meong.presentation.mypage.petedit.model.PetSize
import com.office.meong.presentation.mypage.petedit.model.PetSociability

@Stable
interface PetEditActions {
    fun onImageClick()
    fun onNeuteredToggle(isNeutered: Boolean)
    fun onSizeSelect(size: PetSize)
    fun onActivitySelect(activity: PetActivity)
    fun onSociabilitySelect(sociability: PetSociability)
    fun onHealthSelect(health: PetHealth)
    fun onSaveClick()
}
