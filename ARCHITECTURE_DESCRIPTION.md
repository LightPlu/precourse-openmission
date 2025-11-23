## 아키텍처

### Layered Architecture + DDD

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                   │
│             (Controller, View, Input/Output)            │
└────────────────────────────┬────────────────────────────┘
                             │
┌────────────────────────────┴────────────────────────────┐
│                    Application Layer                    │
│             (Application Services, Use Cases)           │
└────────────────────────────┬────────────────────────────┘
                             │
┌────────────────────────────┴────────────────────────────┐
│                       Domain Layer                      │
│        (Entities, Value Objects, Domain Services)       │
└────────────────────────────┬────────────────────────────┘
                             │
┌────────────────────────────┴────────────────────────────┐
│                    Infrastructure Layer                 │
│       (Repository Implementations, DB, External APIs)   │
└─────────────────────────────────────────────────────────┘
```

### 의존성 방향
- **Presentation** → **Application** → **Domain** ← **Infrastructure**
- Domain Layer는 어떤 계층에도 의존하지 않음 (DIP 원칙)

---

## 계층별 상세 설명

### 1️⃣ Presentation Layer (표현 계층)

**역할**: 사용자 인터페이스 및 입출력 처리

#### 📂 controller/
- **LottoController**: 애플리케이션의 메인 흐름 제어
- **MenuCommand**: 메뉴 선택을 Enum으로 관리하여 확장성 확보

#### 📂 view/
- **UserInputView**: 사용자로부터 입력 받기
- **UserOutputView**: 결과를 사용자에게 출력
- **TypeConverter**: 입력값 타입 변환 유틸리티

---

### 2️⃣ Application Layer (애플리케이션 계층)

**역할**: 유스케이스 구현 및 도메인 서비스 조율

#### 📂 lottoService/
- **LottoPurchaseService**: 로또 구매 로직
- **TicketApplicationService**: 티켓 발행 및 조회

#### 📂 roundService/
- **RoundApplicationService**: 회차 생성 및 조회
- **RoundResultApplicationService**: 회차 결과 계산 및 조회
- **WinningLottoApplicationService**: 당첨 번호 등록 및 조회

**특징**:
- 인터페이스와 구현체 분리 (DIP 준수)
- 도메인 모델을 DTO로 변환하여 외부 노출 최소화

---

### 3️⃣ Domain Layer (도메인 계층) ⭐

**역할**: 비즈니스 로직의 핵심, 다른 계층에 의존하지 않음

#### 📦 Aggregate Root
1. **Round** (회차 애그리거트)
2. **LottoTicket** (로또 티켓 애그리거트)

#### 📂 lottoTicket/ (로또 티켓 애그리거트)

##### Entity
- **LottoTicket**: 식별자를 가진 로또 티켓
    - 한 티켓당 최소 1개, 최대 5개의 Lotto 포함

##### Value Object
- **Lotto**: 6개의 로또 번호 집합 (불변)
- **LottoNumber**: 1~45 사이의 로또 번호 (불변)

#### 📂 round/ (회차 애그리거트)

##### Entity
- **Round**: 회차 정보를 관리하는 애그리거트 루트
    - 회차 번호 관리
    - 회차 상태 관리

##### Value Object
- **WinningLottoNumbers**: 당첨 번호 (6개 번호 + 보너스 번호)
- **RoundResult**: 회차 당첨 결과 (등수별 당첨 횟수)
- **CountResult**: 개별 로또의 맞춘 번호 개수

#### 📂 vo/ (공통 Value Objects)
- **Cash**: 금액 및 구매 가능 횟수 계산
- **Rank**: 당첨 등수 및 상금 정보

#### 📂 service/ (도메인 서비스)
- **LottoNumberGenerator**: 로또 번호 자동 생성
- **LottoCompareService**: 로또 번호 비교 및 당첨 여부 판단

**도메인 규칙**:
- 로또 번호는 1~45 사이
- 한 게임당 중복 없는 6개 번호
- 1,000원당 1게임 발행
- 보너스 번호 일치 시 2등 판정

---

### 4️⃣ Infrastructure Layer (인프라 계층)

**역할**: 외부 시스템 연동 (DB, API 등)

#### 📂 db/
- **DBConnectionManager**: PostgreSQL 연결 관리

#### 📂 repository/
- **PostgresRoundRepository**: Round 애그리거트 영속화
- **PostgresLottoTicketRepository**: LottoTicket 애그리거트 영속화

#### 기타
- **RandomNumberGeneratorImpl**: 난수 생성 구현 (java.util.Random 사용)

---

### 5️⃣ Config Layer

#### ApplicationConfig
- **Composition Root**: 수동 의존성 주입
- 모든 객체 생성 및 의존성 조립을 한 곳에서 관리
- Spring 없이 DI 구현