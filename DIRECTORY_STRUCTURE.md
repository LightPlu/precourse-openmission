
## 디렉토리 구조

```
precourse-openmission/
│
├── src/
│   ├── main/
│   │   └── java/lotto/
│   │       ├── Application.java                    # 메인 애플리케이션 진입점
│   │       │
│   │       ├── presentation/                       # 표현 계층
│   │       │   ├── controller/
│   │       │   │   ├── LottoController.java       # 메인 컨트롤러
│   │       │   │   ├── MenuCommand.java           # 메뉴 커맨드 Enum
│   │       │   │   └── common/
│   │       │   │       └── ApplicationErrorMessage.java
│   │       │   └── view/
│   │       │       ├── UserInputView.java         # 사용자 입력 뷰
│   │       │       ├── UserOutputView.java        # 사용자 출력 뷰
│   │       │       └── utils/
│   │       │           └── TypeConverter.java     # 타입 변환 유틸
│   │       │
│   │       ├── application/                        # 애플리케이션 계층
│   │       │   ├── common/
│   │       │   │   └── ServiceErrorMessage.java
│   │       │   ├── lottoService/
│   │       │   │   ├── LottoPurchaseService.java  # 로또 구매 서비스
│   │       │   │   ├── TicketApplicationService.java
│   │       │   │   └── TicketApplicationServiceImpl.java
│   │       │   └── roundService/
│   │       │       ├── RoundApplicationService.java
│   │       │       ├── RoundApplicationServiceImpl.java
│   │       │       ├── RoundResultApplicationService.java
│   │       │       ├── RoundResultApplicationServiceImpl.java
│   │       │       ├── WinningLottoApplicationService.java
│   │       │       └── WinningLottoApplicationServiceImpl.java
│   │       │
│   │       ├── domain/                             # 도메인 계층 (핵심)
│   │       │   ├── common/
│   │       │   │   ├── DomainErrorMessage.java
│   │       │   │   └── NumberConstants.java
│   │       │   │
│   │       │   ├── lottoTicket/                   # 로또 티켓 애그리거트
│   │       │   │   ├── entity/
│   │       │   │   │   └── LottoTicket.java      # 로또 티켓 엔티티
│   │       │   │   ├── vo/
│   │       │   │   │   ├── Lotto.java            # 로또 번호 집합 VO
│   │       │   │   │   └── LottoNumber.java      # 로또 번호 VO
│   │       │   │   └── repository/
│   │       │   │       └── LottoTicketRepository.java
│   │       │   │
│   │       │   ├── round/                         # 회차 애그리거트
│   │       │   │   ├── entity/
│   │       │   │   │   └── Round.java            # 회차 엔티티
│   │       │   │   ├── vo/
│   │       │   │   │   ├── CountResult.java      # 당첨 개수 결과 VO
│   │       │   │   │   ├── RoundResult.java      # 회차 결과 VO
│   │       │   │   │   └── WinningLottoNumbers.java # 당첨 번호 VO
│   │       │   │   └── repository/
│   │       │   │       └── RoundRepository.java
│   │       │   │
│   │       │   ├── service/                       # 도메인 서비스
│   │       │   │   ├── LottoCompareService.java  # 로또 비교 서비스
│   │       │   │   └── LottoNumberGenerator.java # 로또 번호 생성 서비스
│   │       │   │
│   │       │   └── vo/                            # 공통 Value Objects
│   │       │       ├── Cash.java                 # 금액 VO
│   │       │       └── Rank.java                 # 등수 VO
│   │       │
│   │       ├── infrastructure/                     # 인프라 계층
│   │       │   ├── RandomNumberGenerator.java     # 난수 생성 인터페이스
│   │       │   ├── RandomNumberGeneratorImpl.java # 난수 생성 구현체
│   │       │   ├── db/
│   │       │   │   └── DBConnectionManager.java   # DB 연결 관리자
│   │       │   └── repository/
│   │       │       ├── PostgresLottoTicketRepository.java
│   │       │       └── PostgresRoundRepository.java
│   │       │
│   │       └── config/
│   │           └── ApplicationConfig.java          # DI 설정 (Composition Root)
│   │
│   └── test/
│       └── java/lotto/
│           ├── application/                        # 애플리케이션 테스트
│           │   ├── lottoService/
│           │   │   └── TicketApplicationServiceImplTest.java
│           │   └── roundService/
│           │       ├── RoundApplicationServiceImplTest.java
│           │       ├── RoundResultApplicationServiceImplTest.java
│           │       └── WinningLottoApplicationServiceImplTest.java
│           │
│           ├── domain/                             # 도메인 테스트
│           │   ├── lottoTicket/
│           │   │   ├── entity/
│           │   │   │   └── LottoTicketTest.java
│           │   │   └── vo/
│           │   │       ├── LottoNumberTest.java
│           │   │       └── LottoTest.java
│           │   ├── round/
│           │   │   ├── entity/
│           │   │   │   └── RoundTest.java
│           │   │   └── vo/
│           │   │       ├── RoundResultTest.java
│           │   │       └── WinningLottoNumbersTest.java
│           │   ├── service/
│           │   │   ├── LottoCompareServiceTest.java
│           │   │   ├── LottoNumberGeneratorTest.java
│           │   │   └── LottoPurchaseServiceTest.java
│           │   └── vo/
│           │       ├── CashTest.java
│           │       └── RankTest.java
│           │
│           ├── integration/                        # 통합 테스트
│           │   └── PostgresRoundRepositoryIntegrationTest.java
│           │
│           └── presentation/
│               └── controller/
│                   └── MenuCommandTest.java
```
