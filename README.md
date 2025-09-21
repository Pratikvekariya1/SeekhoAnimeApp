# SeekhoAnimeApp
Project Overview
Seekho Anime App is a modern Android application built for the Seekho edutainment platform that allows users to discover, explore, and watch trailers of popular anime series. The app demonstrates clean architecture principles, modern Android development practices, and robust offline capabilities.

Objective
Build a simple Android app that uses the Jikan API to fetch and display a list of anime series, allowing users to view details and trailers while maintaining excellent user experience both online and offline.

**_Features Implemented_**
**Core Features**
Anime List Page: Displays top-rated anime with title, episode count, rating, and poster images

Anime Detail Page: Shows comprehensive information including synopsis, genres, cast, and video trailer

Video Player Integration:

Smart URL detection (YouTube vs Direct video URLs)

YouTube IFrame Player for YouTube trailers

ExoPlayer for direct video streams

External app launch options

Offline Support: Full Room database integration with local caching

Favorites System: Mark and manage favorite anime with local persistence

Search Functionality: Search anime by title with real-time results

Network State Management: Real-time connectivity monitoring with appropriate UI feedback

**Architecture & Technical Features**
MVVM Architecture Pattern: Clean separation of concerns with ViewModels, LiveData, and Data Binding

Dependency Injection: Hilt/Dagger implementation for loose coupling and testability

Clean Architecture: Repository pattern with distinct data, domain, and presentation layers

Use Cases/Interactors: Business logic encapsulation for maintainability

Reactive Programming: LiveData and StateFlow for reactive UI updates

Error Handling: Comprehensive error management across all layers

Memory Management: Proper lifecycle-aware components and resource cleanup

**Best-in-Class Libraries Integration**
Retrofit: RESTful API communication with OkHttp interceptors

Glide: Efficient image loading with caching and transformations

Room: Local database with type converters and migrations support

Media3 ExoPlayer: Modern video playback with custom controls

YouTube IFrame Player: Official YouTube integration for trailer playback

Navigation Component: Type-safe navigation with SafeArgs

ViewBinding: Type-safe view references replacing findViewById

**User Experience Enhancements**
Swipe-to-Refresh: Pull to refresh anime list with loading indicators

Network Awareness: Data usage warnings on mobile connections

Graceful Degradation: Offline mode with cached data access

Loading States: Skeleton screens and progress indicators

Error Recovery: Retry mechanisms with user-friendly error messages

Material Design: Modern UI following Material Design 3 guidelines

**Advanced Features**
Feature Flag System: Runtime UI flexibility for legal compliance scenarios

Adaptive Layouts: Dynamic layout adjustments based on feature availability

Network Type Detection: WiFi vs Mobile data awareness

Video Player Manager: Unified video playback with multiple source support

Image Fallback System: Alternative content when images are disabled

Constraint Groups: Flexible UI components for design constraint handling

**Architecture & Design Patterns**
MVVM + Clean Architecture
text
Presentation Layer (UI)
↓
Domain Layer (Business Logic)
↓
Data Layer (Repository + Data Sources)
Dependency Injection Structure
DatabaseModule: Room database and DAO providers

NetworkModule: Retrofit, OkHttp, and API service configuration

RepositoryModule: Repository interface bindings

UseCaseModule: Business logic use case providers

ContextModule: Application context provider

Data Flow
UI triggers action via ViewModel

ViewModel calls Use Case

Use Case executes business logic via Repository

Repository fetches from Network/Database

Result flows back through reactive streams (LiveData/StateFlow)

**Installation & Setup**
Prerequisites
Android Studio Arctic Fox or newer

Android SDK 24+ (Android 7.0)

Kotlin 1.9.10+

Gradle 8.2+

Getting Started
Clone the repository:

bash
git clone **https://github.com/yourusername/seekho-anime-app.git**
cd SeekhoAnimeApp
Open project in Android Studio

Sync Gradle files and dependencies

Build and run the application:

bash
./gradlew assembleDebug
API Configuration
The app uses the public Jikan API (v4) - no API key required:

Base URL: https://api.jikan.moe/v4/

Rate Limit: 2 requests/second, 30 requests/minute

Documentation: https://docs.api.jikan.moe

**Assumptions Made**
API & Data Assumptions
Jikan API Stability: Assumed the Jikan API v4 remains stable and publicly accessible

YouTube Trailer Format: Assumed trailer URLs from Jikan API are standard YouTube URLs or embed links

Image Availability: Assumed anime poster images are consistently available from the API

Data Structure: Assumed API response structure remains consistent with current documentation

Rate Limiting: Implemented with assumption of 2 requests/second limit as documented

User Behavior Assumptions
Internet Connectivity: Users may have intermittent internet connections requiring offline support

Mobile Data Concerns: Users on mobile data prefer warnings about video streaming

Viewing Preferences: Users want both in-app and external video viewing options

Content Discovery: Users prefer visual browsing with images over text-only listings

Favorite Management: Users want persistent favorite anime across app sessions

Technical Assumptions
Android Version Support: Targeting Android 7.0+ covers majority of active devices

Device Capabilities: Assumed devices can handle video playback and image loading

Storage Space: Local database caching won't exceed reasonable storage limits

Performance: RecyclerView with image loading performs adequately on target devices

Network Types: WiFi and cellular are primary connection types requiring different handling

Legal & Compliance Assumptions
Image Rights: Anime poster images from Jikan API are legally viewable

Content Restrictions: No age-restricted content filtering required for demo app

YouTube Terms: YouTube IFrame Player usage complies with their terms of service

Data Privacy: No personal data collection beyond local app preferences

Regional Restrictions: App doesn't need geo-blocking or regional content restrictions

**Known Limitations**
API-Related Limitations
Rate Limiting: Jikan API rate limits may cause delays during heavy usage

Server Dependencies: App functionality depends on Jikan API server availability

Data Completeness: Some anime entries may have missing synopsis, genres, or trailer data

Image Quality: Poster image quality varies and some may be low resolution

Search Accuracy: Search functionality limited by Jikan API search capabilities

Video Playback Limitations
YouTube Restrictions: Some YouTube videos may be region-locked or age-restricted

Direct Video URLs: Limited support for non-standard video streaming formats

Network Requirements: Video streaming requires stable internet connection

Mobile Data Usage: Video playback can consume significant mobile data

ExoPlayer Compatibility: Some video formats may not be supported on all devices

Offline Mode Limitations
Initial Data Requirement: App requires initial internet connection to populate cache

Stale Data: Cached data may become outdated without internet connectivity

Search Limitations: Search functionality unavailable in offline mode

Video Playback: No offline video playback support

Image Caching: Limited image cache size may cause images to reload

UI/UX Limitations
Tablet Optimization: UI primarily optimized for phone form factors

Landscape Mode: Limited landscape orientation optimization

Accessibility: Basic accessibility support, could be enhanced for better screen reader support

Dark Theme: Partial dark theme implementation

Localization: Currently supports English only

Performance Limitations
Large Lists: Performance may degrade with very large anime lists (1000+ items)

Image Loading: Multiple simultaneous image loads may cause memory pressure

Database Queries: Complex search queries may be slow on older devices

Cold Start: Initial app launch may be slow due to Hilt initialization

Memory Usage: Video player instances may consume significant memory

Feature Limitations
User Accounts: No user authentication or cloud sync capabilities

Social Features: No sharing, reviews, or social interaction features

Notifications: No push notifications for new episodes or updates

Advanced Search: Limited search filters (genre, year, rating, etc.)

Content Management: No user-generated content or personal lists beyond favorites

Technical Debt & Future Improvements
Testing Coverage: Limited unit and integration test coverage

Error Analytics: No crash reporting or analytics integration

Performance Monitoring: No performance metrics collection

Database Migrations: Limited Room database migration strategy

Configuration Management: No remote configuration for feature toggles

**Technologies Used**
Core Android
Kotlin: Primary programming language

Android SDK: Target SDK 36, Min SDK 24

Jetpack Components: ViewModel, LiveData, Navigation, Room

Architecture & DI
Hilt: Dependency injection framework

MVVM: Architectural pattern implementation

Repository Pattern: Data access abstraction

Use Cases: Business logic encapsulation

Networking & Data
Retrofit: HTTP client for API communication

OkHttp: Network interceptor and logging

Gson: JSON serialization/deserialization

Room: Local database with SQLite

Media & UI
Media3 ExoPlayer: Video playback engine

YouTube IFrame Player: YouTube video integration

Glide: Image loading and caching

Material Design Components: UI components

ViewBinding: Type-safe view references

**Project Statistics**
Development Time: ~40 hours (includes bonus features)

Lines of Code: ~3,500+ lines of Kotlin

Architecture Layers: 3 (Presentation, Domain, Data)

Database Tables: 1 (with room for expansion)

API Endpoints: 3 (top anime, details, search)

Feature Flags: 4 (images, profiles, video, ratings)

Screen Orientations: Portrait (primary), Landscape (partial)

**Support & Contact**
For technical questions, bug reports, or feature requests, please contact:

Developer: Pratik Vekariya

Email: vekariyapratik5598@gmail.com

GitHub: https://github.com/Pratikvekariya1

LinkedIn: https://www.linkedin.com/in/pratik-vekariya-919681141/

📄 License
This project is developed as an assessment for Seekho and follows standard open-source practices. All anime data is sourced from the public Jikan API and MyAnimeList database.

