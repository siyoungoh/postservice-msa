# Post Service

## 개요
Post Service는 사용자 게시물을 관리하는 마이크로서비스입니다.  
이 서비스는 User Service와 통신하여 사용자의 이름을 조회하며, RESTful API를 통해 게시물 데이터를 제공합니다.

---

## 주요 구성 요소 설명

### PostController
- **클래스 경로:** `com.example.postservice.PostController`
- **역할:** 게시물 관련 HTTP 요청을 처리합니다.
- **주요 메서드:**
    - `getPosts()`: 모든 게시물을 반환.
    - `getPost(Long id)`: 특정 ID의 게시물을 반환.
- User Service에서 사용자 이름을 가져오기 위해 **UserClient**를 호출합니다.

---

### PostResponseDto
- **클래스 경로:** `com.example.postservice.PostResponseDto`
- **역할:** 게시물 데이터를 클라이언트에 반환하기 위한 DTO.
- **구성 필드:**
    - 게시물 ID, 제목, 내용, 작성자 정보 등을 포함합니다.

---

### UserClient
- **클래스 경로:** `com.example.postservice.UserClient`
- **역할:** User Service와 통신하여 사용자 정보를 가져옵니다.
- User Service의 기본 URL(`userservice.base-url`)은 `application.yml`에서 설정됩니다.
- **주요 메서드:**
    - `getUserName(Long userId)`: 특정 User ID로 User Service를 호출하여 사용자 이름을 가져옵니다.

---

### HttpClientConfig
- **클래스 경로:** `com.example.postservice.HttpClientConfig`
- **역할:** 외부 서비스와의 HTTP 통신을 위한 설정을 관리합니다.
- WebClient 또는 RestTemplate 구성을 제공하여 User Service와의 안정적인 통신을 보장합니다.

---

### UserResponseDto
- **클래스 경로:** `com.example.postservice.UserResponseDto`
- **역할:** User Service에서 가져온 사용자의 정보를 저장하는 DTO.
- **구성 필드:**
    - `id (Long)`: 사용자 ID.
    - `name (String)`: 사용자 이름.

---

## 환경 설정 (application.yml)

### 서버 설정
| **설정 키**      | **값**        | **설명**                |
|------------------|---------------|------------------------|
| `server.port`    | `8080`        | Post Service의 실행 포트 |

### User Service 설정
| **설정 키**              | **값**                     | **설명**                              |
|-------------------------|----------------------------|--------------------------------------|
| `userservice.base-url`  | `http://userservice:8081`  | User Service의 기본 URL (도커 네트워크에서 사용) |

---

## API 테스트

API를 간편하게 테스트할 수 있는 `.http` 파일이 제공됩니다.

- **파일 경로:** `ApiEndpoints.http`

### 예제 요청

1. **게시물 목록 조회**
```
GET http://localhost:8080/posts
   Accept: application/json
```

2. **특정 게시물 조회 (ID: 1)**
```
GET http://localhost:8080/posts/1
   Accept: application/json
```

3. **특정 게시물 조회 (ID: 2)**
```
GET http://localhost:8080/posts/2
   Accept: application/json
```

**실행 방법:**  
`.http` 파일에서 원하는 요청 위에 커서를 두고 **실행 버튼**을 클릭하여 테스트를 진행합니다.

---

## 빌드 및 실행

서비스 실행을 위한 `Dockerfile` 설정이 포함되어 있습니다.

### 도커 빌드 및 실행

1. **이미지 빌드**
```shell script
docker build -t postservice .
```

2. **도커 컨테이너 실행**
```shell script
docker run -p 8080:8080 postservice
```

**Dockerfile 상세설명**

- `gradle` 이미지를 활용하여 빌드하고, 빌드된 `.jar` 파일을 런타임 환경(`eclipse-temurin`)에 배포합니다.
- **주요 단계:**
    1. `gradle clean bootJar`로 애플리케이션 빌드.
    2. 빌드된 `.jar` 파일을 런타임 환경으로 복사.
    3. 기본 실행 포트(`8080`)과 메모리 설정(`JAVA_OPTS`)을 적용.

---

## 도커 컴포즈

Post Service와 User Service를 함께 실행하려면 `docker-compose`를 사용할 수 있습니다.

```yaml
services:
  postservice:
    build: .
    ports:
      - "8080:8080"

  userservice:
    image: userservice-image
    ports:
      - "8081:8081"
```

---

## 문제 해결

1. **User Service 연결 불가**
    - `userservice.base-url` 값을 확인하세요.
    - 도커 환경에서는 `http://userservice:8081`를 사용해야 합니다.

2. **포트 충돌**
    - 로컬 및 도커 상에서 지정된 포트(`8080`, `8081`)가 다른 서비스와 충돌하지 않는지 확인하세요.

---

## 기여

- 이 프로젝트에 기여하고 싶다면 다음 단계를 따르세요:
    1. 이 저장소를 포크합니다.
    2. 새 브랜치를 만듭니다. (`git checkout -b feature/기능이름`)
    3. 변경 사항을 커밋합니다. (`git commit -m 'feat: 기능 추가 설명'`)
    4. 브랜치를 푸시합니다. (`git push origin feature/기능이름`)
    5. Pull Request를 생성합니다.