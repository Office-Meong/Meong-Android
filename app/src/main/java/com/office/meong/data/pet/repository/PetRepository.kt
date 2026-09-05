package com.office.meong.data.pet.repository

import com.office.meong.data.pet.model.PetInputModel
import com.office.meong.data.pet.model.PetModel
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun getDogs(): Result<List<PetModel>>
    fun observeDogs(): Flow<List<PetModel>?>
    suspend fun createDog(request: PetInputModel): Result<PetModel>
    suspend fun updateDog(dogId: Long, request: PetInputModel): Result<PetModel>
    suspend fun deleteDog(dogId: Long): Result<Unit>
}
