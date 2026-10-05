# F1 Race Countdown

CP3406 / CP5307 – Assessment 1: Utility App

An "at-a-glance" utility app for Formula 1 fans. Open it and immediately see **what the next race is,
how long until the next session starts, and when every session of the weekend takes place** – in your own
time zone. Nothing else.

## Core features

| Screen | What it does |
| --- | --- |
| **Race** (main screen) | Race name, round, circuit and location; a live countdown (days / hours / minutes / seconds) to the next session or to the race; the weekend timetable with past sessions dimmed and the countdown target highlighted. |
| **Settings** | Controls the main screen: time zone (device / UTC), 12- or 24-hour clock, countdown target (next session / race) and which session groups are shown (practice, qualifying, sprint). Settings apply instantly and are intentionally not persisted (per the assignment brief). |

Extra touches: loading, error and "no upcoming race" states with a retry button, a refresh action in the top bar,
screen-reader friendly countdown description, light/dark theme and edge-to-edge layout.

## How the requirements are met

| Topic (Weeks 1–5) | Implementation |
| --- | --- |
| Kotlin and Android Studio | 100 % Kotlin, Gradle Kotlin DSL and a version catalog (`gradle/libs.versions.toml`). |
| Jetpack Compose layouts | Small, reusable composables: `RaceHeader`, `CountdownCard`, `SessionScheduleCard`, `SessionRow`, `LoadingContent`, `ErrorContent`, `SettingsScreen` sections. State is hoisted; screens are stateless and previewable (`@Preview`). |
| Material Design 3 | `MaterialTheme` with light/dark colour schemes, `Scaffold`, `CenterAlignedTopAppBar`, `NavigationBar`, `Card`, `FilterChip`, `Switch`, tonal `primaryContainer` hero card, theme colours and typography everywhere (no hard-coded colours in composables). |
| App architecture | **ViewModel** (`UtilityViewModel`, `StateFlow`-based `UtilityUiState`), **Repository pattern** (`RaceRepository` / `DefaultRaceRepository`), **dependency injection** through a manual `AppContainer` created in `UtilityApplication` and injected with a `ViewModelProvider.Factory`. |
| Web APIs using Retrofit | `F1Api` (Retrofit + OkHttp + Gson) calls the free [Jolpica F1 API](https://api.jolpi.ca/ergast/f1/) endpoint `GET /ergast/f1/current/next.json`. DTOs are mapped to a clean domain model in `RaceMapper`. Network, server and data errors are translated into user-friendly messages. |

The starter template structure is kept: `UtilityApp()` (scaffold with bottom navigation), `UtilityScreen()` (core
utility) and `SettingsScreen()` (preferences), all inside a single activity.

## Architecture

```
UI (Compose)                    UtilityApp ─ UtilityScreen ─ SettingsScreen
      │  observes StateFlow          ▲
      ▼                              │ UtilityUiState / UtilitySettings / now
UtilityViewModel  ── shared by both screens (settings change ⇒ main screen updates)
      │
      ▼
RaceRepository (interface) ◄── DefaultRaceRepository
      │                               │
      │                               ▼
      │                        F1Api (Retrofit) ── Jolpica F1 web API
      ▼
domain: computeCountdown(), visibleSessions()  (pure functions, unit-tested)
```

### Project structure

```
app/src/main/java/com/example/cp3406_assessment_younghoocheon/
├── MainActivity.kt, UtilityApplication.kt
├── data/
│   ├── remote/      F1Api.kt, RaceDtos.kt
│   ├── mapper/      RaceMapper.kt
│   ├── model/       Race.kt
│   └── repository/  RaceRepository.kt, DefaultRaceRepository.kt
├── di/              AppContainer.kt
├── domain/          UtilitySettings.kt, SessionSchedule.kt
├── util/            TimeFormatting.kt
└── ui/
    ├── UtilityApp.kt, AppDestination.kt
    ├── utility/     UtilityScreen.kt, UtilityViewModel.kt, UtilityUiState.kt, components/
    ├── settings/    SettingsScreen.kt
    ├── preview/     PreviewData.kt
    └── theme/       Color.kt, Type.kt, Theme.kt
```

## Screenshots

Add your own screenshots in `docs/screenshots/` and reference them here:

| Race screen | Settings screen | Error state |
| --- | --- | --- |
| ![Race](docs/screenshots/race.png) | ![Settings](docs/screenshots/settings.png) | ![Error](docs/screenshots/error.png) |

## Running the app

1. Open the project root folder in Android Studio (Ladybug or newer; JDK 17+ – the bundled JDK is fine).
2. Let Gradle sync finish (an internet connection is needed to download dependencies).
3. Run the `app` configuration on an emulator or device with Android 8.0 (API 26) or higher.
   The device needs internet access to load the schedule.

Run the unit tests with `./gradlew testDebugUnitTest` or right-click `app/src/test` → *Run Tests*.

## Tests

Plain JVM unit tests cover the JSON → DTO → domain mapping (including sprint weekends), the repository error
handling with a fake API, the countdown and session-filter logic and the time formatting.

## Data source

Schedule data comes from the community-run [Jolpica F1 API](https://github.com/jolpica/jolpica-f1)
(Ergast-compatible). The app makes one small request per launch/refresh, well within the API's rate limits.

## AI assistance

Generative AI (Claude, by Anthropic) was used to help build and review this project.
The use is documented in [`docs/AI_DECLARATION.md`](docs/AI_DECLARATION.md); my reflection is in [`docs/REFLECTION.md`](docs/REFLECTION.md).
