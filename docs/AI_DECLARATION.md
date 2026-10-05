# Declaration of AI-Generated Material

**Course:** CP3406 / CP5307 – Assessment 1 (Utility App)
**Project:** F1 Race Countdown
**Student:** Younghoocheon  <!-- add student ID -->

## AI tool used

- Claude (Anthropic), via the claude.ai chat interface.

## How the AI was used

| Area | Use of AI | What I did myself |
| --- | --- | --- |
| Project set-up | Suggested fixing the Gradle version catalog (Kotlin 2.0.x with the Compose compiler plugin), SDK levels and launcher icons. | Applied and checked the changes in Android Studio. |
| Architecture | Proposed the layers Compose UI → ViewModel → Repository → Retrofit, a manual DI container and the package structure. | <!-- describe your decisions --> |
| Code | Generated and reviewed Kotlin for the data layer (`F1Api`, DTOs, `RaceMapper`, repository), domain logic (countdown, session filtering), ViewModel, Compose screens/components and theme. | <!-- describe what you wrote, edited or rewrote --> |
| Tests | Generated JVM unit tests for parsing, mapping, repository error handling, countdown logic and time formatting. | <!-- describe running / extending the tests --> |
| Documentation | Drafted the README and the structure of this declaration and the reflection. | <!-- describe your edits --> |

## Prompts

Main prompts used (summarised):

1. Convert my web-based project into a native Kotlin/Jetpack Compose utility app that follows the assignment brief.
2. Review my project against the assessment requirements and list what still needs to change.
3. Fix the remaining items and produce the final project.

<!-- Attach or link the full chat history if the course requires it. -->

## Verification

I ran the app and the unit tests myself and checked that the behaviour matches the assignment requirements:
<!-- tick only what you really did -->
- [ ] App runs on an emulator / device
- [ ] `./gradlew testDebugUnitTest` passes
- [ ] I read and understand all submitted code

## Statement

I understand the AI-generated material listed above and take responsibility for the submitted work.

Signed: ____________________   Date: ____________
