package com.example.postservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * UserClient 클래스는 User Service와 통신하여 사용자 정보를 가져오는 역할을 담당합니다.
 * RestTemplate을 사용해 HTTP 요청을 수행하며, 관련 URL은 환경 설정 값에서 가져옵니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserClient {

    // RestTemplate 객체로 HTTP 요청을 전송
    private final RestTemplate restTemplate;

    // User Service의 Base URL, 환경 설정(application.yml 등)에서 값을 읽음
    @Value("${userservice.base-url:http://localhost:8081}")
    private String userServiceBaseUrl;

    /**
     * 지정된 사용자 ID에 대한 REST API URL을 생성합니다.
     *
     * @param userId 사용자 ID
     * @return 호출에 사용할 URL (예: http://localhost:8081/users/1)
     */
    private String getUserUrl(Long userId) {
        // userServiceBaseUrl이 '/'로 끝나면 제거
        String base = userServiceBaseUrl.endsWith("/")
            ? userServiceBaseUrl.substring(0, userServiceBaseUrl.length() - 1)
            : userServiceBaseUrl;

        // 사용자 ID를 포함한 URL 반환
        return base + "/users/" + userId;
    }

    /**
     * User ID를 사용해 User Service에서 사용자의 이름을 가져옵니다.
     *
     * @param userId 사용자 ID
     * @return 사용자 이름 (API 호출 성공 시) 또는 "Unknown User" (호출 실패 시)
     */
    public String getUserName(Long userId) {
        try {
            // GET 요청을 통해 User Service로부터 사용자 정보를 가져옴
            ResponseEntity<UserResponseDto> response = restTemplate.getForEntity(getUserUrl(userId), UserResponseDto.class);

            // 응답에서 본문 데이터 추출
            UserResponseDto body = response.getBody();

            // 본문 데이터가 없으면 "Unknown User" 반환
            return body != null ? body.getName() : "Unknown User";
        } catch (Exception e) {
            // 호출에 실패했을 경우 경고 메시지 기록
            log.warn("UserService 호출 실패 – 기본값 반환");
            return "Unknown User";
        }
    }
}