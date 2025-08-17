# homepage_init_deposit_app

SCSC 홈페이지 입금 확인 앱 문서

> 최초 작성일: 2025-08-17
>
> 최신 개정일: 2025-08-17
>
> 최신 개정자: [이한경TI]()
>
> 작성자: [강명석CSE](mailto:tomskang@naver.com), [이한경TI]()

## 브랜치

- main: 배포 버전 코드. 태그로 구별합니다.
- develop: 개발 중인 코드를 저장합니다.

## 개발 환경

### Android Studio

안드로이드 스튜디오 상단 메뉴 Help > About 에서 확인할 수 있는 안드로이드 스튜디오 등 버전은 아래와 같습니다.

```
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

## 앱 설정

### 요구 안드로이드 버전

요구 안드로이드 버전은 `app/build.gradle.kts`에 설정되어 있고 값은 다음과 같습니다.

- **minSdk=31(android 12)**
- targetSdk=36(android 16)
- compileSdk=36

### 앱 권한

- `android.permission.BIND_NOTIFICATION_LISTENER_SERVICE`: 임의의 알림을 읽는 `NotificationListenerService`를
  사용하기 위한 권한입니다.
- `android.permission.INTERNET`: 인터넷 권한입니다.

## 실행 방법

- writing...
