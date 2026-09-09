<div align="center">
  <img src="https://github.com/user-attachments/assets/56bf9a3e-58f8-4a22-bef5-0945e4028d1b" width="120" alt="오피스멍 앱 아이콘" />
  <h1>오피스멍 (Office Meong)</h1>
  <p>우리 집 강아지와 떠나는 워케이션, 코스 추천부터 일정 관리까지 🐾</p>
</div>

## 📖 소개
**오피스멍**은 반려견과 함께 강원 지역(강릉·춘천·원주)에서 워케이션을 계획하는 사용자를 위한 맞춤 코스 추천 Android 앱입니다.

여행 지역, 워케이션 기간, 집중 업무시간, 숙소 유형, 워케이션 스타일을 입력하면 반려견 정보와 장소 데이터를 바탕으로 최적의 워케이션 코스를 추천해 드립니다.

<br>

## ✨ 주요 기능
* **반려견 정보 기반 코스 추천:** 반려견의 크기, 활동량, 사회성, 건강 상태를 반영해 무리 없는 코스를 제안합니다.
* **맞춤형 워케이션 조건 입력:** 지역, 기간, 집중 업무시간, 숙소 유형, 워케이션 스타일을 설정하여 조건에 맞는 코스를 생성합니다.
* **방문 순서 기반 일정 추천:** 촘촘한 시간표 대신 이동하기 좋은 방문 순서 중심으로 코스를 구성하며, 카카오맵 경로 연동을 제공합니다.
* **펫-워크 지수 제공:** 반려견 동반 조건, 워케이션 적합도, 예측 혼잡도, 접근성, 응급 접근성을 종합적으로 반영한 장소별 등급을 안내합니다.
* **장소 탐색 및 관심 장소 저장:** 숙소, 업무장소, 음식점, 관광·산책 장소를 탐색하고 관심 장소로 찜할 수 있습니다.
* **코스 저장 및 관리:** 추천받은 코스를 저장하고, 자유롭게 일정을 편집하거나 장소를 변경 및 삭제할 수 있습니다.

<br>

## 📱 화면 구성

<div align="center">
  <img src="https://github.com/user-attachments/assets/6f830602-1593-4c76-9a8b-95c6a990a6b5" width="11%" />
  <img src="https://github.com/user-attachments/assets/d313ebde-5db2-49b7-b9f8-b856a6d79f5b" width="11%" />
  <img src="https://github.com/user-attachments/assets/2094906b-1861-46b6-bd9b-a5a11c721de2" width="11%" />
  <img src="https://github.com/user-attachments/assets/077e90b0-51ca-47b5-88d6-ed7d0d760a62" width="11%" />
  <img src="https://github.com/user-attachments/assets/8ffd0905-05d4-4fab-aeb9-926fbe5cf1d1" width="11%" />
  <img src="https://github.com/user-attachments/assets/f1e2717d-8360-4064-a052-398f1b88d578" width="11%" />
  <img src="https://github.com/user-attachments/assets/03732d86-4d33-4756-9f54-6e9b8c4cfed2" width="11%" />
  <img src="https://github.com/user-attachments/assets/d6969fff-8e6d-421e-bbb5-ead09c0a573f" width="11%" />
</div>

<br>

## 🛠기술 스택

  ### 🏗Architecture & Core
  * **Language:** Kotlin
  * **UI:** Jetpack Compose (Material 3)
  * **Architecture:** MVVM + `UiState<T>` sealed class 기반 상태 관리
  * **Navigation:** Navigation Compose (`MeongNavHost`, 타입 세이프 `Route` 정의)
  * **Asynchronous:** Kotlin Coroutines, Flow (`callbackFlow`, `StateFlow` 확장 유틸 등)
  * **DI:** Hilt
  * **Module:** Single Module (`:app`), 기능 단위 패키지 분리 (`data / domain / presentation / core`)

  ### 🗄Data & Network
  * **HTTP Client:** Retrofit 3 + OkHttp 5 (BOM 관리)
  * **Serialization:** Kotlinx Serialization (Retrofit converter 연동)
  * **API Response:** `BaseResponse<T>` 공통 래퍼 + `ApiException` 타입별 예외 매핑
  * **Auth Pipeline:** `AuthInterceptor` / `TokenAuthenticator`(401 자동 토큰 갱신) / `ForbiddenRetryInterceptor` / `CookieInterceptor`
  * **Local Storage:** DataStore Preferences — Auth / User 스토어 분리 설계로 쓰기 경합·로그아웃 영향 범위 최소화
  * **Caching:** `LruCache` 기반 응답 캐싱 (`getOrFetch` 확장)
  * **Network State:** `NetworkMonitorImpl` (`callbackFlow` 기반 실시간 연결 상태 감지)
  * **Image Upload:** Presigned URL 기반 업로드 플로우 (`data/presigned`)

  ### 🔒 Security
  * **Encryption:** Google Tink 기반 암호화 (`CryptoManagerImpl`)
  * **Key Management:** Android Keystore 하드웨어 기반 키셋 관리 (`AndroidKeysetManager`)
  * **AAD:** 컨텍스트별 Associated Data 계층 적용 (`AadContext`)

  ### 🎨 UI/UX & Custom Components
  * **Design System:** `MeongColors` (`staticCompositionLocalOf` 기반 커스텀 색상 시스템) + 재사용 컴포넌트 세트 (button / chip / dialog / bottomsheet / textfield / topbar …)
  * **Image Loading:** Coil (`coil-compose`, `coil-gif`)
  * **Animation:** Lottie Compose
  * **Loading State:** Compose Shimmer 기반 스켈레톤 UI
  * **Custom UI:**
    * 커스텀 캘린더 날짜 선택기 / 휠 타임피커
    * 커스텀 Compose Popup 기반 툴팁·말풍선 (자체 구현, SVG 기반 커스텀 Shape)
    * 드래그 앤 드롭 정렬 (`DragDropState`)
  * **Immutable Collections:** `kotlinx-collections-immutable` 적용으로 Compose 재구성 안정성 확보
  * **Date/Time:** `kotlinx-datetime`

  ### ⚙️ Build & Tools
  * **Build:** AGP 9.x, Gradle Version Catalog
  * **Encryption:** Google Tink 기반 암호화 (`CryptoManagerImpl`)
  * **Key Management:** Android Keystore 하드웨어 기반 키셋 관리 (`AndroidKeysetManager`)
  * **AAD:** 컨텍스트별 Associated Data 계층 적용 (`AadContext`)

  ### 🎨 UI/UX & Custom Components
  * **Design System:** `MeongColors` (`staticCompositionLocalOf` 기반 커스텀 색상 시스템) + 재사용 컴포넌트 세트 (button / chip / dialog / bottomsheet / textfield / topbar …)
  * **Image Loading:** Coil (`coil-compose`, `coil-gif`)
  * **Animation:** Lottie Compose
  * **Loading State:** Compose Shimmer 기반 스켈레톤 UI
  * **Custom UI:**
    * 커스텀 캘린더 날짜 선택기 / 휠 타임피커
    * 커스텀 Compose Popup 기반 툴팁·말풍선 (자체 구현, SVG 기반 커스텀 Shape)
    * 드래그 앤 드롭 정렬 (`DragDropState`)
  * **Immutable Collections:** `kotlinx-collections-immutable` 적용으로 Compose 재구성 안정성 확보
  * **Date/Time:** `kotlinx-datetime`

  ### ⚙️ Build & Tools
  * **Build:** AGP 9.x, Gradle Version Catalog
  * **Code Generation:** KSP
  * **Release Optimization:** R8 minify + Resource Shrinking (`src/main/keepRules/*.keep` 분리 관리)
  * **Logging:** Timber
  * **Open Source License:** AboutLibraries
  * **External Integration:** Kakao SDK (`v2-auth` 소셜 로그인) + 카카오맵 딥링크(경로 탐색·장소 검색, `kakaomap://` URI 스킴)
