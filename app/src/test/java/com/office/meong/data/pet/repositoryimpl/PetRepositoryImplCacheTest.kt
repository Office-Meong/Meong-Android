package com.office.meong.data.pet.repositoryimpl

import com.office.meong.core.cache.InMemoryCache
import com.office.meong.core.network.model.BaseResponse
import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability
import com.office.meong.data.pet.model.PetInputModel
import com.office.meong.data.pet.model.PetModel
import com.office.meong.data.pet.remote.api.PetService
import com.office.meong.data.pet.remote.datasource.PetDataSource
import com.office.meong.data.pet.remote.dto.request.DogRequest
import com.office.meong.data.pet.remote.dto.response.DogResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

private class FakeDogService : PetService {
    var getDogsCallCount = 0
        private set

    private val dogResponse = DogResponse(
        id = 1,
        name = "몽몽이",
        breed = "푸들",
        weightKg = 5.0,
        birthDate = "2020-01-01",
        isNeutered = true,
        imageUrl = "",
        sizeCategory = PetSizeCategory.SMALL.name,
        activityLevel = PetActivityLevel.MEDIUM.name,
        sociability = PetSociability.NORMAL.name,
        healthStatus = PetHealthStatus.HEALTHY.name,
    )

    override suspend fun getDogs(): BaseResponse<List<DogResponse>> {
        getDogsCallCount++
        return BaseResponse(success = true, data = listOf(dogResponse))
    }

    override suspend fun postDog(dogRequest: DogRequest): BaseResponse<DogResponse> =
        BaseResponse(success = true, data = dogResponse.copy(id = 2, name = dogRequest.name))

    override suspend fun putDog(dogId: Long, dogRequest: DogRequest) = throw NotImplementedError()
    override suspend fun deleteDog(dogId: Long) = throw NotImplementedError()
}

class PetRepositoryImplCacheTest {

    private fun buildRepository(fakeService: FakeDogService) = PetRepositoryImpl(
        petDataSource = PetDataSource(fakeService),
        dogsCache = InMemoryCache(),
    )

    @Test
    fun `getDogs를 여러 화면에서 호출해도 네트워크는 한 번만 나간다`() = runBlocking {
        val fakeService = FakeDogService()
        val repository = buildRepository(fakeService)

        val firstResult = repository.getDogs()
        val secondResult = repository.getDogs()

        assertEquals(1, fakeService.getDogsCallCount)
        assertEquals(firstResult.getOrNull(), secondResult.getOrNull())
    }

    @Test
    fun `반려견을 등록하면 캐시가 무효화되어 다음 조회는 다시 네트워크를 탄다`() = runBlocking {
        val fakeService = FakeDogService()
        val repository = buildRepository(fakeService)

        repository.getDogs()
        repository.createDog(
            PetInputModel(
                name = "새 강아지",
                breed = "말티즈",
                weightKg = 3.0,
                birthDate = "2022-01-01",
                isNeutered = false,
                imageUrl = "",
                sizeCategory = PetSizeCategory.SMALL,
                activityLevel = PetActivityLevel.HIGH,
                sociability = PetSociability.FRIENDLY,
                healthStatus = PetHealthStatus.HEALTHY,
            )
        )
        repository.getDogs()

        assertEquals(2, fakeService.getDogsCallCount)
    }

    @Test
    fun `동시에 두 화면이 처음 조회해도 네트워크는 한 번만 나간다`() = runBlocking {
        val fakeService = FakeDogService()
        val repository = buildRepository(fakeService)

        val firstDeferred = async { repository.getDogs() }
        val secondDeferred = async { repository.getDogs() }
        val results = listOf(firstDeferred.await(), secondDeferred.await())

        assertEquals(1, fakeService.getDogsCallCount)
        assertEquals(results[0].getOrNull(), results[1].getOrNull())
    }
}
