package com.office.meong.core.crypto

sealed class CryptoException(message: String, cause: Throwable? = null) : Exception(message, cause)

/** 이 기기에서 Android Keystore를 사용할 수 없는 경우.
 *  호출부는 이 예외를 받으면 "암호화 저장을 포기하고 세션 메모리에만 유지" 또는
 *  "재로그인 요구" 등의 정책을 명시적으로 적용해야 함. */
class CryptoNotHardwareBackedException(val availability: CryptoAvailability) :
    CryptoException("이 기기는 하드웨어 보호 암호화를 지원하지 않습니다. (state=$availability)")

class EncryptionFailedException(cause: Throwable? = null) :
    CryptoException("암호화 실패", cause)

/** GCM 인증 태그 불일치 — 데이터 위변조, 키 불일치, 또는 AAD 불일치. */
class DecryptionAuthFailedException(cause: Throwable? = null) :
    CryptoException("복호화 인증 실패: 데이터가 손상되었거나 위변조되었을 수 있습니다.", cause)

/** base64 디코딩 실패 등 입력값 자체가 잘못된 경우 */
class DecryptionMalformedInputException(cause: Throwable? = null) :
    CryptoException("복호화 실패: 입력값 형식이 올바르지 않습니다.", cause)
