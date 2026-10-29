# Komorebi - Premium Manga & Book Reader

> **Komorebi** (木漏れ日) - "Sunlight filtering through trees" - A premium, modern manga and book reader for Android with liquid/glass morphism design.

## ✨ Features

- **📚 Multi-format Support**: CBZ, CBR, PDF, EPUB, ZIP, RAR
- **🌐 Online Sources**: MangaDex, MANGA Plus, WEBTOON, Komga, Kavita + extensible source system
- **🎨 Premium Liquid/Glass UI**: Custom design system with glass morphism, liquid animations, organic shapes
- **📖 Immersive Reader**: Vertical (webtoon), RTL (manga), LTR (comics) reading modes
- **☁️ Sync & Backup**: Cloud synchronization, auto-backup, cross-device progress
- **🔍 Smart Library**: Auto-scan, series grouping, progress tracking, favorites, tags
- **🎯 Modern Stack**: Kotlin, Jetpack Compose, Material 3 Expressive, Room, Hilt, Flow

## 🛠 Tech Stack

| Layer | Technology |
|-------|------------|
| **Language** | Kotlin 2.0.20 |
| **UI** | Jetpack Compose (Material 3 Expressive) |
| **Architecture** | MVVM + Clean Architecture |
| **DI** | Hilt (Dagger) |
| **Database** | Room + Kotlinx Serialization |
| **Preferences** | DataStore Preferences |
| **Network** | Retrofit + OkHttp + Coroutines |
| **Images** | Coil 3 |
| **Navigation** | Navigation Compose |
| **Async** | Kotlinx Coroutines + Flow |
| **Build** | Gradle 8.7 (Kotlin DSL) |

## 📱 Screens

1. **Library** - Grid/List view, filters, search, progress tracking
2. **Reader** - Immersive reading with gestures, themes, zoom, bookmarks
3. **Explore** - Trending, new releases, recommendations, genres, sources
4. **Settings** - Appearance, reading, library, sources, sync, data, about

## 🚀 Getting Started

### Prerequisites

- Android Studio Ladybug (2024.2.1) or later
- JDK 17+
- Android SDK 35
- Minimum SDK 26 (Android 8.0)

### Build

```bash
# Clone the repository
git clone https://github.com/yourusername/komorebi.git
cd komorebi

# Build debug APK
./gradlew assembleDebug

# Build release APK (requires signing config)
./gradlew assembleRelease
```

### Run Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

## 📁 Project Structure

```
komorebi/
├── app/
│   ├── src/main/
│   │   ├── java/com/komorebi/reader/
│   │   │   ├── data/
│   │   │   │   ├── local/          # Room database, DAOs, entities
│   │   │   │   ├── preferences/    # DataStore preferences
│   │   │   │   └── repository/     # Repository layer
│   │   │   ├── di/                 # Hilt modules
│   │   │   ├── ui/
│   │   │   │   ├── components/     # Glass/Liquid components
│   │   │   │   ├── navigation/     # Navigation graph
│   │   │   │   ├── screens/        # Screen composables + ViewModels
│   │   │   │   └── theme/          # Design system (colors, typography, shapes)
│   │   │   ├── util/               # Extensions, file utils
│   │   │   ├── KomorebiApplication.kt
│   │   │   └── MainActivity.kt
│   │   ├── res/
│   │   │   ├── values/             # Strings, themes
│   │   │   └── xml/                # Backup rules, file paths, etc.
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## 🎨 Design System

The app features a custom **liquid/glass morphism** design system:

- **Colors**: Deep forest base with emerald/gold accents, semantic glass surfaces
- **Typography**: Japanese editorial-inspired scale, reading-optimized styles
- **Shapes**: Organic asymmetric corners (droplet, puddle, blob)
- **Motion**: Spring-based liquid animations, shimmer effects
- **Components**: GlassSurface, LiquidButton, LiquidFAB, GlassCard, LiquidChip, LiquidProgress, ShimmerSkeleton

## 🔧 Configuration

### Signing (Release)

Create `local.properties` or set environment variables:

```properties
# local.properties
storeFile=../keystore.jks
storePassword=your_store_password
keyAlias=your_key_alias
keyPassword=your_key_password
```

### API Keys

For online sources, configure in `local.properties`:

```properties
mangadex_api_key=your_key
```

## 📦 Dependencies

Key versions (from `gradle.properties`):
- Kotlin: 2.0.20
- Compose BOM: 2024.10.00
- Room: 2.6.1
- Hilt: 2.51.1
- Coil: 2.7.0
- Navigation: 2.8.2

## 🧪 Testing

- Unit tests: JUnit 4, Mockito-Kotlin, Turbine (Flow testing)
- UI tests: Compose Test Rules, Espresso
- Test coverage: `./gradlew jacocoTestReport`

## 📄 License

```
MIT License

Copyright (c) 2024 Komorebi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software...
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests and lint
5. Submit a PR

## 🙏 Acknowledgments

- **Tachiyomi** - Inspiration for source extensibility
- **Komikku** - UI/UX reference
- **Material 3 Expressive** - Design foundation
- **Jetpack Compose** - Modern UI toolkit

---

**Built with ❤️ for manga and book lovers**