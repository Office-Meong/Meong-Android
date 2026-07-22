package com.office.meong.core.crypto

/**
 * 이 기기에서 실제로 사용 가능한 암호화 보호 수준
 * HARDWARE_BACKED만 "안전하다"고 간주하며,
 * 나머지 두 상태는 정책상 동일하게 취급
 */
enum class CryptoAvailability {
    /** Android Keystore 연동 정상. 마스터 키가 OS 밖으로 노출되지 않음 */
    HARDWARE_BACKED,

    /** Keystore 연동 실패(재시도 포함) 후, masterKeyUri 없이 소프트웨어 키로 초기화됨 - 구글 권장
     *  암호화 자체는 동작하지만 키가 OS 레벨 보호 없이 앱 저장소에 존재 — 신뢰하지 않음 */
    SOFTWARE_FALLBACK,

    /** 소프트웨어 폴백 초기화조차 실패한 경우 (매우 드묾) */
    UNAVAILABLE,
}
