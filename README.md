# Bookstore API Automation Tests

Automated API test framework for the **FakeRestAPI Online Bookstore**, covering the
**Books** and **Authors** endpoints. Built with **Java 17 + RestAssured + TestNG**,
reported with **Allure**, and wired for CI on **GitHub Actions** and **Jenkins**.

- Target API: `https://fakerestapi.azurewebsites.net/api/v1`
- Endpoints under test: `/Books`, `/Books/{id}`, `/Authors`, `/Authors/{id}`

---

## 1. Tech stack

| Concern            | Choice                          |
|--------------------|---------------------------------|
| Language           | Java 17                         |
| Build tool         | Maven                           |
| HTTP / API testing | RestAssured 5                   |
| Test runner        | TestNG 7                        |
| Reporting          | Allure + Surefire (JUnit XML)   |
| CI/CD              | GitHub Actions & Jenkins        |

---

## 2. Prerequisites

- **JDK 17** (`java -version` should report 17)
- **Maven 3.9+** (`mvn -version`) — or use IntelliJ's bundled Maven
- Internet access (the suite hits the live FakeRestAPI)
- *(Optional)* **Allure CLI** for opening the HTML report locally:
  `brew install allure` / `scoop install allure`

---

## 3. Open in IntelliJ IDEA

1. **File → Open…** and select the project root (the folder containing `pom.xml`).
2. IntelliJ auto-detects Maven and downloads dependencies. If not:
   right-click `pom.xml` → **Add as Maven Project**.
3. Set the Project SDK to **17** (*File → Project Structure → Project*).
4. Run tests by right-clicking `testng.xml` → **Run**, or any test class.

---

## 4. Run the tests (command line)

```bash
# Run the whole suite
mvn clean test

# Run a single class
mvn clean test -Dtest=GetBooksTests

# Point the suite at a different environment without changing code
mvn clean test -DbaseUri=https://staging.example.com -DbasePath=/api/v1
```

Configuration precedence (highest first): **JVM system property → environment
variable → `config.properties`**. So CI can inject `BASE_URI` while local runs use
the defaults in `src/test/resources/config.properties`.

---

## 5. Reporting

### Allure (rich HTML report)

```bash
mvn clean test                                   # produces target/allure-results
mvn io.qameta.allure:allure-maven:report         # renders target/site/allure-maven-plugin
mvn io.qameta.allure:allure-maven:serve          # or open it directly in a browser
```

### Surefire (plain summary)

After any run, `target/surefire-reports/` contains the pass/fail summary per test,
including JUnit-style XML consumed by the CI publishers.

---

## 6. Project structure

```
bookstore-api-tests/
├── pom.xml                     # Build + dependencies + Surefire/Allure config
├── testng.xml                  # Suite definition (Books + Authors, parallel by class)
├── Jenkinsfile                 # Jenkins pipeline
├── .github/workflows/ci.yml    # GitHub Actions pipeline
├── README.md
└── src/test/
    ├── java/com/allwyn/bookstore/
    │   ├── config/     # ConfigManager (env-aware), Endpoints (paths)
    │   ├── models/     # Book, Author POJOs with fluent builders
    │   ├── clients/    # BaseApiClient + BookApiClient + AuthorApiClient
    │   ├── data/       # Test-data factories (randomised fixtures)
    │   ├── base/       # BaseTest — shared setup, ready-to-use clients
    │   └── tests/
    │       ├── books/    # GET / POST / PUT / DELETE Books tests
    │       └── authors/  # GET / POST / PUT / DELETE Authors tests
    └── resources/
        ├── config.properties
        └── allure.properties
```

### Design notes (SOLID / clean code)

- **Single Responsibility** — request-building lives only in `BaseApiClient`;
  endpoint clients only describe *which* call; tests only *assert*.
- **Open/Closed** — new endpoints are added by extending `BaseApiClient`,
  no existing class changes.
- **Dependency Inversion** — tests depend on client abstractions and read config
  through `ConfigManager`, not on hard-coded URLs.
- **DRY / reusability** — POJO builders and data factories remove payload
  duplication; a single shared `RequestSpecification` centralises headers,
  timeouts and Allure logging.

---

## 7. Test coverage

Both **happy paths** and **edge cases** are covered for every endpoint:

- **Books** — list all, get by id, create (valid / minimal / empty / malformed /
  boundary page count), update (valid / bad id / large id), delete (valid / bad id /
  non-existent), plus response-time and schema-shape checks.
- **Authors** — list all, get by id, create (valid / empty / malformed),
  update, delete, with the same edge-case matrix.

Negative ids use TestNG `@DataProvider` to keep cases data-driven.

> **Note on FakeRestAPI:** it is a stateless demo — writes are echoed but not
> persisted. Tests therefore assert on **status codes and echoed payloads** rather
> than on re-reading created records. A couple of edge cases assert only "no 5xx"
> because the demo API is lenient about validation; those are intentionally tolerant
> and documented in-code.

---

## 8. CI/CD

### GitHub Actions — `.github/workflows/ci.yml`
Runs on every push/PR to `main`/`master` (and manually). Steps: checkout → JDK 17 →
`mvn clean test` → generate Allure report → upload Allure + Surefire artifacts →
publish a JUnit test summary on the run.

### Jenkins — `Jenkinsfile`
Declarative pipeline: checkout → `mvn clean test` → publish JUnit results and the
Allure report (requires the **Allure** Jenkins plugin; tool names `jdk-17` /
`maven-3.9` must match your Jenkins tool config).

---

## 9. Extras (beyond the brief)

- **Contract / JSON-schema validation** (`tests/contract`) — every GET response is
  validated against a JSON schema under `src/test/resources/schemas/`, asserting
  field presence and types, not just status codes. This catches breaking API
  changes (a renamed or retyped field) that value assertions would miss.
- **Automatic retry of flaky tests** (`support/`) — `RetryAnalyzer` retries a
  failing test up to twice, applied globally via `RetryTransformer` (registered as
  a listener in `testng.xml`). The FakeRestAPI is a shared public demo that
  occasionally has transient hiccups; this keeps the report focused on genuine
  failures without touching a single `@Test`.
- **Parallel execution** — the suite runs classes in parallel (`thread-count=4`)
  for faster feedback; shared state in the chained workflows is confined to a
  single class, which TestNG runs on one thread, so it stays safe.
