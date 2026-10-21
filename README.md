# F1 Race Countdown

CP3406 / CP5307 – Assessment 1: Utility App

An "at-a-glance" utility app for Formula 1 fans. Open it and immediately see **which race is next, how long
until the next session starts, and when every session of the weekend takes place** – in your own time zone,
with the latest official F1 headlines underneath.

> Unofficial student project. It is not affiliated with or endorsed by Formula 1. The F1 logo and the news
> content belong to their respective owners (see [Data sources](#data-sources)).

## App purpose

Race weekends are scattered across time zones, so fans keep converting "Sunday 20:00 UTC" in their head.
This app answers the three questions a fan actually has, on one screen and without any navigation:

1. **What is the next race?** – name, round, season, circuit and location.
2. **How long until it starts?** – a live countdown (days / hours / minutes / seconds) to the next session or to the race itself.
3. **When is everything on?** – the full weekend timetable in the user's own time zone.

The Settings screen only controls what the main screen shows, as required by the brief.

## Core features

| Screen | What it does |
| --- | --- |
| **Splash** | Short branded intro (F1 logo, red speed streaks) shown once per launch. The race data loads underneath it, so the main screen is ready when the splash fades out. |
| **Home** (main screen) | Race header; live countdown card; "Latest F1 news" block (one top story with photo plus compact headlines that open on formula1.com); weekend timetable with past sessions dimmed and the countdown target marked **NEXT**. The refresh action in the top bar reloads both the race and the news. |
| **Settings** | Time zone (device / UTC), 12- or 24-hour clock, countdown target (next session / race), and which session groups are shown (practice, qualifying incl. sprint qualifying, sprint). The race is always shown. A *Reset to defaults* button restores everything. |

Settings apply instantly and are intentionally **not persisted** (the brief states that settings do not need to be persistent).

Other details:

- Loading, error and "no upcoming race" states, each with a retry action. A news failure never affects the countdown.
- Handles sprint weekends (Sprint Qualifying and Sprint sessions) as well as the older `SprintShootout` key used by earlier seasons.
- Screen-reader friendly: the four countdown boxes are read out as one sentence, and each settings switch row is a single toggle target.
- Dark "race night" Material 3 theme with the F1 red as the only accent, edge-to-edge layout, and a dark launch theme so there is no white flash before the splash.

## How the requirements are met

| Topic (Weeks 1–5) | Implementation |
| --- | --- |
| **Kotlin and Android Studio** | 100 % Kotlin 2.0.21, Gradle Kotlin DSL and a version catalog (`gradle/libs.versions.toml`). Modern APIs throughout: coroutines, `StateFlow`, `java.time`, lifecycle-aware collection. `minSdk 26`, `targetSdk 35`. |
| **Layouts using Jetpack Compose** | Small, reusable composables: `RaceHeader`, `CountdownCard`, `NewsSection`, `SessionScheduleCard`, `SessionRow`, `LoadingContent`, `ErrorContent` and the sections of `SettingsScreen`. State is hoisted, so screens are stateless and previewable (`@Preview`). The `now` clock is passed as a `State<Instant>` and read only by the countdown and schedule composables, so only those recompose every second. The splash uses `Animatable` and a `Canvas`. |
| **Material Design 3** | `MaterialTheme` with a custom colour scheme and typography, `Scaffold`, `TopAppBar`, `NavigationBar`, `Card`, `FilterChip`, `Switch`, `Button` / `TextButton`, `HorizontalDivider`. Colours come from the theme (`Color.kt`, `Theme.kt`). The only hard-coded colours are white and black used for text and scrims on top of photos and the countdown gradient. |
| **App architecture** | **ViewModel**: `UtilityViewModel` exposes `StateFlow`s (`UtilityUiState`, `NewsUiState`, `UtilitySettings`, `now`) and is shared by both screens, so a settings change updates the Home screen immediately. **Repository pattern**: `RaceRepository` / `DefaultRaceRepository` and `NewsRepository` / `DefaultNewsRepository` hide all networking from the UI and return `Result`. **Dependency injection**: a manual `AppContainer` created in `UtilityApplication` and injected through a `ViewModelProvider.Factory`. |
| **Web APIs using Retrofit** | Two Retrofit services sharing one OkHttp client (15 s timeouts and a polite `User-Agent` header). `F1Api` uses the Gson converter to read the Jolpica schedule. `F1NewsApi` returns raw `ResponseBody` for the formula1.com RSS feed and for article pages. See below. |

### Networking in detail

- **Race schedule** – `GET https://api.jolpi.ca/ergast/f1/current/next.json`. The Gson DTOs (`RaceDtos.kt`) are nullable with defaults, so a missing key never crashes parsing. `RaceMapper` converts them to the `Race` domain model: sessions are sorted by start time, the race itself is mandatory and a missing session time falls back to midnight UTC. Failures are translated into `RaceError` (`NO_RACE`, `NETWORK`, `SERVER`, `DATA`), so the UI never sees Retrofit or OkHttp exceptions and shows a friendly message for each.
- **News** – `GET https://www.formula1.com/en/latest/all.xml`. The RSS XML is parsed with `XmlPullParser` (`NewsFeedParser`). For the first six articles the repository looks up each article's `og:image` photo **in parallel** (`async` / `awaitAll` on `Dispatchers.IO`); only the first 200 KB of each page is read (streamed), and an article without a photo is still listed (the top story falls back to a red-to-black gradient). Photos are displayed with Coil (`AsyncImage`).
- Cancellation is never swallowed (`CancellationException` is rethrown), so coroutines stay structured and lifecycle-safe.

## Architecture

```
UI (Compose)         UtilityApp ─ UtilityScreen ─ SettingsScreen ─ SplashScreen
      │ collectAsStateWithLifecycle()      ▲
      ▼                                    │ UtilityUiState / NewsUiState / UtilitySettings / now
UtilityViewModel ── one instance shared by both screens
      │
      ├──► RaceRepository (interface) ◄── DefaultRaceRepository ──► F1Api (Retrofit + Gson) ──► Jolpica F1 API
      │
      ├──► NewsRepository (interface) ◄── DefaultNewsRepository ──► F1NewsApi (Retrofit) ──► formula1.com RSS + article pages
      │
      └──► domain: computeCountdown(), visibleSessions()   (pure functions, unit-tested)

AppContainer (manual DI, created in UtilityApplication) builds the Retrofit/OkHttp objects and the repositories.
```

### Project structure

```
app/src/main/java/com/example/cp3406_assessment_younghoocheon/
├── MainActivity.kt, UtilityApplication.kt
├── data/
│   ├── remote/      F1Api.kt, RaceDtos.kt, F1NewsApi.kt, NewsFeedParser.kt, OgImageParser.kt
│   ├── mapper/      RaceMapper.kt
│   ├── model/       Race.kt, NewsArticle.kt
│   └── repository/  RaceRepository.kt, DefaultRaceRepository.kt, NewsRepository.kt, DefaultNewsRepository.kt
├── di/              AppContainer.kt
├── domain/          UtilitySettings.kt, SessionSchedule.kt
├── util/            TimeFormatting.kt
└── ui/
    ├── UtilityApp.kt, AppDestination.kt
    ├── splash/      SplashScreen.kt
    ├── utility/     UtilityScreen.kt, UtilityViewModel.kt, UtilityUiState.kt, NewsUiState.kt, SessionLabels.kt
    │   └── components/  CountdownCard.kt, NewsSection.kt, RaceHeader.kt, SessionRow.kt,
    │                    SessionScheduleCard.kt, StatusMessages.kt
    ├── settings/    SettingsScreen.kt
    ├── preview/     PreviewData.kt
    └── theme/       Color.kt, Type.kt, Theme.kt
```

The starter template structure is kept: `UtilityApp()` (scaffold with bottom navigation), `UtilityScreen()` (core
utility) and `SettingsScreen()` (preferences), all inside a single activity.

## Tech stack

| Area | Library / version |
| --- | --- |
| Build | Android Gradle Plugin 8.7.3, Kotlin 2.0.21 (Compose compiler plugin), `compileSdk 35` |
| UI | Jetpack Compose UI 1.7.5, Material 3 1.3.1, Material icons core 1.7.5, Activity Compose 1.9.3 |
| Lifecycle | Lifecycle runtime / ViewModel / runtime-compose 2.8.7 |
| Async | Kotlin Coroutines 1.9.0 |
| Networking | Retrofit 2.11.0, Gson converter, OkHttp 4.12.0 |
| Images | Coil 2.7.0 |
| Tests | JUnit 4.13.2 |

## Running the app

1. Open the project root folder in Android Studio (Ladybug or newer; JDK 17+ – the bundled JDK is fine).
2. Let Gradle sync finish (an internet connection is needed to download dependencies).
3. Run the `app` configuration on an emulator or device with Android 8.0 (API 26) or higher.
   The device needs internet access to load the schedule and the news.

Run the unit tests with `./gradlew testDebugUnitTest` or right-click `app/src/test` → *Run Tests*.

## Tests

25 plain JVM unit tests (no emulator needed):

| Test class | Covers |
| --- | --- |
| `RaceDtoParsingTest` | Real Jolpica JSON → DTO → domain model, including a sprint weekend |
| `RaceMapperTest` | Standard and sprint weekends, sorting, legacy `SprintShootout`, missing time / date, safe defaults |
| `DefaultRaceRepositoryTest` | Error handling with a fake `F1Api`: success, empty list, missing body, `IOException`, unusable race |
| `SessionScheduleTest` | Countdown maths, next-session and race modes, hidden sessions, weekend over, race always visible |
| `TimeFormattingTest` | 24-hour time, time zone conversion, day roll-over (UTC vs Pacific/Auckland) |

## Data sources

- **Schedule**: community-run [Jolpica F1 API](https://github.com/jolpica/jolpica-f1) (Ergast-compatible). The app makes one small request per launch / refresh, well within the API's rate limits.
- **News**: the public RSS feed and article pages of [formula1.com](https://www.formula1.com/en/latest). Headlines, summaries and photos belong to Formula 1; tapping a story opens the original article in the browser.
- The F1 logo is used for identification only, in this non-commercial student project.

## AI assistance

Generative AI (Claude, by Anthropic) was used to help build and review this project.
The use is documented in [`docs/AI_DECLARATION.md`](docs/AI_DECLARATION.md). The self-reflection is submitted as a separate PDF through LearnJCU.