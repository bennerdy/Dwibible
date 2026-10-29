# Testing Dwibible on iPhone (iOS)

Because Apple restricts direct `.apk` installations on iOS and standard native iOS development requires a Mac with Xcode and paid Apple Developer signing, we have provided **3 zero-friction methods** to test and run Dwibible directly on your iPhone.

---

## Method 1: Instant Streaming on iPhone (No Setup Required)
You can access the live, fully interactive Android app directly in Safari on your iPhone:
1. Open Safari on your iPhone.
2. Visit your AI Studio live preview link (or tap the **Share** button in the AI Studio header to get the public link).
3. The applet streams live in Safari with touch interaction, split-screen reading, trivia, and verse comparison.

---

## Method 2: Offline Progressive Web App (PWA)
The `/web` folder contains a 100% standalone Progressive Web App with offline caching and mobile-first touch UI designed for iOS Safari.

### Running locally or hosting:
1. You can test it locally by running a quick static server from the project directory:
   ```bash
   npx serve web
   # or
   python3 -m http.server 8080 --directory web
   ```
2. Or deploy `/web` to any free static host (GitHub Pages, Vercel, Netlify, Cloudflare Pages).
3. **On your iPhone**:
   - Open the web URL in **Safari**.
   - Tap the **Share button** (square with arrow pointing up at bottom of Safari).
   - Scroll down and tap **"Add to Home Screen"**.
   - An app icon named **Dwibible** will appear on your iPhone home screen.
   - Tap it to launch full-screen without Safari browser bars, working 100% offline via Service Worker!

---

## Method 3: Expo Go (Native iOS Testing)
The `/expo` folder is an Expo React Native project that runs directly inside the free **Expo Go** app from the Apple App Store without needing Xcode or an Apple Developer account.

### Steps:
1. On your iPhone, download **Expo Go** from the Apple App Store (Free).
2. On your computer (in terminal inside the exported repo):
   ```bash
   cd expo
   npm install
   npx expo start --tunnel
   ```
3. Open your iPhone's **Camera** app (or Expo Go app) and scan the QR code displayed in your terminal.
4. The Dwibible app will load and execute natively on your iPhone!
