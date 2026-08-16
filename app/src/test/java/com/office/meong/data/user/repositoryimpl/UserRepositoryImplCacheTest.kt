package com.office.meong.data.user.repositoryimpl

import com.office.meong.core.cache.InMemoryCache
import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.user.remote.api.UserService
import com.office.meong.data.user.remote.datasource.UserDataSource
import com.office.meong.data.user.remote.dto.request.PatchUserRequest
import com.office.meong.data.user.remote.dto.response.UserResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

private class FakeUserService : UserService {
    var getUserInfoCallCount = 0
        private set

    private var currentUser = UserResponse(
        id = 1,
        nickname = "몽집사",
        profileImageUrl = "",
        email = "meong@example.com",
        createdAt = "2026-01-01T00:00:00.000Z",
    )

    override suspend fun getUserInfo(): BaseResponse<UserResponse> {
        getUserInfoCallCount++
        return BaseResponse(success = true, data = currentUser)
    }

    override suspend fun deleteUser(): BaseResponse<Unit> = BaseResponse(success = true, data = Unit)

    override suspend fun patchUser(patchUserRequest: PatchUserRequest): BaseResponse<UserResponse> {
        currentUser = currentUser.copy(
            nickname = patchUserRequest.nickname,
            profileImageUrl = patchUserRequest.profileImageUrl,
        )
        return BaseResponse(success = true, data = currentUser)
    }
}

class UserRepositoryImplCacheTest {

    private fun buildRepository(fakeService: FakeUserService) = UserRepositoryImpl(
        userDataSource = UserDataSource(fakeService),
        userInfoCache = InMemoryCache(),
    )

    @Test
    fun `getUserInfo를 여러 화면에서 호출해도 네트워크는 한 번만 나간다`() = runBlocking {
        val fakeService = FakeUserService()
        val repository = buildRepository(fakeService)

        val firstResult = repository.getUserInfo()
        val secondResult = repository.getUserInfo()

        assertEquals(1, fakeService.getUserInfoCallCount)
        assertEquals(firstResult.getOrNull(), secondResult.getOrNull())
    }

    @Test
    fun `patchUser 응답으로 캐시를 바로 채워서 이후 조회는 네트워크를 안 탄다`() = runBlocking {
        val fakeService = FakeUserService()
        val repository = buildRepository(fakeService)

        repository.getUserInfo()
        val patched = repository.patchUser(nickname = "새 닉네임", profileImageUrl = "new.png")
        val afterPatch = repository.getUserInfo()

        assertEquals(1, fakeService.getUserInfoCallCount)
        assertEquals("새 닉네임", patched.getOrNull()?.nickname)
        assertEquals(patched.getOrNull(), afterPatch.getOrNull())
    }

    @Test
    fun `회원 탈퇴 시 캐시가 무효화되어 다음 조회는 다시 네트워크를 탄다`() = runBlocking {
        val fakeService = FakeUserService()
        val repository = buildRepository(fakeService)

        repository.getUserInfo()
        repository.deleteUser()
        repository.getUserInfo()

        assertEquals(2, fakeService.getUserInfoCallCount)
    }

    @Test
    fun `동시에 두 화면이 처음 조회해도 네트워크는 한 번만 나간다`() = runBlocking {
        val fakeService = FakeUserService()
        val repository = buildRepository(fakeService)

        val firstDeferred = async { repository.getUserInfo() }
        val secondDeferred = async { repository.getUserInfo() }
        val results = listOf(firstDeferred.await(), secondDeferred.await())

        assertEquals(1, fakeService.getUserInfoCallCount)
        assertEquals(results[0].getOrNull(), results[1].getOrNull())
    }
}
