# Middleware Playground

## Overview

Middleware Playground is a multiplatform application developed with Kotlin Multiplatform (KMP) that enables you to map, transform, and customize API responses to fit your application's needs.

Integrating with the route mapping middleware, the app provides an intuitive graphical interface to create transformation rules, preview changes, and manage your mapping configurations, all with a consistent user experience on both Android and iOS.

## Official Documentation and Resources

* [Project Middleware Documentation](https://leonardos-organization-15.gitbook.io/projectmiddleware-public-docs)
* [Middleware Backend Repository](https://github.com/LeonardoBai12/ProjectMiddleware)
* [Authentication Service Repository](https://github.com/LeonardoBai12/MIddlewareUserService)
* [Postman Documentation](https://documenter.getpostman.com/view/28162587/2sAXjRX9p1#intro)

## Key Features

* **Multiplatform Interface**: Developed using Kotlin Multiplatform, providing a native experience for both Android (Jetpack Compose) and iOS (SwiftUI).

* **Route Management**: View, create, and organize your mapped routes with an intuitive interface.

* **Intuitive Mapping Editor**: Step-by-step interface for creating mappings, allowing:
  * Simple field mapping definition
  * Grouping related fields
  * Value concatenation
  * Transformation of complex JSON structures

* **Real-time Preview**: Test your mapping configurations before permanently creating them.

* **Profile Management**: Keep all your configurations secure with an integrated authentication system.

## Technologies Used

* **Kotlin Multiplatform**: Foundation for code sharing between platforms
* **Jetpack Compose**: Declarative UI framework for Android
* **SwiftUI**: Declarative UI framework for iOS
* **Ktor Client**: For multiplatform HTTP communication
* **SQLDelight**: Multiplatform local data persistence
* **Kotlin Coroutines Flow**: State management and asynchronous communication

## Architecture

The application was developed following Clean Architecture and SOLID principles:

* **Separation of Concerns**: Well-defined layers for domain, data, and presentation
* **State Pattern for UI**: Event-based interface state management
* **Factory Pattern**: To abstract platform-specific implementations
* **Adapter Pattern**: For sharing ViewModels between platforms
* **Dependency Inversion**: To ensure business code doesn't depend on external frameworks

## How to Use

### Getting Started

1. **Sign Up and Login**: Create your account or log in to access your configurations
2. **Explore APIs**: View already configured routes or add a new one

### Creating a New Mapping

1. **Define Original Route**: Configure the source API, specifying HTTP method, URL and path
2. **Map Fields**: Define how each field in the original response should be transformed
3. **Configure Parameters**: Add custom headers or other necessary parameters
4. **Preview Test**: View the transformation result before confirming
5. **Create Route**: Finalize and create your custom mapped route

### Demo videos:

#### Android

https://github.com/user-attachments/assets/b4f926a1-99f6-4684-b5b4-1aa542d47c2f

#### iOS

https://github.com/user-attachments/assets/b0de6527-e056-4ede-93ae-c2124c84f324
