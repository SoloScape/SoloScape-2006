'use strict';
const assert = require('node:assert/strict');
const fs = require('node:fs');
const vm = require('node:vm');
const html = fs.readFileSync('Client/WebClient/web/local-render.html', 'utf8');
const inline = html.match(/<script>\s*([\s\S]*?)<\/script>/)[1];
new vm.Script(inline);
const source = inline.slice(inline.indexOf('  // Local touch gestures'),
    inline.indexOf('  function fitClient()'));
assert.ok(source.includes('tapAfterHover'), 'Touch handlers were not found');
const handlers = {};
const calls = [];
let now = 0;
let frame = 0;
let rect = { left: 0, top: 0, width: 765, height: 503 };
const display = {
  addEventListener(type, handler) { (handlers[type] ||= []).push(handler); },
  getBoundingClientRect() { return rect; },
  setPointerCapture() {}
};
const context = {
  display,
  window: { addEventListener() {} },
  clientBridge: {
    async sendMouse(id, button, x, y) { calls.push({ id, button, x, y, frame }); return 0; },
    async sceneFrame() { return ++frame; },
    async zoom(steps) { calls.push({ zoom: steps }); }
  },
  performance: { now: () => now },
  setTimeout(callback, delay) { now += delay; queueMicrotask(callback); },
  console
};
vm.runInNewContext(source, context);
function pointer(type, id, x, y, pointerType = 'touch') {
  const scale = Math.min(rect.width / 765, rect.height / 503);
  const event = {
    pointerType, pointerId: id,
    clientX: rect.left + (rect.width - 765 * scale) / 2 + x * scale,
    clientY: rect.top + (rect.height - 503 * scale) / 2 + y * scale,
    preventDefault() { this.prevented = true; },
    stopImmediatePropagation() { this.stopped = true; }
  };
  for (const handler of handlers[type] || []) {
    handler(event);
    if (event.stopped) break;
  }
  return event;
}
async function settle() {
  for (let n = 0; n < 100; n++) await Promise.resolve();
}
(async () => {
  const down = pointer('pointerdown', 1, 100, 100);
  pointer('pointerup', 1, 100, 100);
  await settle();
  assert.ok(down.prevented && down.stopped, 'CheerpJ must not also synthesize a touch click');
  assert.deepEqual(calls.map(e => [e.id, e.button]), [[503, 0], [501, 1], [502, 1]]);
  assert.ok(calls[1].frame >= 2, 'Ground tap pressed before scene picking rendered');

  calls.length = 0;
  rect = { left: 13, top: 7, width: 1000, height: 600 };
  pointer('pointerdown', 2, 700, 50);
  pointer('pointerup', 2, 700, 50);
  await settle();
  const click = calls.find(e => e.id === 501);
  assert.ok(Math.abs(click.x - 700) <= 1 && Math.abs(click.y - 50) <= 1,
      'Letterboxed display coordinates were mapped incorrectly');
  rect = { left: 0, top: 0, width: 765, height: 503 };

  calls.length = 0;
  pointer('pointerdown', 3, 100, 100);
  now += 30;
  pointer('pointermove', 3, 140, 120);
  pointer('pointerup', 3, 140, 120);
  await settle();
  assert.ok(calls.some(e => e.id === 506 && e.button === 2), 'World drag did not rotate');
  assert.ok(!calls.some(e => e.id === 501 && e.button === 1), 'Camera drag generated a walk');

  calls.length = 0;
  pointer('pointerdown', 4, 100, 100);
  pointer('pointerdown', 5, 200, 100);
  pointer('pointermove', 5, 248, 100);
  pointer('pointermove', 5, 176, 100);
  pointer('pointerup', 5, 176, 100);
  pointer('pointerup', 4, 100, 100);
  await settle();
  assert.deepEqual(calls.filter(e => e.zoom !== undefined).map(e => e.zoom), [-4, 6],
      'Pinching apart/together did not zoom in/out');
  assert.ok(!calls.some(e => e.id === 501), 'Pinch generated an unintended click');

  calls.length = 0;
  pointer('pointerdown', 6, 650, 400);
  now += 30;
  pointer('pointermove', 6, 680, 410);
  pointer('pointerup', 6, 680, 410);
  await settle();
  assert.ok(calls.some(e => e.id === 506 && e.button === 1), 'Inventory drag was lost');
  assert.ok(!calls.some(e => e.button === 2), 'Inventory drag rotated the camera');

  calls.length = 0;
  pointer('pointerdown', 7, 100, 100);
  pointer('pointerup', 7, 100, 100);
  pointer('pointerdown', 8, 100, 100);
  pointer('pointercancel', 8, 100, 100);
  await settle();
  assert.ok(!calls.some(e => e.id === 501), 'Cancelled tap was delivered late');

  calls.length = 0;
  pointer('pointerdown', 9, 100, 100);
  pointer('pointermove', 9, 140, 120);
  pointer('pointercancel', 9, 140, 120);
  await settle();
  assert.ok(calls.some(e => e.id === 502 && e.button === 2), 'Cancelled drag left camera pressed');

  const mouse = pointer('pointerdown', 10, 100, 100, 'mouse');
  assert.ok(!mouse.prevented, 'Desktop mouse input should pass through');
  console.log('Local renderer touch gesture checks passed.');
})().catch(error => { console.error(error); process.exitCode = 1; });
