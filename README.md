# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

### step 1. 인증 기반 인수 테스트 도구
1. 기능 요구사항
   1. 지하철역, 노선, 구간을 변경하는 API 는 `관리자`만 접근이 가능하도록 수정!
      1. 해당 API: 생성, 수정, 삭제 API
      2. 조회 API 는 권한이 필요없음
   2. Form 기반 로그인과 Bearer 기반 로그인 기능을 구현!
      1. `UsernamePasswordAuthenticationFilter`, `BearerTokenAuthenticationFilter` 를 구현!
      2. `AuthAcceptanceTest` 테스트를 통해 기능 구현을 확인!
2. 요구사항 설명
   1. **관리자 전용 API 접근 제한**
   2. **초기 설정**
      1. 관리자 전용 API 에 대한 접근 제한 기능을 구현하기 위해서는 관리자 멤버와 역할이 `미리 설정`되어있어야 한다.
      2. 인수 테스트를 수행하기 전 공통으로 필요한 `멤버`, `역할`은 초기에 설정할 수 있도록 하자.
      3. `DataLoader` 를 활용할 수 있다.
   3. **권한 검증**
      1. API 별 권한 검증을 위해 `@Secured` 를 활용하자.
```java
public class StationController {

   @PostMapping("/stations")
   @Secured("ROLE_ADMIN")
   public ResponseEntity<StationRepsonse> createStation(@RequestBody StationRequest stationRequest) {
      StationResponse station = stationService.saveStation(stationRequest);
      return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
   }
}
```