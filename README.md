# PRESSTESTER — Android Edition (v1.2)

Offline Android calculator with one built-in SIX SEVEN (67) easter egg.

## How the 67 secret works

Whenever a valid calculation produces the numeric result **67**, pressing `=` **immediately replaces the calculator with a fullscreen animated SIX SEVEN screen** and automatically plays a loud, normalized, offline English "six seven" voice.

Examples: `67 =`, `60+7 =`, `70-3 =`, `134/2 =`, `6.7*10 =`, `(50+17) =`, `201/3 =`.

No other secret codes have been added.

The entire effect works offline and is embedded in the Android app: GIF (32 frames), MP3, CSS animation, and calculator code. On Android, the secret screen also temporarily hides system bars; Android Back returns to the normal calculator. It will never raise your phone's own media-volume setting or request Internet access. Sound is set to maximum app volume and the audio file has been loudness-normalized.

## Build a real APK with GitHub Actions

This archive contains **Android source code, not a compiled APK**.

1. Extract the archive.
2. Copy all files *including the hidden `.github` directory* to your `Presstester` GitHub repository (or create a new repository).
3. Commit changes to the `main` or `master` branch.
4. Visit GitHub > **Actions > Build Presstester APK**, open the latest successful run.
5. Download **Presstester**, unzip it, and install `Presstester.apk` on your Android phone.

Alternatively, open the project directory in Android Studio and use Build > Build APK(s).

## Preview without Android

Open `calculator.html` locally in a modern desktop browser (the GIF/audio assets are in the same folder). The calculator and easter egg will work, but native Android fullscreen mode is only available in the installed APK.

## Files

- `app/src/main/assets/calculator.html`, `.js`, `.css`: offline calculator
- `app/src/main/assets/six-seven.gif`: animated celebration
- `app/src/main/assets/six-seven.mp3`: normalized voice recording
- `app/src/main/java/com/presstester/app/MainActivity.java`: Android WebView and fullscreen integration
- `.github/workflows/build-apk.yml`: GitHub Actions APK build

The debug APK is signed with a development key suitable for personal testing, not a production Play Store release. This app requests no Android permissions.
