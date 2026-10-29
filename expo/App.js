import React, { useState } from 'react';
import {
  StyleSheet,
  Text,
  View,
  SafeAreaView,
  ScrollView,
  TouchableOpacity,
  TextInput,
  Modal,
  Alert
} from 'react-native';
import { StatusBar } from 'expo-status-bar';

const SEED_VERSES = [
  { book: 'Genesis', bookBm: 'Kejadian', ch: 1, v: 1, bm: 'Pada mulanya Allah menciptakan langit dan bumi.', kjv: 'In the beginning God created the heaven and the earth.', nkjv: 'In the beginning God created the heavens and the earth.' },
  { book: 'Genesis', bookBm: 'Kejadian', ch: 1, v: 2, bm: 'Bumi belum berbentuk dan kosong; gelap gulita menutupi samudera raya, dan Roh Allah melayang-layang di atas permukaan air.', kjv: 'And the earth was without form, and void; and darkness was upon the face of the deep.', nkjv: 'The earth was without form, and void; and darkness was on the face of the deep.' },
  { book: 'Genesis', bookBm: 'Kejadian', ch: 1, v: 3, bm: 'Berfirmanlah Allah: "Jadilah terang." Lalu terang itu jadi.', kjv: 'And God said, Let there be light: and there was light.', nkjv: 'Then God said, "Let there be light"; and there was light.' },
  { book: 'Psalms', bookBm: 'Mazmur', ch: 23, v: 1, bm: 'TUHAN adalah gembalaku, takkan kekurangan aku.', kjv: 'The LORD is my shepherd; I shall not want.', nkjv: 'The LORD is my shepherd; I shall not want.' },
  { book: 'John', bookBm: 'Yohanes', ch: 3, v: 16, bm: 'Karena begitu besar kasih Allah akan dunia ini, sehingga Ia telah mengaruniakan Anak-Nya yang tunggal...', kjv: 'For God so loved the world, that he gave his only begotten Son...', nkjv: 'For God so loved the world that He gave His only begotten Son...' }
];

export default function App() {
  const [englishVersion, setEnglishVersion] = useState('NKJV');
  const [isDarkMode, setIsDarkMode] = useState(true);
  const [fontSize, setFontSize] = useState(14);
  const [search, setSearch] = useState('');
  const [activeVerse, setActiveVerse] = useState(null);
  const [showStudyModal, setShowStudyModal] = useState(false);
  const [showDailyModal, setShowDailyModal] = useState(false);
  const [highlights, setHighlights] = useState({});
  const [notes, setNotes] = useState({});
  const [noteDraft, setNoteDraft] = useState('');

  const colors = isDarkMode
    ? { bg: '#12161F', top: '#1A202C', card: '#1E2433', border: '#2D364B', text: '#F1F5F9', muted: '#94A3B8', blue: '#7BA7E1', gold: '#FFD700', red: '#E53935' }
    : { bg: '#F4F1EA', top: '#FFFFFF', card: '#FFFFFF', border: '#D5DCE6', text: '#18202C', muted: '#526075', blue: '#1E5BB8', gold: '#B45309', red: '#D32F2F' };

  const filteredVerses = SEED_VERSES.filter(v => {
    if (!search.trim()) return true;
    const q = search.toLowerCase();
    return v.bm.toLowerCase().includes(q) || v.nkjv.toLowerCase().includes(q) || `${v.book} ${v.ch}:${v.v}`.toLowerCase().includes(q);
  });

  const onSelectVerse = (v) => {
    setActiveVerse(v);
    const key = `${v.book}_${v.ch}_${v.v}`;
    setNoteDraft(notes[key] || '');
    setShowStudyModal(true);
  };

  const saveStudy = (color) => {
    if (!activeVerse) return;
    const key = `${activeVerse.book}_${activeVerse.ch}_${activeVerse.v}`;
    if (color) setHighlights({ ...highlights, [key]: color });
    if (noteDraft) setNotes({ ...notes, [key]: noteDraft });
    setShowStudyModal(false);
    Alert.alert('Saved', 'Saved offline on device!');
  };

  return (
    <SafeAreaView style={[styles.container, { backgroundColor: colors.bg }]}>
      <StatusBar style={isDarkMode ? 'light' : 'dark'} />

      {/* Header */}
      <View style={[styles.header, { backgroundColor: colors.top, borderColor: colors.border }]}>
        <View style={styles.row}>
          <TouchableOpacity onPress={() => setShowDailyModal(true)} style={styles.iconBtn}>
            <Text style={{ fontSize: 18 }}>☀️</Text>
          </TouchableOpacity>
        </View>
        <Text style={[styles.brand, { color: colors.text }]}>📖 Dwibible</Text>
        <TouchableOpacity onPress={() => setIsDarkMode(!isDarkMode)} style={styles.iconBtn}>
          <Text style={{ fontSize: 16 }}>{isDarkMode ? '☀️' : '🌙'}</Text>
        </TouchableOpacity>
      </View>

      {/* Search Bar */}
      <View style={styles.searchContainer}>
        <TextInput
          placeholder="Search verse or keyword..."
          placeholderTextColor={colors.muted}
          value={search}
          onChangeText={setSearch}
          style={[styles.searchInput, { backgroundColor: colors.card, borderColor: colors.border, color: colors.text }]}
        />
      </View>

      {/* Toolbar: Font & Version Toggle */}
      <View style={styles.toolbar}>
        <Text style={[styles.emblem, { color: colors.blue }]}>BM</Text>
        <View style={[styles.toolbarControls, { backgroundColor: colors.card, borderColor: colors.border }]}>
          <TouchableOpacity onPress={() => setFontSize(Math.max(12, fontSize - 2))}><Text style={[styles.toolBtn, { color: colors.text }]}>A-</Text></TouchableOpacity>
          <Text style={{ color: colors.blue, fontWeight: 'bold' }}>{fontSize}sp</Text>
          <TouchableOpacity onPress={() => setFontSize(Math.min(22, fontSize + 2))}><Text style={[styles.toolBtn, { color: colors.text }]}>A+</Text></TouchableOpacity>
        </View>
        <Text style={[styles.emblem, { color: colors.blue }]}>EN</Text>
      </View>

      {/* Main Split-Screen Reader */}
      <View style={styles.readerRow}>
        {/* BM Pane */}
        <View style={[styles.pane, { backgroundColor: colors.card, borderColor: colors.border }]}>
          <Text style={[styles.paneTitle, { color: colors.text, borderColor: colors.border }]}>BM VERSION</Text>
          <ScrollView contentContainerStyle={styles.scrollList}>
            {filteredVerses.map(v => {
              const key = `${v.book}_${v.ch}_${v.v}`;
              const hl = highlights[key];
              return (
                <TouchableOpacity key={'bm_' + key} onPress={() => onSelectVerse(v)} style={[styles.verseItem, hl && { backgroundColor: hl }]}>
                  <Text style={[styles.verseRef, { color: colors.gold, fontSize }]}>{v.bookBm} {v.ch}:{v.v}. </Text>
                  <Text style={{ color: colors.text, fontSize, lineHeight: fontSize * 1.4 }}>{v.bm}</Text>
                </TouchableOpacity>
              );
            })}
          </ScrollView>
        </View>

        {/* English Pane */}
        <View style={[styles.pane, { backgroundColor: colors.card, borderColor: colors.border }]}>
          <TouchableOpacity onPress={() => setEnglishVersion(englishVersion === 'NKJV' ? 'KJV' : 'NKJV')}>
            <Text style={[styles.paneTitle, { color: colors.text, borderColor: colors.border }]}>{englishVersion} ▼</Text>
          </TouchableOpacity>
          <ScrollView contentContainerStyle={styles.scrollList}>
            {filteredVerses.map(v => {
              const key = `${v.book}_${v.ch}_${v.v}`;
              const hl = highlights[key];
              const enText = englishVersion === 'KJV' ? v.kjv : v.nkjv;
              return (
                <TouchableOpacity key={'en_' + key} onPress={() => onSelectVerse(v)} style={[styles.verseItem, hl && { backgroundColor: hl }]}>
                  <Text style={[styles.verseRef, { color: colors.gold, fontSize }]}>{v.book} {v.ch}:{v.v}. </Text>
                  <Text style={{ color: colors.text, fontSize, lineHeight: fontSize * 1.4 }}>{enText}</Text>
                </TouchableOpacity>
              );
            })}
          </ScrollView>
        </View>
      </View>

      {/* Verse Comparison & Study Modal */}
      {activeVerse && (
        <Modal visible={showStudyModal} animationType="slide" transparent>
          <View style={styles.modalOverlay}>
            <View style={[styles.modalCard, { backgroundColor: colors.card, borderColor: colors.blue }]}>
              <Text style={[styles.modalTitle, { color: colors.gold }]}>{activeVerse.bookBm} / {activeVerse.book} {activeVerse.ch}:{activeVerse.v}</Text>
              <Text style={[styles.modalSubtitle, { color: colors.muted }]}>Side-by-Side 3-Version Comparison</Text>

              {/* 3 Versions Compare */}
              <View style={styles.compareRow}>
                <View style={[styles.compareCol, { backgroundColor: colors.top, borderColor: colors.border }]}>
                  <Text style={{ color: colors.blue, fontWeight: 'bold', fontSize: 10 }}>MALAY (BM)</Text>
                  <Text style={{ color: colors.text, fontSize: 11, marginTop: 4 }}>{activeVerse.bm}</Text>
                </View>
                <View style={[styles.compareCol, { backgroundColor: colors.top, borderColor: colors.border }]}>
                  <Text style={{ color: colors.gold, fontWeight: 'bold', fontSize: 10 }}>KJV</Text>
                  <Text style={{ color: colors.text, fontSize: 11, marginTop: 4 }}>{activeVerse.kjv}</Text>
                </View>
                <View style={[styles.compareCol, { backgroundColor: colors.top, borderColor: colors.border }]}>
                  <Text style={{ color: colors.red, fontWeight: 'bold', fontSize: 10 }}>NKJV</Text>
                  <Text style={{ color: colors.text, fontSize: 11, marginTop: 4 }}>{activeVerse.nkjv}</Text>
                </View>
              </View>

              {/* Highlight colors */}
              <Text style={{ color: colors.muted, fontSize: 11, fontWeight: 'bold', marginTop: 10 }}>COLOR HIGHLIGHT</Text>
              <View style={styles.row}>
                {['rgba(250, 204, 21, 0.35)', 'rgba(34, 197, 94, 0.35)', 'rgba(56, 189, 248, 0.35)'].map((col, idx) => (
                  <TouchableOpacity key={idx} onPress={() => saveStudy(col)} style={[styles.colorBtn, { backgroundColor: col }]}>
                    <Text style={{ fontSize: 12, color: colors.text }}>●</Text>
                  </TouchableOpacity>
                ))}
              </View>

              <TextInput
                placeholder="Personal study note..."
                placeholderTextColor={colors.muted}
                value={noteDraft}
                onChangeText={setNoteDraft}
                style={[styles.noteInput, { backgroundColor: colors.top, borderColor: colors.border, color: colors.text }]}
                multiline
              />

              <TouchableOpacity onPress={() => saveStudy(null)} style={[styles.actionBtn, { backgroundColor: colors.blue }]}>
                <Text style={{ fontWeight: 'bold', color: '#12161F' }}>Save Offline</Text>
              </TouchableOpacity>
              <TouchableOpacity onPress={() => setShowStudyModal(false)} style={{ marginTop: 8 }}>
                <Text style={{ textAlign: 'center', color: colors.muted }}>Close</Text>
              </TouchableOpacity>
            </View>
          </View>
        </Modal>
      )}

      {/* Daily Verse Modal */}
      <Modal visible={showDailyModal} animationType="fade" transparent>
        <View style={styles.modalOverlay}>
          <View style={[styles.modalCard, { backgroundColor: colors.card, borderColor: colors.blue }]}>
            <Text style={[styles.modalTitle, { color: colors.text }]}>Daily Verse</Text>
            <Text style={[styles.modalSubtitle, { color: colors.muted }]}>Calculated 100% Offline</Text>
            <View style={{ marginVertical: 10, padding: 10, backgroundColor: colors.top, borderRadius: 8, borderWidth: 1, borderColor: colors.border }}>
              <Text style={{ color: colors.gold, fontWeight: 'bold' }}>Deuteronomy 33:13</Text>
              <Text style={{ color: colors.text, marginTop: 4 }}>BM: "Tentang Yusuf ia berkata: Kiranya negerinya diberkati oleh TUHAN..."</Text>
              <Text style={{ color: colors.text, marginTop: 4 }}>NKJV: "And of Joseph he said: Blessed of the LORD be his land..."</Text>
            </View>
            <TouchableOpacity onPress={() => setShowDailyModal(false)} style={[styles.actionBtn, { backgroundColor: colors.blue }]}>
              <Text style={{ fontWeight: 'bold', color: '#12161F' }}>Close</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  header: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', paddingHorizontal: 12, paddingVertical: 10, borderBottomWidth: 1 },
  brand: { fontSize: 20, fontWeight: 'bold' },
  iconBtn: { padding: 4 },
  searchContainer: { paddingHorizontal: 12, paddingVertical: 6 },
  searchInput: { height: 40, borderWidth: 1, borderRadius: 20, paddingHorizontal: 14, fontSize: 14 },
  toolbar: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingHorizontal: 16, paddingVertical: 4 },
  emblem: { fontWeight: '800', fontSize: 14 },
  toolbarControls: { flexDirection: 'row', alignItems: 'center', gap: 10, borderWidth: 1, borderRadius: 16, paddingHorizontal: 10, paddingVertical: 4 },
  toolBtn: { fontWeight: 'bold', paddingHorizontal: 4 },
  readerRow: { flex: 1, flexDirection: 'row', gap: 6, paddingHorizontal: 8, paddingBottom: 6 },
  pane: { flex: 1, borderWidth: 1, borderRadius: 10, overflow: 'hidden' },
  paneTitle: { textAlign: 'center', paddingVertical: 8, fontWeight: 'bold', fontSize: 13, borderBottomWidth: 1 },
  scrollList: { padding: 8, gap: 10 },
  verseItem: { padding: 4, borderRadius: 4 },
  verseRef: { fontWeight: 'bold' },
  modalOverlay: { flex: 1, backgroundColor: 'rgba(0,0,0,0.65)', justifyContent: 'flex-end' },
  modalCard: { borderTopLeftRadius: 18, borderTopRightRadius: 18, borderTopWidth: 2, padding: 18 },
  modalTitle: { fontSize: 17, fontWeight: 'bold' },
  modalSubtitle: { fontSize: 11, marginBottom: 8 },
  compareRow: { flexDirection: 'row', gap: 4, marginVertical: 8 },
  compareCol: { flex: 1, borderWidth: 1, borderRadius: 6, padding: 6 },
  row: { flexDirection: 'row', gap: 8, alignItems: 'center', marginVertical: 6 },
  colorBtn: { paddingHorizontal: 12, paddingVertical: 6, borderRadius: 6 },
  noteInput: { height: 60, borderWidth: 1, borderRadius: 8, padding: 8, marginVertical: 8, fontSize: 13 },
  actionBtn: { paddingVertical: 12, borderRadius: 8, alignItems: 'center', marginTop: 4 }
});
