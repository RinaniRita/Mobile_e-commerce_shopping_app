## UWE Shopping App

Instructions to open and run the Android app in Android Studio on an API 36.1 emulator (medium phone form factor).

### Prerequisites

- Android Studio (latest stable; bundled JDK is fine)
- Android SDK Platform 36 (Android 16) + Google Play/Google APIs image
- Android SDK Build-Tools for API 36
- Gradle wrapper is included; no manual install needed

### Open the project

1. Start Android Studio.
2. `File` → `Open…` and select the project folder `mobile_shopping_app`.
3. Let Gradle sync finish (internet required to download dependencies).

### Create an emulator (API 36, medium phone)

1. Open **Device Manager** → **Create device**.
2. Choose a medium phone (e.g., Pixel 7/7a class).
3. Select the **Android 16 (API 36)** system image (Google APIs/Play).
4. Finish and ensure the new device appears in the device list.

### Run the app from Android Studio

1. Select the newly created emulator in the device dropdown.
2. Click **Run ▶**. Android Studio will build and install `app` in debug mode.

### Command-line build (optional)

- Build debug APK: `./gradlew assembleDebug`
- Run UI tests on a connected/emulated device: `./gradlew connectedDebugAndroidTest`

### Notes

-`compileSdk`/`targetSdk` are 36; `minSdk` is 33.

- Uses Jetpack Compose and Room; no extra local services required.
- If Gradle sync fails, confirm SDK 36 components are installed and retry.
