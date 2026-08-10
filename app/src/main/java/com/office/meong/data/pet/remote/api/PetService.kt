package com.office.meong.data.pet.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.pet.remote.dto.request.DogRequest
import com.office.meong.data.pet.remote.dto.response.DogResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PetService {
    @GET("dogs")
    suspend fun getDogs(): BaseResponse<List<DogResponse>>

    @POST("dogs")
    suspend fun postDog(
        @Body dogRequest: DogRequest
    ): BaseResponse<DogResponse>

    @PUT("dogs/{dogId}")
    suspend fun putDog(
        @Path("dogId") dogId: Long,
        @Body dogRequest: DogRequest
    ): BaseResponse<DogResponse>

    @DELETE("dogs/{dogId}")
    suspend fun deleteDog(
        @Path("dogId") dogId: Long
    ): BaseResponse<Unit>
}
