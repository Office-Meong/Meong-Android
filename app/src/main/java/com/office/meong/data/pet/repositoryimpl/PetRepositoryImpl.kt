package com.office.meong.data.pet.repositoryimpl

import com.office.meong.core.cache.InMemoryCache
import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.data.pet.model.PetInputModel
import com.office.meong.data.pet.model.PetModel
import com.office.meong.data.pet.model.toDto
import com.office.meong.data.pet.model.toModel
import com.office.meong.data.pet.remote.datasource.PetDataSource
import com.office.meong.data.pet.repository.PetRepository
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petDataSource: PetDataSource,
    private val dogsCache: InMemoryCache<List<PetModel>>,
) : PetRepository {
    override suspend fun getDogs(): Result<List<PetModel>> = suspendRunCatching {
        dogsCache.getOrFetch { petDataSource.getDogs().map { it.toModel() } }
    }

    override suspend fun createDog(request: PetInputModel): Result<PetModel> = suspendRunCatching {
        petDataSource.postDog(request.toDto()).toModel().also { dogsCache.invalidate() }
    }

    override suspend fun updateDog(dogId: Long, request: PetInputModel): Result<PetModel> = suspendRunCatching {
        petDataSource.putDog(dogId, request.toDto()).toModel().also { dogsCache.invalidate() }
    }

    override suspend fun deleteDog(dogId: Long): Result<Unit> = suspendRunCatching {
        petDataSource.deleteDog(dogId)
        dogsCache.invalidate()
    }
}
