# 🔍 HCS Test GitHub User Search App

<p align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white" />
</p>

A modern Android application for submission interview test in HCS Recruitment process that allows users to search for GitHub users, view search results, and explore detailed user profiles. Built with Clean Architecture principles and the latest Android development best practices.

## ✨ Features

### Core Features
- 🔎 **Real-time Search** - Search GitHub users with debounced input for optimal performance
- 📋 **User List** - Display search results in a beautiful, scrollable list
- 👤 **User Details** - View comprehensive user profiles including:
  - Profile information (avatar, username, bio)
  - Statistics (repositories, followers, following)
  - Contact information (company, location, email, blog)
  - Direct link to GitHub profile
- 💾 **Offline Support** - Cache user data locally using Room database
- 🎨 **Modern UI** - Clean, intuitive interface following Material Design guidelines

### Technical Features
- ⚡ **Asynchronous Operations** - Smooth user experience with Kotlin Coroutines
- 🏗️ **Clean Architecture** - Separation of concerns with Domain, Data, and Presentation layers
- 💉 **Dependency Injection** - Managed with Hilt for better testability
- 🧪 **Unit Testing** - Comprehensive test coverage
- 🐛 **Network Debugging** - Integrated Chucker for API inspection
- 📦 **Version Catalog** - Centralized dependency management

## 📸 Screenshots

<div align="center" style="display:flex; justify-content:center; gap:10px;">

  <img src="https://github.com/user-attachments/assets/f36ba790-0068-40ed-b784-6c85705ef977" width="220"/>

  <img src="https://github.com/user-attachments/assets/5dec01cb-4ac3-4eb3-ab9a-7da4e846f1fa" width="220"/>

  <img src="https://github.com/user-attachments/assets/205a2e43-30af-42ff-bb3f-7fb4b532ad9a" width="220"/>

</div>

## 🏛️ Architecture

This project follows **Clean Architecture** principles with **MVVM** pattern:
```
presentation/     → UI Layer (Activities, ViewModels, Adapters)
│
domain/          → Business Logic Layer (Use Cases, Models, Repository Interfaces)
│
data/            → Data Layer (Repository Implementation, API, Database)
    ├── remote/  → Network (Retrofit, DTOs)
    ├── local/   → Persistence (Room, DAOs, Entities)
    └── mapper/  → Data Transformers
```

### Architecture Diagram
```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                    │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │   Activity   │  │  ViewModel   │  │   Adapter    │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                     Domain Layer                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  Use Cases   │  │    Models    │  │  Repository  │  │
│  │              │  │              │  │  Interface   │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                      Data Layer                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  Repository  │  │ API Service  │  │     Room     │  │
│  │     Impl     │  │  (Retrofit)  │  │   Database   │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### Why This Architecture?

**Clean Architecture** was chosen for several key benefits:

1. **Separation of Concerns** - Each layer has a specific responsibility
2. **Testability** - Easy to write unit tests for each layer independently
3. **Scalability** - Simple to add new features without affecting existing code
4. **Maintainability** - Clear structure makes code easier to understand and modify
5. **Independence** - Domain layer is independent of frameworks and UI

## 🛠️ Tech Stack

### Core
- **Kotlin** - Modern programming language for Android
- **Android SDK** - Target SDK 34, Min SDK 24

### Architecture Components
- **ViewModel** - Store and manage UI-related data
- **LiveData** - Observable data holder for lifecycle-aware components
- **Room** - Local database for data persistence

### Dependency Injection
- **Hilt** - Simplified dependency injection for Android

### Networking
- **Retrofit** - Type-safe HTTP client
- **OkHttp** - HTTP client with logging interceptor
- **Moshi** - Modern JSON library for Kotlin

### Asynchronous
- **Kotlin Coroutines** - Asynchronous programming
- **Flow** - Reactive streams for data emission

### Image Loading
- **Glide** - Fast and efficient image loading library

### Debugging
- **Chucker** - HTTP inspector for OkHttp

### Testing
- **JUnit** - Unit testing framework
- **MockK** - Mocking library for Kotlin
- **Coroutines Test** - Testing utilities for coroutines
- **Architecture Components Testing** - Testing LiveData and ViewModel

### Build Configuration
- **Version Catalog** - Centralized dependency version management

## 📋 Prerequisites

Before running this project, make sure you have:

- Android Studio Hedgehog | 2023.1.1 or newer
- JDK 17 or newer
- Android SDK with minimum API level 24
- Gradle 8.2 or newer

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/github-user-search.git
cd github-user-search
```

### 2. Open in Android Studio

1. Open Android Studio
2. Select **File → Open**
3. Navigate to the cloned project directory
4. Click **OK**

### 3. Sync Project

Android Studio will automatically sync Gradle. If not:
- Click **File → Sync Project with Gradle Files**

### 4. Run the App

1. Connect an Android device or start an emulator
2. Click **Run** button or press `Shift + F10`
3. Select your device/emulator
4. Wait for the app to build and install

## 🧪 Running Tests

### Run All Tests
```bash
./gradlew test
```

### Run Specific Test Class
```bash
./gradlew test --tests SearchViewModelTest
```

### Run Tests with Coverage
```bash
./gradlew testDebugUnitTest jacocoTestReport
```

## 📦 Project Structure
```
app/
├── src/
│   ├── main/
│   │   ├── java/com/yourcompany/githubusersearch/
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   ├── dao/
│   │   │   │   │   ├── database/
│   │   │   │   │   └── entity/
│   │   │   │   ├── remote/
│   │   │   │   │   ├── api/
│   │   │   │   │   └── dto/
│   │   │   │   ├── mapper/
│   │   │   │   └── repository/
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   └── usecase/
│   │   │   ├── presentation/
│   │   │   │   ├── search/
│   │   │   │   │   ├── adapter/
│   │   │   │   │   ├── SearchActivity.kt
│   │   │   │   │   ├── SearchViewModel.kt
│   │   │   │   │   └── SearchUiState.kt
│   │   │   │   └── detail/
│   │   │   │       ├── UserDetailActivity.kt
│   │   │   │       ├── UserDetailViewModel.kt
│   │   │   │       └── UserDetailUiState.kt
│   │   │   ├── di/
│   │   │   └── GitHubSearchApplication.kt
│   │   └── res/
│   │       ├── layout/
│   │       ├── drawable/
│   │       ├── values/
│   │       └── ...
│   └── test/
│       └── java/com/yourcompany/githubusersearch/
│           └── presentation/
│               ├── search/
│               └── detail/
├── gradle/
│   └── libs.versions.toml
└── build.gradle.kts
```

## 🔑 Key Components

### Use Cases

- **SearchUsersUseCase** - Search for GitHub users by username
- **GetUserDetailUseCase** - Fetch detailed information for a specific user
- **GetCachedUsersUseCase** - Retrieve cached users from local database

### ViewModels

- **SearchViewModel** - Manages search state and user list
- **UserDetailViewModel** - Manages user detail state

### Repository

- **GitHubRepositoryImpl** - Implements data fetching logic with caching strategy

## 🎯 API Integration

This app uses the official **GitHub REST API v3**:

- **Search Users**: `GET https://api.github.com/search/users?q={query}`
- **Get User Details**: `GET https://api.github.com/users/{username}`

No authentication required for basic usage (60 requests/hour limit).

## 🐛 Debugging with Chucker

Chucker is integrated for network debugging:

1. Run the app in debug mode
2. Perform API calls
3. Open Chucker notification
4. View request/response details


## 📝 Code Style

This project follows:
- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)

## 🔮 Future Enhancements

Potential features for future versions:

- [ ] Favorite/Bookmark users
- [ ] View user repositories
- [ ] Search filters (type, location, etc.)
- [ ] Dark/Light theme toggle
- [ ] Pagination for search results
- [ ] Share user profile
- [ ] Search history


## 👤 Author

**[Your Name]**

- Email: ziadsyahrulm@gmail.com
- GitHub: [@ziadsyahrul](https://github.com/ziadsyahrul)
- LinkedIn: [Ziad Syahrul](https://www.linkedin.com/in/ziad-syahrul-63b4a7176/)

## 🙏 Acknowledgments

- [GitHub API](https://docs.github.com/en/rest) - For providing the user data
- [Material Design](https://material.io/) - For design guidelines
- Android Community - For amazing libraries and resources

---

<p align="center">Made with ❤️ for HCS Android Assessment</p>
