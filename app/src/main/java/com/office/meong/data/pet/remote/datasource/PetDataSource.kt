package com.office.meong.data.pet.remote.datasource

import com.office.meong.core.network.model.ApiException
import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.pet.remote.api.PetService
import com.office.meong.data.pet.remote.dto.request.DogRequest
import com.office.meong.data.pet.remote.dto.response.DogResponse
import javax.inject.Inject

class PetDataSource @Inject constructor(
    private val petService: PetService
) {
    suspend fun getDogs(): List<DogResponse> = petService.getDogs().getOrThrow()

    suspend fun postDog(dogRequest: DogRequest): DogResponse =
        petService.postDog(dogRequest).getOrThrow()

    suspend fun putDog(dogId: Long, dogRequest: DogRequest): DogResponse =
        petService.putDog(dogId, dogRequest).getOrThrow()

    suspend fun deleteDog(dogId: Long) {
        val response = petService.deleteDog(dogId)
        if (!response.success) throw ApiException(response.message)
    }
}
