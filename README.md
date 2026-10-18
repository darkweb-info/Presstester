# Presstester v1.3 — Android calculator

Changes: new app launcher icon, cleaner dark calculator UI, animated offline `SIX SEVEN!` screen built from CSS instead of the old GIF, improved MP3 effects, optional in-app sound toggle. **Only Easter egg 67 exists.**

Any expression with numeric result exactly 67 triggers when `=` is pressed, e.g. `60+7`, `134/2`, `100-33`. Other results remain normal.

## Important

This ZIP contains **Android source, not a compiled APK**. Android SDK and a build are required. Voice is remastered from the previous synthetic sample; for a naturally spoken recording, replace `app/src/main/assets/six-seven.mp3` with your own licensed recording and rebuild.

## GitHub upload / APK build

Upload all files and folders in the package root to the existing repository, including hidden `.github`. It is best to remove the old duplicated files from the repository root (older calculator.html, calculator.css, calculator.js, GIF/MP3) so only assets in `app/src/main/assets/` remain. Keep your Git history.

Commit changes to main. Open GitHub **Actions > Build Presstester APK**, choose the latest successful run, download the `Presstester` artifact and unzip to `Presstester.apk`.

The app works offline and needs no Android runtime permissions. A debug APK is for personal testing, not for production Play Store submission. Android package ID: `com.presstester.app`.

**Java build fix:** the Activity helper was renamed from `setImmersive(boolean)` to `updateSecretFullscreen(boolean)` to avoid overriding a public Android method with private visibility.
