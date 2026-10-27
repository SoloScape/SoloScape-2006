'use strict';

// Run from the repository root: node Client/WebClient/tests/browser-input.test.cjs
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');

const app = fs.readFileSync(path.join(__dirname, '../web/app.js'), 'utf8');

function browser(bounds = { left: 0, top: 0, width: 765, height: 503 }) {
  const callbacks = {};
  const sent = [];
  const timers = [];
  let now = 1000;

  const canvas = {
    getBoundingClientRect: () => bounds,
    getContext: () => ({ fillRect() {}, drawImage() {} }),
    setPointerCapture() {},
    addEventListener(name, handler) { callbacks['canvas:' + name] = handler; }
  };
  const status = { textContent: '', classList: { toggle() {} } };
  const keyboard = { addEventListener(name, handler) { callbacks['keyboard:' + name] = handler; } };
  const sound = { textContent: '', addEventListener(name, handler) { callbacks['sound:' + name] = handler; } };
  const softInput = { addEventListener(name, handler) { callbacks['input:' + name] = handler; } };
  const elements = { game: canvas, status, keyboard, sound, softInput };
  const document = { getElementById: name => elements[name], activeElement: null };
  const window = {
    addEventListener(name, handler) { callbacks['window:' + name] = handler; }
  };
  class WebSocket {
    static OPEN = 1;
    constructor() { this.readyState = WebSocket.OPEN; }
    addEventListener() {}
    send(bytes) { sent.push([...bytes]); }
  }

  vm.runInNewContext(app, {
    document, window, WebSocket,
    location: { protocol: 'http:', host: '127.0.0.1:8081' },
    performance: { now: () => now },
    setTimeout: (handler, delay) => timers.push({ handler, delay }),
    navigator: {},
    addEventListener() {},
    console
  }, { filename: 'web/app.js' });

  function emit(name, options = {}) {
    const target = name === 'blur' ? 'window:blur' : 'canvas:' + name;
    assert.equal(typeof callbacks[target], 'function', target + ' is registered');
    const event = {
      pointerId: 1, pointerType: 'mouse', button: 0,
      clientX: 100, clientY: 100, preventDefault() {},
      ...options
    };
    callbacks[target](event);
  }

  return {
    sent, timers, emit,
    advance(ms) { now += ms; },
    flushTimers() {
      while (timers.length) timers.shift().handler();
    }
  };
}
const count = (packets, type, button) =>
  packets.filter(packet => packet[0] === type &&
    (button === undefined || packet[1] === button)).length;
const coord = (packet, offset) => packet[offset] * 256 + packet[offset + 1];

{
  const b = browser();
  b.emit('pointermove', { clientX: 350, clientY: 200 });
  assert.equal(count(b.sent, 1), 1, 'ordinary hover must move the game cursor');
  b.emit('pointerleave');
  assert.equal(count(b.sent, 6), 1, 'canvas exit must reach JMouseListener');
  b.advance(30);
  b.emit('pointermove', { clientX: 200, clientY: 150 });
  assert.equal(count(b.sent, 1), 2, 'reentering must restore game cursor');

  b.emit('pointerdown', { button: 2 });
  b.emit('pointerup', { button: 2 });
  b.emit('contextmenu', { button: 2 });
  assert.equal(count(b.sent, 2, 3), 1, 'right click must press exactly once');
  assert.equal(count(b.sent, 3, 3), 1, 'right click must release exactly once');
  assert.equal(count(b.sent, 7), 0, 'stationary right-click cannot start a drag');
}
{
  const b = browser();
  b.emit('pointerdown', { pointerType: 'touch', clientX: 140, clientY: 110 });
  b.emit('pointerup', { pointerType: 'touch', clientX: 140, clientY: 110 });
  assert.equal(count(b.sent, 2, 1), 1, 'tap should press left button');
  assert.equal(count(b.sent, 3, 1), 0, 'tap should not immediately release');
  assert.ok(b.timers[0].delay >= 40, 'leave a full client frame to process tap');
  b.flushTimers();
  assert.equal(count(b.sent, 3, 1), 1, 'tap should release once');
}
{
  const b = browser();
  b.emit('pointerdown', { pointerType: 'touch' });
  b.advance(500);
  b.emit('contextmenu', { pointerType: 'touch' });
  b.emit('pointerup', { pointerType: 'touch' });
  b.flushTimers();
  assert.equal(count(b.sent, 2, 3), 1, 'long press must open only one right menu');
  assert.equal(count(b.sent, 3, 3), 1, 'long press must release once');
}
{
  const b = browser();
  b.emit('pointerdown', { pointerType: 'touch' });
  b.emit('pointermove', { pointerType: 'touch', clientX: 135, clientY: 135 });
  b.emit('pointerup', { pointerType: 'touch', clientX: 135, clientY: 135 });
  assert.equal(count(b.sent, 2, 1), 1, 'touch drag presses once');
  assert.ok(count(b.sent, 7, 1) >= 1, 'touch swipe must emit game drag');
  assert.equal(count(b.sent, 3, 1), 1, 'touch drag releases once');
  assert.equal(b.timers.length, 0, 'drag must not schedule an extra tap');
}
{
  const b = browser();
  b.emit('pointerdown', { pointerType: 'touch' });
  b.emit('pointerup', { pointerType: 'touch', clientX: 1500, clientY: 100 });
  assert.equal(count(b.sent, 2), 0, 'finger released outside cannot click minimap');
  assert.equal(b.timers.length, 0, 'off-canvas touch cannot generate deferred click');
}
{
  const b = browser();
  b.emit('pointerdown', { clientX: 300, clientY: 300 });
  b.emit('pointermove', { clientX: 1200, clientY: 300 });
  b.emit('pointerup', { clientX: 1200, clientY: 300 });
  assert.equal(count(b.sent, 2, 1), 1, 'only one initial mouse click');
  assert.equal(count(b.sent, 6), 1, 'drag outside must send exit');
  assert.equal(count(b.sent, 3, 1), 1, 'mouse release outside must clear held button');
}
{
  const b = browser({ left: 120, top: 40, width: 382.5, height: 251.5 });
  b.emit('pointerdown', { clientX: 311.25, clientY: 165.75 });
  const press = b.sent.find(packet => packet[0] === 2);
  assert.ok(Math.abs(coord(press, 2) - 382) <= 1, 'scaled X maps to RuneScape X');
  assert.ok(Math.abs(coord(press, 4) - 251) <= 1, 'scaled Y maps to RuneScape Y');
  b.emit('blur');
  assert.equal(count(b.sent, 3, 1), 1, 'focus loss releases pressed mouse');
  assert.equal(count(b.sent, 6), 1, 'focus loss moves cursor off the game');
}
console.log('SoloScape browser mouse/touch input regression checks passed.');
