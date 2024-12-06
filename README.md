# Colour Spotter App

## Overview

Colour Spotter is an Android application that allows users to capture and identify colors from images using the device's camera. 
The app is based on [Android developers architecture](https://developer.android.com/jetpack/guide?gclsrc=ds&gclsrc=ds)
developed with [Jetpack Compose](https://developer.android.com/jetpack/compose).

## Features

* **Color Capture:** Capture colors from images using the device's camera.
* **Color Identification:** Identify the dominant color
* **Database Integration:** Store and retrieve color palettes using a local database (Room).
* **Dynamic Updates:** The color list automatically updates with database changes.
* **MVVM Architecture:** Follows the Model-View-ViewModel architectural pattern for clean code organization.
* **Jetpack Compose:** Built with Jetpack Compose for a modern and declarative UI.
* **Material Design 3:** Adheres to Material Design 3 guidelines for a consistent and user-friendly experience.

## Getting Started
To run the app locally, follow these steps:

1. Clone this repository.
2. Open the project in Android Studio
3. Select build variant *devDebug* in *app* module
4. Build and run the app on an Android emulator or device.

**Requirements:**

I used Android Studio Ladybug | 2024.2.1 Patch 3
I have used a emulator "Pixel 7" to reference with Android api level 35 
I used a real Pixel 7 with Android 15 api level 35 and a Galaxy A21s with Android 12 api level 32


## Architecture and library used
The app follow this components:
- Project use **MVVM** architecture and Clean architecture;
- divided project in sub-module;
- used Jetpack **Compose** and **Material Design 3**;
- **Retrofit** to request network;
- **Room** to manage database;
- **Flow** to manage change state of data;
- **Hilt/Dagger** for dependency injection;
- **Mockk** to mock data on unit test

## Modularazition
The app follows an implementation of mudularazition by features:
- *app* : main module to run app
- *data* : it contains 'data' layer such as datasource
- *domain* : it contains domain layer such as repository, usecase
- *common* : contains UI models class and utils for general purpose
- *database* : contains utils to manage db, models database and dao;
- *network* : it implements manage network, network models
- *ui* : it contains app theme and custom view;
- *feature/spotter* : 
- *feature/list* : implements list color and owr viewmodel;
- *camera* : implements camera and logic to quantize color

This the graph of dependencies:

<img width="371" alt="Colour Spotter" src="https://github.com/LorenzoNiero/colour-spotter-app/blob/develop/gradle/dependency-graph/project.dot.png">

## Testing

The project includes unit tests for the network, domain, and data modules. 
You can find them in the respective module directories.

## Screenshots

TODO

### Todo
- enable obfuscation code with R8
- implement more unit test. For example in ViewModel