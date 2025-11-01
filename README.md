# 📘 FocusTune — Developer README
## 🧠 Overview

FocusTune is a **Kotlin Multiplatform (KMP)** productivity app (Pomodoro + focus timer) targeting
**Android & iOS**.

This document explains project structure, development setup, and build/run steps for contributors.

## 📂 Project Structure
```
FocusTune/
 ├─ composeApp/           # Shared Kotlin UI + Logic (Compose Multiplatform)
 │   ├─ src/commonMain/   # Shared business logic & UI
 │   ├─ src/androidMain/  # Android-specific code
 │   └─ src/iosMain/      # iOS-specific code
 │
 ├─ iosApp/               # iOS entry point (Xcode project)
 │   └─ iosApp/
 │
 ├─ Dockerfile            # Dev environment setup (JDK, SDK, etc.)
 ├─ docker-compose.yml
 └─ README.md             # Dev README (this file)
```

## 🛠 Development Environment
| Tool           | Version / Notes                |
| -------------- | ------------------------------ |
| macOS          | Apple Silicon recommended      |
| Android Studio | Latest stable                  |
| Xcode          | Latest                         |
| Docker         | Used for CLI builds (optional) |
| Kotlin         | via SDKMAN                     |
| Gradle         | Wrapper included               |

## 🚀 Setup
### Clone the Repository
```
git clone <repository-url>
cd FocusTune
```

### Install Dependencies (local machine)
#### Android
```
./gradlew :composeApp:androidDependencies
```

#### iOS
Open Xcode project:
```
iosApp/iosApp.xcodeproj
```

Make sure Xcode CLI tools installed:
```
xcode-select --install
```

## 🐳 (Optional) Docker Dev Container

Start container:
```
docker compose up -d
docker exec -it focustune-dev bash
```

Android Studio is used locally — Docker is for consistent CLI builds.

## ▶️ Build & Run

### Android
#### IDE
Run from Android Studio (recommended)

#### CLI build
```
./gradlew :composeApp:assembleDebug
```

Install APK:
```
adb install composeApp/build/outputs/apk/debug/*.apk
```

### iOS
#### IDE (recommended)
Open in Xcode and run the app:
```
iosApp/iosApp.xcodeproj
```

#### CLI build
```
cd iosApp
xcodebuild
```

## 🧪 Testing
```
./gradlew test
```

## 📦 Tech Stack
| Layer        | Tech                    |
| ------------ | ----------------------- |
| UI           | Compose Multiplatform   |
| Architecture | MVVM + StateFlow        |
| Networking   | TBD (Ktor candidate)    |
| Local DB     | TBD (SQLDelight likely) |
| DI           | TBD (Koin or other)     |

- Write shared logic first (commonMain)
- Platform code only when necessary (e.g., permissions)
- Commit readable code & comments
- Follow Git branching rules below

## 🌿 Git Branch Workflow
| Branch      | Purpose                    |
| ----------- | -------------------------- |
| `main`      | Stable production code     |
| `dev`       | Integrate development work |
| `feature/*` | Individual tasks/features  |

## Workflow:
1. Create `feature/<name>`
2. Develop & test
3. PR → Review → Merge to `dev`

## 🎯 Roadmap
- [] Core Pomodoro timer logic
- [] Shared ViewModel setup
- [] Android UI screens
- [] iOS SwiftUI entry + Compose integration
- [] Local storage
- [] Notifications (focus complete)
- [] Analytics / future monetization

## 👥 Team Notes
- Keep README updated
- Document major architectural decisions
- Prefer shared KMP code first

## ✅ Done — You can paste this into your repo 🎉