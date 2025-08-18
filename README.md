# homepage_init_deposit_app

SCSC 홈페이지 입금 확인 앱 문서

> 최초 작성일: 2025-08-17
>
> 최신 개정일: 2025-08-18
>
> 최신 개정자: [이한경TI](mailto:tteokgook@gmail.com)
>
> 작성자: [강명석CSE](mailto:tomskang@naver.com), [이한경TI](mailto:tteokgook@gmail.com)

## 브랜치

- main: 배포 버전 코드. 태그로 구별합니다.
- develop: 개발 중인 코드를 저장합니다.

## 개발 환경

### Android Studio / Build Toolchain

Android Studio: 2025.1.2 (Narwhal Feature Drop)

<details>
<summary>About 상세 정보 (펼치기)</summary>

안드로이드 스튜디오 상단 메뉴 Help > About 에서 확인할 수 있는 안드로이드 스튜디오 등 버전은 아래와 같습니다.

참고: 아래 정보는 IDE 런타임 정보로, 프로젝트의 Gradle/빌드 JDK 설정과는 다를 수 있습니다(빌드 JDK는 아래 JDK 섹션 참고).

```text
Android Studio Narwhal Feature Drop | 2025.1.2
Build #AI-251.26094.121.2512.13840223, built on July 27, 2025
Runtime version: 21.0.6+-13391695-b895.109 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
Toolkit: sun.awt.windows.WToolkit
Windows 11.0
Kotlin plugin: K2 mode
GC: G1 Young Generation, G1 Concurrent GC, G1 Old Generation
Memory: 8192M
Cores: 12
Registry:
ide.experimental.ui=true
```

</details>

## 프로젝트 설정

### 요구 안드로이드 버전

요구 안드로이드 버전은 `app/build.gradle.kts`에 설정되어 있고 값은 다음과 같습니다.

- **minSdk = 31 (Android 12)**
- targetSdk = 36 (Android 16)
- compileSdk = 36

### 빌드 도구 등 버전

- targetCompatibility: Java 11
- AGP (Android Gradle Plugin): 8.12.0
- Gradle: 8.13
- Gradle JDK: 21
- Kotlin: 2.2.10

### 앱 권한

- `android.permission.BIND_NOTIFICATION_LISTENER_SERVICE`: `NotificationListenerService`를 사용하기 위한 특수
  권한입니다. 이 권한은 `uses-permission`으로 선언하지 않으며, 서비스 선언 시
  `android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE"`로 지정합니다. 또한 런타임 권한 요청
  대상이 아니며, 사용자가 시스템 설정에서 앱의 ‘알림 접근’을 허용해야 동작합니다.
- `android.permission.INTERNET`: 인터넷 권한(정상 권한)으로, 설치 시 자동 부여되며 런타임 요청이 필요 없습니다.
- `android.permission.ACCESS_NETWORK_STATE`: 네트워크 연결 상태를 확인하는 데 필요한 권한(정상 권한)입니다.

## 실행 방법

### 설정 파일

- `app/src/main/res/values/secrets.xml` 파일이 필요합니다.

- TODO: 실행 방법 문서화 예정
