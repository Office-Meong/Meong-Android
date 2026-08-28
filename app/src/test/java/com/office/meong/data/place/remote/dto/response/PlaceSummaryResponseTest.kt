package com.office.meong.data.place.remote.dto.response

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 서버가 일부 장소 레코드의 문자열 필드에 null 을 내려보내도(예: acmpyType)
 * 페이지 전체 파싱이 깨지지 않아야 한다. [NetworkModule] 의 Json 설정과 동일하게 맞춘다.
 */
class PlaceSummaryResponseTest {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        encodeDefaults = true
    }

    @Test
    fun `문자열 필드가 null 이면 기본값으로 대체되어 파싱된다`() {
        val raw = """
            {"id":45,"name":"고씨네 동해막국수","region":"GANGNEUNG","placeType":"FOOD",
             "address":"강원 강릉시","thumbnailUrl":null,"grade":"C","totalScore":32,
             "acmpyType":null,"congestionScore":12,"congestionLevel":"RELAXED","favorite":false}
        """.trimIndent()

        val result = json.decodeFromString<PlaceSummaryResponse>(raw)

        assertEquals(45L, result.id)
        assertEquals("", result.acmpyType)
    }

    @Test
    fun `null 필드를 가진 항목이 섞여 있어도 페이지 응답 전체가 파싱된다`() {
        val raw = """
            {"content":[
              {"id":1,"name":"A","region":"GANGNEUNG","placeType":"FOOD","address":"a","thumbnailUrl":null,"grade":"C","totalScore":50,"acmpyType":"INDOOR","congestionScore":12,"congestionLevel":"RELAXED","favorite":false},
              {"id":2,"name":"B","region":"GANGNEUNG","placeType":"FOOD","address":"b","thumbnailUrl":null,"grade":"E","totalScore":32,"acmpyType":null,"congestionScore":12,"congestionLevel":"RELAXED","favorite":false}
            ],"page":1,"size":20,"totalElements":44,"totalPages":3,"hasNext":true}
        """.trimIndent()

        val result = json.decodeFromString<PlacePageResponse>(raw)

        assertEquals(2, result.content.size)
        assertEquals("", result.content[1].acmpyType)
        assertEquals(1, result.page)
        assertEquals(true, result.hasNext)
    }
}
