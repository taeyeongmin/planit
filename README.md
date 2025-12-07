## 1.실행 방법

### 사전 준비

* **JDK 21 이상**
* **Gradle** 또는 **IntelliJ IDEA (Spring Boot 지원)**

### 실행 명령

```bash
# 클린 빌드 후 실행
./gradlew clean bootRun
```

---
## 2.설계한 REST API 명세 요약
자세한 내용은 스웨거 참조(http://localhost:8080/swagger-ui/index.html)
### 1. 공휴일 최신화 API 
```
URL: POST /api/holiday/refresh
param:
{
  "countryCode": "CH",
  "year": 2024
}
Response: OK

```

### 2. 공휴일 제거 API 
```
URL: DELETE /api/holiday
param:
{
  "countryCode": "CH",
  "year": 2025
}
Response: 
{
  "countryCode": "CH",
  "year": 2025,
  "deletedCount": 20
}
```

### 3. 공휴일 조회 API 
```
URL: GET /api/holiday/search?countryCode=CH&year=2025&page=0&size=20
param: countryCode=CH&year=2025&page=0&size=20
Response: 
{
 "content": [
    {
      "date": "2025-01-01",
      "localName": "Neujahr",
      "name": "New Year's Day",
      "countryCode": "CH",
      "fixed": false,
      "global": true,
      "counties": [],
      "launchYear": null,
      "types": [
        "PUBLIC"
      ]
  },
  ...  
}
```

---

## 3. ./gradlew clean test 성공 스크린샷
<img width="1380" height="147" alt="image" src="https://github.com/user-attachments/assets/343f898a-5acc-49d8-9171-e3e5805a3c74" />
---
## 4.Swagger UI 주소
```
http://localhost:8080/swagger-ui/index.html
```
---

