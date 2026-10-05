# Self-Reflection – F1 Race Countdown (Gibbs' Reflective Cycle)

> **Template only – write this in your own words.** The brief requires a 1–2 page PDF of 500–1000 words
> and marks it on insight, understanding of Android coding and *meaningful personal experiences*.
> Replace every prompt below with your own experience, delete this note, check the word count,
> and export the result to PDF for the LearnJCU dropbox.

## 1. Description – What happened? (~100–150 words)

*Which app did you choose and why? How did you go from idea to the finished app? Mention real events,
e.g. a first attempt that did not fit the brief and had to be rebuilt, fixing the Gradle/Kotlin setup,
adding Retrofit, writing tests.*

Facts about the final app you can build on:
- Main screen: next race, live countdown, weekend timetable. Settings screen: time zone, 12/24-hour clock, countdown target, visible sessions.
- Compose UI → shared `UtilityViewModel` → `RaceRepository` → Retrofit `F1Api` (Jolpica API), manual DI via `AppContainer`.

## 2. Feelings – What were you thinking and feeling? (~100–150 words)

*Be honest: confident, frustrated, confused, proud? When did each feeling appear and why?*

## 3. Evaluation – What went well and what did not? (~150–200 words)

*Give concrete examples for both. Ideas to check against your own experience: state hoisting and reusable composables,
sharing one ViewModel across both screens, error/loading states, unit tests with a fake API, build/setup problems,
time spent, how you used AI and where its output needed fixing.*

## 4. Analysis – Why did things go well or badly? (~150–200 words)

*Explain causes and link them to course concepts (Weeks 1–5): Kotlin/Android Studio, Compose layouts,
Material Design 3, ViewModel/DI/Repository, Retrofit. For example: why does a Repository plus DTO mapping make
the UI simpler to test? Why must `viewModel()` be created above both screens?*

## 5. Conclusion – What did you learn? (~100 words)

*Technical skills and personal lessons (planning, reading the brief early, version control habits, AI use).*

## 6. Action Plan – What would you do differently? (~100 words)

*Specific next steps, e.g. commit small and often from day one, read the marking rubric first,
persist settings with DataStore, add an offline cache, test on more devices.*

---
Word count target: 500–1000 words in total (headings and this note do not count).
