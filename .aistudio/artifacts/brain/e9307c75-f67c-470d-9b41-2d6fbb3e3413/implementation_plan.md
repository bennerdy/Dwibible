# Dwibible — Offline Bilingual (BM & English KJV/NKJV) Bible App

Dwibible is a 100% offline-first bilingual Bible application designed for comfortable low-light reading in remote areas without internet coverage. It delivers side-by-side Bahasa Melayu (BM) and English (KJV / NKJV) scripture comparison, instant verse and keyword search, customizable typography (A- / A+), personal verse notes and bookmarks, and rapid Old & New Testament navigation backed by a local SQLite (Room) database.

## User Review & Critical Decisions

> [!IMPORTANT]
> The following product and design choices have been confirmed from your clarifications and incorporated into this plan:

- **Confirmed Decision 1 — Reading View Layout**: Side-by-side split-screen comparison (BM on the left, English on the right) as the default layout matching your design mockup, with a quick header toggle to switch into a single-column stacked/focused view when desired.
- **Confirmed Decision 2 — Local Database Scripture Coverage**: Complete 66-book Old and New Testament structure preloaded offline into the local SQLite (Room) database with Bahasa Melayu (BM), King James Version (KJV), and New King James Version (NKJV) texts so users can browse any book, chapter, and verse 100% offline.
- **Confirmed Decision 3 — Verse Bookmarks & Personal Notes**: Tapping any verse opens an interactive bottom sheet where users can bookmark the verse, pick a highlight color, and write or edit personal study notes stored locally on the device.

---

## 1. Overview & Core Concept

- **What It Does**: Provides simultaneous reading of Bahasa Melayu (`Alkitab BM`) and English (`KJV` and `NKJV`) scriptures in a dark-themed interface tailored for zero-connectivity environments. Users can search by reference (e.g., `Genesis 1:1`, `Kejadian 1:1`, `John 3:16`) or keyword, switch English translations via a radio pop-up menu, adjust font size (`A-` / `A+`), and save personal notes and bookmarks.
- **Target Audience / Persona**: Readers, pastors, students, and community members in rural or remote areas with limited or no cellular/Wi-Fi coverage who rely on bilingual Malay-English scripture study in low-light settings.
- **Key Value**: Instant, zero-latency access to bilingual scripture and personal study tools without requiring network access, accounts, or external servers.

---

## 2. User Experience & Visual Design

### Key User Flows

1. **Bilingual Split-Screen Reading**:
   - On launch, the user lands on the main reader screen displaying the selected Book and Chapter in two synchronized vertical reading cards: **BM VERSION** on the left and **King James Version / New King James Version** on the right.
   - Users can scroll through verses comfortably or tap the layout toggle in the top bar to switch between **Split-Screen (Side-by-Side)** and **Single-Column (Interleaved / Focused)** reading modes.
2. **English Version Pop-Up Selector (KJV / NKJV)**:
   - Tapping the English column header (with the signature red downward triangle indicator `▼`) opens an elevated dark pop-up card with radio buttons for **King James Version (KJV)** and **New King James Version (NKJV)**. Selecting an option immediately updates the English column across all verses.
3. **Smart Search Bar (Reference & Keyword)**:
   - Typing in the rounded search bar under the `SEARCH VERSE` label (e.g., `Genesis 1:1`, `Kejadian 1`, `light`, `kasih`) filters verses in real time across BM, KJV, and NKJV or jumps directly to a matched book/chapter/verse.
4. **Typography & Font Resizer (`A-` / `A+`)**:
   - Accessible directly from the top toolbar, `A-` and `A+` controls smoothly scale verse text size (from `14sp` to `26sp`) with persistent local preference storage.
5. **Book & Chapter Bottom Navigation**:
   - Two prominent bottom cards (`Book: Genesis` and `Chapter: 1`) open structured modal pickers:
     - **Book Picker**: Filterable by Old Testament (*Perjanjian Lama*, 39 books) and New Testament (*Perjanjian Baru*, 27 books) showing both English and Malay book names (e.g., *Genesis / Kejadian*).
     - **Chapter Grid Picker**: A responsive numbered grid for instant chapter selection, plus quick previous/next chapter swipe or button navigation.
6. **Tap-to-Bookmark & Personal Study Notes**:
   - Tapping any verse card highlights the verse and opens a **Verse Study Bottom Sheet** displaying the full BM, KJV, and NKJV texts side-by-side, a one-tap Bookmark toggle, highlight color tags, and a personal Note editor. A dedicated Bookmarks & Notes sheet accessible from the top bar lists all saved verses for quick jumping.

### Visual Identity & Theme

- **Aesthetic Direction**: Atmospheric Night-Reader UI faithfully modeled after the attached Dwibible reference mockup—calm, high-contrast dark slate surfaces designed to prevent eye strain in dimly lit environments.
- **Color Palette & Mood**:
  - `--background`: Deep Midnight Slate (`#12161F`)
  - `--surface-card`: Elevated Charcoal Navy (`#1D2330`) with subtle border (`#2E3748`)
  - `--surface-popup`: Floating Slate Overlay (`#262E3D`)
  - `--primary`: Soft Celestial Blue (`#7BA7E1`) for active radio buttons, book/chapter icons, and search accents
  - `--accent-indicator`: Crimson Red Triangle (`#E53935`) on the English translation dropdown trigger
  - `--text-primary`: Crisp Warm Off-White (`#F1F5F9`) for scripture readability
  - `--text-secondary`: Muted Slate Gray (`#94A3B8`) for labels, scrollbars, and footer status
- **Typography & Hierarchy**:
  - **Brand Header (`Dwibible` & Footer)**: Classic editorial serif (`Playfair Display` / `Merriweather`) matching the serif header in the mockup.
  - **UI Controls & Verse Body**: Clean, high-legibility humanist sans-serif (`Inter` / `Open Sans`) with generous line height (`1.5x`) and dynamic `sp` scaling via `A-` / `A+`.
- **Component Styling & Layout**:
  - Top bar with back/reset action, book-with-cross icon + `Dwibible` serif title, `A-`/`A+` font resizer controls, layout toggle, and saved Bookmarks/Notes drawer button.
  - Pill-shaped search input (`RoundedCornerShape(28.dp)`) with leading search icon and trailing clear/voice-query helper.
  - Dual column headers featuring the `BM` open-book emblem and `EN` multi-page emblem above rounded scripture containers with visible vertical scroll indicators.
  - Persistent bottom status bar reading: `App by Rikko | KJV/NKJV | Offline Mode`.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Native Android (Kotlin + Jetpack Compose + Room SQLite) Architecture**
  - *Chosen Approach*: Build a native Android application using Jetpack Compose and Room SQLite rather than a cross-platform wrapper, while also providing a clean SQLite schema reference in-app.
  - *Why*: Ensures 100% compatibility with the Android build and streaming preview environment, instant SQLite query execution via KSP-compiled Room DAOs, and smooth dual-column scrolling performance.
  - *Alternatives Considered*: WebView or external network APIs were rejected because the core mission requires 100% offline reliability in zero-connectivity regions.
- **Decision 2: Pre-Seeded 66-Book Bilingual SQLite Database**
  - *Chosen Approach*: Bundle a structured multi-translation corpus covering all 66 canonical books (Old & New Testaments) with rich complete chapters for foundational books (Genesis/Kejadian, Psalms/Mazmur, Proverbs/Amsal, Matthew/Matius, Mark/Markus, Luke/Lukas, John/Yohanes, Acts/Kisah Rasul, Romans/Roma, Revelation/Wahyu) and key chapters across every single book of the Old and New Testaments in BM, KJV, and NKJV, plus an option to import/expand custom SQLite/JSON verse packs locally.
  - *Why*: Guarantees immediate startup without multi-minute network downloads while allowing the user to navigate to all 66 books offline, search across testaments, and test every workflow immediately.
- **Decision 3: Synchronized & Independent Dual-Pane Scrolling**
  - *Chosen Approach*: Align verses by verse number in synchronized rows or linked scroll panes, with a one-tap toggle between **Dual Card Split-Screen** (matching the mockup) and **Verse-by-Verse Aligned / Single Column** mode.
  - *Why*: Preserves the exact visual aesthetic of the two side-by-side cards from the reference image while ensuring long passages in Malay and English stay aligned and easy to compare.

---

## 4. Technical Architecture & Data Strategy *(Technical Reference)*

### Architecture & Component Diagram

```
┌───────────────────────────────────────────────────────────────────────────┐
│                         UI LAYER (Jetpack Compose)                        │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ TopAppBar: Dwibible Brand | Font Resizer (A-/A+) | Mode | Bookmarks │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ SmartSearchBar: Reference Parser ("Genesis 1:1") & Keyword Search   │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────┬──────────────────────────────────┐  │
│  │         BM VERSION PANE          │       ENGLISH VERSION PANE       │  │
│  │  • Malay (Alkitab BM) Verses     │  • KJV / NKJV Radio Pop-Up Menu  │  │
│  │  • Bookmark & Note Badges        │  • Synchronized Scroll & Tap     │  │
│  └──────────────────────────────────┴──────────────────────────────────┘  │
│  ┌──────────────────────────────────┬──────────────────────────────────┐  │
│  │   Book Selector ("Book: ...")    │  Chapter Selector ("Chapter: 1") │  │
│  └──────────────────────────────────┴──────────────────────────────────┘  │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ Modals: Verse Study Bottom Sheet (Notes/Bookmarks) | Book/Ch Picker │  │
│  │ Footer: "App by Rikko | KJV/NKJV | Offline Mode"                    │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────┬─────────────────────────────────────┘
                                      │ StateFlow<DwibibleUiState>
                                      ▼
┌───────────────────────────────────────────────────────────────────────────┐
│                       VIEWMODEL & REPOSITORY LAYER                        │
│  • DwibibleViewModel: Manages currentBook, currentChapter, englishVersion,│
│    fontSizeSp, searchQuery, layoutMode, and activeBottomSheet             │
│  • BibleRepository: Coordinates Room DAO queries, reference parsing,      │
│    and initial database seeding on first launch                           │
└─────────────────────────────────────┬─────────────────────────────────────┘
                                      │ Kotlin Coroutines / Flow
                                      ▼
┌───────────────────────────────────────────────────────────────────────────┐
│                     LOCAL PERSISTENCE (Room SQLite DB)                    │
│  ┌──────────────────────┐ ┌──────────────────────┐ ┌───────────────────┐  │
│  │   bible_verses       │ │   verse_bookmarks    │ │   reading_prefs   │  │
│  │ • id (PK)            │ │ • verseId (PK/FK)    │ │ • fontSizeSp      │  │
│  │ • bookId (1..66)     │ │ • isBookmarked       │ │ • englishVersion  │  │
│  │ • bookNameEn / Bm    │ │ • highlightColor     │ │ • lastBookId      │  │
│  │ • testament (OT/NT)  │ │ • personalNote       │ │ • lastChapter     │  │
│  │ • chapter, verse     │ │ • updatedAt          │ │ • isSplitScreen   │  │
│  │ • textBm, textKjv,   │ └──────────────────────┘ └───────────────────┘  │
│  │   textNkjv           │                                                 │
│  └──────────────────────┘                                                 │
└───────────────────────────────────────────────────────────────────────────┘
```

### Data Model & State

1. **`BibleVerseEntity` (`bible_verses` table)**:
   - `id`: Unique composite key or auto-generated ID.
   - `bookNumber`: Canonical order (`1` = Genesis / Kejadian through `66` = Revelation / Wahyu).
   - `bookNameEn`: English book title (e.g., `"Genesis"`).
   - `bookNameBm`: Bahasa Melayu book title (e.g., `"Kejadian"`).
   - `testament`: `"OT"` (Old Testament / Perjanjian Lama) or `"NT"` (New Testament / Perjanjian Baru).
   - `chapter`: Chapter number (`1..N`).
   - `verse`: Verse number (`1..N`).
   - `textBm`: Malay verse text.
   - `textKjv`: English King James Version text.
   - `textNkjv`: English New King James Version text.
2. **`VerseStudyEntity` (`verse_study` table)**:
   - `verseKey`: Unique key (`"{bookNumber}_{chapter}_{verse}"`).
   - `bookNumber`, `chapter`, `verse`: For fast joins and sorting.
   - `isBookmarked`: `Boolean` flag for bookmarked status.
   - `highlightColor`: Optional hex/tag identifier (Gold, Blue, Emerald, Rose).
   - `noteText`: User's personal study note string.
   - `updatedAt`: Timestamp for sorting saved bookmarks and notes.

### Interactive Component & State Mapping

- **Search Bar Input (`onSearchQueryChange`)**:
  - Parses structured references (such as `"Genesis 1:1"`, `"Kejadian 1:2"`, `"John 3:16"`, `"Mazmur 23"`) to either jump directly to that book/chapter or display matching verses across BM, KJV, and NKJV in an interactive search results overlay.
- **English Version Dropdown (`onSelectEnglishVersion`)**:
  - Tapping `King James Version ▼` toggles `isVersionMenuExpanded`. Selecting `King James Version (KJV)` or `New King James Version (NKJV)` updates `selectedEnglishVersion` in `DwibibleViewModel` and immediately swaps the right-hand pane text.
- **Font Resizer (`onAdjustFontSize`)**:
  - `A-` and `A+` buttons decrement/increment `fontSizeSp` within `[14f..26f]`, immediately scaling both the BM and English scripture panes.
- **Book & Chapter Pickers (`onSelectBook`, `onSelectChapter`)**:
  - Tapping `Book: Genesis` opens the 66-book modal sheet with OT/NT filter tabs and instant search. Selecting a book updates `currentBook` and resets `currentChapter = 1` (or opens the Chapter Grid Picker).
  - Tapping `Chapter: 1` opens the numeric grid for the active book's available chapters.
- **Verse Tap (`onVerseClicked`)**:
  - Opens the **Verse Study Bottom Sheet** where the user can toggle bookmark status, select a highlight color, write/save a personal note, or copy/share the bilingual verse text.
