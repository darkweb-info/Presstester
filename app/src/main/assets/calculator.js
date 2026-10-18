"use strict";

const display = document.getElementById("display");
const easterEgg = document.getElementById("easterEgg");
const audio = document.getElementById("sixSevenAudio");
let justCalculated = false;
let soundOn = true;

function toggleSound() {
  soundOn = !soundOn;
  const toggle = document.getElementById("soundToggle");
  toggle.classList.toggle("muted", !soundOn);
  toggle.textContent = soundOn ? "♫" : "♪̸";
  toggle.setAttribute("aria-label", soundOn ? "Отключить звук" : "Включить звук");
  if (!soundOn) { audio.pause(); audio.currentTime = 0; }
}


function press(value) {
  if (!easterEgg.hidden) return;
  if (display.value === "Ошибка" || display.value === "0" ||
      (justCalculated && /[0-9.]/.test(String(value)))) {
    display.value = value === "." ? "0." : String(value);
    justCalculated = false;
    return;
  }
  justCalculated = false;
  if (value === ".") {
    const lastNumber = display.value.split(/[+\-*/%()]/).pop();
    if (lastNumber.includes(".")) return;
  }
  if (display.value.length < 80) display.value += String(value);
}

function clearDisplay() {
  if (!easterEgg.hidden) return;
  display.value = "0";
  justCalculated = false;
}

function backspace() {
  if (!easterEgg.hidden) return;
  display.value = display.value.length > 1 ? display.value.slice(0, -1) : "0";
  justCalculated = false;
}

function calculate() {
  if (!easterEgg.hidden) return;
  try {
    // Only mathematical calculator characters, never external input / remote code.
    const expression = display.value.replace(/(\d+(?:\.\d+)?)%/g, "($1/100)");
    if (!/^[0-9.()+\-*/%\s]+$/.test(expression) || expression.length > 120) {
      throw Error("Bad input");
    }
    const rawResult = Function('"use strict"; return (' + expression + ')')();
    if (typeof rawResult !== "number" || !Number.isFinite(rawResult)) {
      throw Error("Invalid result");
    }

    // Display the result using calculator precision before checking secret 67.
    const result = Number(rawResult.toPrecision(12));
    display.value = String(result);
    justCalculated = true;

    // Any valid expression returning 67 triggers immediately after '='.
    // Examples: 67, 60+7, 70-3, 134/2, 6.7*10, (50+17).
    if (result === 67) showEasterEgg();
  } catch (_) {
    display.value = "Ошибка";
    justCalculated = false;
  }
}

function setNativeFullScreen(enabled) {
  try {
    if (window.PresstesterBridge && window.PresstesterBridge.setEasterEggActive) {
      window.PresstesterBridge.setEasterEggActive(enabled);
    }
  } catch (_) { /* Web preview has no native bridge. */ }
}

function showEasterEgg() {
  if (!easterEgg.hidden) return;
  easterEgg.hidden = false;
  document.body.classList.add("secret-active");
  setNativeFullScreen(true);
  replaySixSeven();
}

function replaySixSeven() {
  if (easterEgg.hidden || !soundOn) return;
  audio.pause();
  audio.currentTime = 0;
  audio.volume = 1; // Max in-app volume; system media volume remains user-controlled.
  const playback = audio.play();
  if (playback && playback.catch) playback.catch(() => {
    // If audio is blocked by the browser, the replay button still works.
  });
}

function closeEasterEgg() {
  audio.pause();
  audio.currentTime = 0;
  easterEgg.hidden = true;
  document.body.classList.remove("secret-active");
  setNativeFullScreen(false);
  display.value = "0";
  justCalculated = false;
}
window.closeEasterEgg = closeEasterEgg;

document.addEventListener("keydown", (event) => {
  if (!easterEgg.hidden) {
    if (event.key === "Escape" || event.key === "Backspace") {
      event.preventDefault();
      closeEasterEgg();
    }
    return;
  }
  const key = event.key;
  if (key === "Enter" || key === "=") {
    event.preventDefault();
    calculate();
  } else if (/^[0-9]$/.test(key)) press(key);
  else if (key === "." || key === "," || event.code === "NumpadDecimal") press(".");
  else if (["+", "-", "*", "/", "%", "(", ")"].includes(key)) press(key);
  else if (key === "Backspace") { event.preventDefault(); backspace(); }
  else if (key === "Escape") clearDisplay();
});
