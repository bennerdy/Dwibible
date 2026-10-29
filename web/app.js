// Dwibible PWA - Offline Web Logic
const SEED_VERSES = [
  { book: 'Genesis', bookBm: 'Kejadian', ch: 1, v: 1, bm: 'Pada mulanya Allah menciptakan langit dan bumi.', kjv: 'In the beginning God created the heaven and the earth.', nkjv: 'In the beginning God created the heavens and the earth.' },
  { book: 'Genesis', bookBm: 'Kejadian', ch: 1, v: 2, bm: 'Bumi belum berbentuk dan kosong; gelap gulita menutupi samudera raya, dan Roh Allah melayang-layang di atas permukaan air.', kjv: 'And the earth was without form, and void; and darkness was upon the face of the deep. And the Spirit of God moved upon the face of the waters.', nkjv: 'The earth was without form, and void; and darkness was on the face of the deep. And the Spirit of God was hovering over the face of the waters.' },
  { book: 'Genesis', bookBm: 'Kejadian', ch: 1, v: 3, bm: 'Berfirmanlah Allah: "Jadilah terang." Lalu terang itu jadi.', kjv: 'And God said, Let there be light: and there was light.', nkjv: 'Then God said, "Let there be light"; and there was light.' },
  { book: 'Genesis', bookBm: 'Kejadian', ch: 1, v: 4, bm: 'Allah melihat bahwa terang itu baik, lalu dipisahkan-Nyalah terang itu dari gelap.', kjv: 'And God saw the light, that it was good: and God divided the light from the darkness.', nkjv: 'And God saw the light, that it was good; and God divided the light from the darkness.' },
  { book: 'Genesis', bookBm: 'Kejadian', ch: 1, v: 5, bm: 'Dan Allah menamai terang itu siang, dan gelap itu malam. Jadilah petang dan jadilah pagi, itulah hari pertama.', kjv: 'And God called the light Day, and the darkness he called Night. And the evening and the morning were the first day.', nkjv: 'God called the light Day, and the darkness He called Night. So the evening and the morning were the first day.' },
  { book: 'Psalms', bookBm: 'Mazmur', ch: 23, v: 1, bm: 'TUHAN adalah gembalaku, takkan kekurangan aku.', kjv: 'The LORD is my shepherd; I shall not want.', nkjv: 'The LORD is my shepherd; I shall not want.' },
  { book: 'John', bookBm: 'Yohanes', ch: 3, v: 16, bm: 'Karena begitu besar kasih Allah akan dunia ini, sehingga Ia telah mengaruniakan Anak-Nya yang tunggal, supaya setiap orang yang percaya kepada-Nya tidak binasa, melainkan beroleh hidup yang kekal.', kjv: 'For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life.', nkjv: 'For God so loved the world that He gave His only begotten Son, that whoever believes in Him should not perish but have everlasting life.' }
];

let currentEnglishVersion = 'NKJV'; // default NKJV
let currentFontSize = 15;
let currentBook = 'Genesis';
let currentChapter = 1;
let activeVerse = null;
let quizScore = 0;

// LocalStorage helpers
function getStudy(key) {
  try { return JSON.parse(localStorage.getItem('dwibible_study_' + key)) || {}; }
  catch(e) { return {}; }
}

function saveStudy(key, data) {
  localStorage.setItem('dwibible_study_' + key, JSON.stringify(data));
}

// Render Verses
function renderVerses() {
  const bmList = document.getElementById('bm-verses-list');
  const enList = document.getElementById('en-verses-list');
  bmList.innerHTML = '';
  enList.innerHTML = '';

  const query = document.getElementById('search-input').value.trim().toLowerCase();
  const filtered = SEED_VERSES.filter(v => {
    if (!query) return v.book === currentBook && v.ch === currentChapter;
    return v.bm.toLowerCase().includes(query) || v.nkjv.toLowerCase().includes(query) || v.kjv.toLowerCase().includes(query) || `${v.book} ${v.ch}:${v.v}`.toLowerCase().includes(query);
  });

  filtered.forEach(item => {
    const key = `${item.book}_${item.ch}_${item.v}`;
    const study = getStudy(key);
    const hlClass = study.color && study.color !== 'NONE' ? `highlight-${study.color.toLowerCase()}` : '';

    // Left Pane: BM
    const bmDiv = document.createElement('div');
    bmDiv.className = `verse-item ${hlClass}`;
    bmDiv.style.fontSize = `${currentFontSize}px`;
    bmDiv.innerHTML = `<span class="verse-ref">${item.bookBm} ${item.ch}:${item.v}.</span> ${item.bm}`;
    bmDiv.onclick = () => openVerseStudy(item);
    bmList.appendChild(bmDiv);

    // Right Pane: EN (NKJV or KJV)
    const enText = currentEnglishVersion === 'KJV' ? item.kjv : item.nkjv;
    const enDiv = document.createElement('div');
    enDiv.className = `verse-item ${hlClass}`;
    enDiv.style.fontSize = `${currentFontSize}px`;
    enDiv.innerHTML = `<span class="verse-ref">${item.book} ${item.ch}:${item.v}.</span> ${enText}`;
    enDiv.onclick = () => openVerseStudy(item);
    enList.appendChild(enDiv);
  });
}

// Open Verse Comparison & Study Sheet
function openVerseStudy(item) {
  activeVerse = item;
  const key = `${item.book}_${item.ch}_${item.v}`;
  const study = getStudy(key);

  document.getElementById('study-verse-title').innerText = `${item.bookBm} / ${item.book} ${item.ch}:${item.v}`;
  document.getElementById('compare-text-bm').innerText = item.bm;
  document.getElementById('compare-text-kjv').innerText = item.kjv;
  document.getElementById('compare-text-nkjv').innerText = item.nkjv;
  document.getElementById('study-note-input').value = study.note || '';

  document.querySelectorAll('#highlight-selector .chip').forEach(c => {
    c.classList.toggle('active', c.getAttribute('data-color') === (study.color || 'NONE'));
  });

  document.getElementById('modal-verse-study').classList.add('active');
}

// Event Listeners
document.addEventListener('DOMContentLoaded', () => {
  // Service Worker Registration for Offline iPhone PWA
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('./sw.js').catch(console.error);
  }

  renderVerses();

  // Search
  document.getElementById('search-input').addEventListener('input', renderVerses);
  document.getElementById('search-clear').addEventListener('click', () => {
    document.getElementById('search-input').value = '';
    renderVerses();
  });

  // Font resizer
  document.getElementById('font-increase').addEventListener('click', () => {
    currentFontSize = Math.min(24, currentFontSize + 2);
    document.getElementById('font-size-label').innerText = `${currentFontSize}px`;
    renderVerses();
  });
  document.getElementById('font-decrease').addEventListener('click', () => {
    currentFontSize = Math.max(12, currentFontSize - 2);
    document.getElementById('font-size-label').innerText = `${currentFontSize}px`;
    renderVerses();
  });

  // Day / Dark theme toggle
  document.getElementById('theme-toggle').addEventListener('click', () => {
    document.body.classList.toggle('light-mode');
  });

  // Split / Single column toggle
  document.getElementById('layout-toggle').addEventListener('click', () => {
    document.getElementById('reader-container').classList.toggle('single-column');
  });

  // English Version Toggle (NKJV / KJV)
  document.getElementById('en-version-trigger').addEventListener('click', () => {
    currentEnglishVersion = currentEnglishVersion === 'NKJV' ? 'KJV' : 'NKJV';
    document.getElementById('en-version-label').innerText = currentEnglishVersion === 'NKJV' ? 'New King James Version (NKJV)' : 'King James Version (KJV)';
    renderVerses();
  });

  // Daily Verse Modal
  document.getElementById('btn-daily-verse').addEventListener('click', () => {
    const todayVerse = SEED_VERSES[Math.floor(Math.random() * SEED_VERSES.length)];
    document.getElementById('daily-ref').innerText = `${todayVerse.bookBm} / ${todayVerse.book} ${todayVerse.ch}:${todayVerse.v}`;
    document.getElementById('daily-text-bm').innerText = `"${todayVerse.bm}"`;
    document.getElementById('daily-text-en').innerText = `"${todayVerse.nkjv}"`;
    document.getElementById('modal-daily-verse').classList.add('active');
  });
  document.getElementById('close-daily-modal').onclick = () => document.getElementById('modal-daily-verse').classList.remove('active');
  document.getElementById('btn-close-daily').onclick = () => document.getElementById('modal-daily-verse').classList.remove('active');

  // Study Modal Actions
  document.getElementById('close-study-modal').onclick = () => document.getElementById('modal-verse-study').classList.remove('active');
  document.querySelectorAll('#highlight-selector .chip').forEach(chip => {
    chip.onclick = () => {
      document.querySelectorAll('#highlight-selector .chip').forEach(c => c.classList.remove('active'));
      chip.classList.add('active');
    };
  });
  document.getElementById('btn-save-study').onclick = () => {
    if (!activeVerse) return;
    const key = `${activeVerse.book}_${activeVerse.ch}_${activeVerse.v}`;
    const activeChip = document.querySelector('#highlight-selector .chip.active');
    const color = activeChip ? activeChip.getAttribute('data-color') : 'NONE';
    const note = document.getElementById('study-note-input').value.trim();
    saveStudy(key, { color, note, updatedAt: Date.now() });
    document.getElementById('modal-verse-study').classList.remove('active');
    renderVerses();
    alert('Saved offline!');
  };

  // Offline Quiz
  document.getElementById('btn-quiz').onclick = loadQuiz;
  document.getElementById('close-quiz-modal').onclick = () => document.getElementById('modal-quiz').classList.remove('active');
});

function loadQuiz() {
  const target = SEED_VERSES[Math.floor(Math.random() * SEED_VERSES.length)];
  const distractors = SEED_VERSES.filter(v => v !== target).slice(0, 3);
  const options = [target, ...distractors].sort(() => Math.random() - 0.5);

  document.getElementById('quiz-clue').innerText = `"${target.nkjv}"`;
  const container = document.getElementById('quiz-options');
  container.innerHTML = '';
  document.getElementById('quiz-feedback').style.display = 'none';
  document.getElementById('btn-next-quiz').style.display = 'none';

  options.forEach((opt, idx) => {
    const btn = document.createElement('button');
    btn.className = 'nav-btn';
    btn.style.textAlign = 'left';
    btn.innerText = `${String.fromCharCode(65 + idx)}. ${opt.bookBm} / ${opt.book} ${opt.ch}:${opt.v}`;
    btn.onclick = () => {
      if (opt === target) {
        quizScore++;
        document.getElementById('quiz-score').innerText = quizScore;
        btn.style.background = '#153E32';
        btn.style.borderColor = '#22C55E';
        document.getElementById('quiz-feedback').innerText = 'Correct! Well done!';
        document.getElementById('quiz-feedback').style.color = '#4ADE80';
      } else {
        btn.style.background = '#4A1D2F';
        btn.style.borderColor = '#E53935';
        document.getElementById('quiz-feedback').innerText = `Incorrect. Correct: ${target.book} ${target.ch}:${target.v}`;
        document.getElementById('quiz-feedback').style.color = '#FCA5A5';
      }
      document.getElementById('quiz-feedback').style.display = 'block';
      document.getElementById('btn-next-quiz').style.display = 'block';
      Array.from(container.children).forEach(b => b.disabled = true);
    };
    container.appendChild(btn);
  });

  document.getElementById('btn-next-quiz').onclick = loadQuiz;
  document.getElementById('modal-quiz').classList.add('active');
}
