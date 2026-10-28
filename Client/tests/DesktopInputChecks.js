'use strict';
const assert = require('node:assert/strict');
const fs = require('node:fs');
const vm = require('node:vm');
const html = fs.readFileSync('Client/WebClient/web/desktop.html', 'utf8');
const script = html.split('<script>')[1].split('</script>')[0];
new vm.Script(script);
const controls = script.slice(script.indexOf('  // Desktop input controls.'),
    script.indexOf('  // Start runtime'));
assert.ok(controls.includes('setKeyDown'), 'Desktop controls are missing');
const displayHandlers = {}, windowHandlers = {}, calls = [];
let now = 1000;
const display = {
  addEventListener(type, fn) { (displayHandlers[type] ||= []).push(fn); },
  getBoundingClientRect() { return { left: 10, top: 20, width: 1530, height: 1006 }; },
  setPointerCapture() {}
};
vm.runInNewContext(controls + '\nclient = bridge;', {
  document: { getElementById: () => display },
  window: { addEventListener(type, fn) { (windowHandlers[type] ||= []).push(fn); } },
  performance: { now: () => now },
  console,
  bridge: {
    async setKeyDown(code, down) { calls.push({ key: code, down }); },
    async sendMouse(id, button, x, y) { calls.push({ id, button, x, y }); },
    async zoom(steps) { calls.push({ zoom: steps }); }
  }
});
function dispatch(handlers, type, extra = {}) {
  const e = Object.assign({
    pointerType: 'mouse', pointerId: 1, button: 1, clientX: 210, clientY: 220,
    preventDefault() { this.prevented = true; },
    stopImmediatePropagation() { this.stopped = true; }
  }, extra);
  for (const fn of handlers[type] || []) {
    fn(e);
    if (e.stopped) break;
  }
  return e;
}
async function settle() { for (let n = 0; n < 150; n++) await Promise.resolve(); }
(async () => {
  const arrow = dispatch(windowHandlers, 'keydown', { key: 'ArrowLeft' });
  dispatch(windowHandlers, 'keydown', { key: 'ArrowLeft', repeat: true });
  dispatch(windowHandlers, 'keypress', { key: 'ArrowLeft' });
  dispatch(windowHandlers, 'keyup', { key: 'ArrowLeft' });
  await settle();
  assert.ok(arrow.prevented && arrow.stopped, 'Arrow event leaked into CheerpJ text input');
  assert.deepEqual(calls, [{ key: 37, down: true }, { key: 37, down: false }],
      'Held arrows must have one press and one release');
  assert.ok(!dispatch(windowHandlers, 'keydown', { key: 'a' }).prevented,
      'Ordinary chat typing must pass through');

  calls.length = 0;
  dispatch(displayHandlers, 'pointerdown');
  dispatch(displayHandlers, 'pointermove', { clientX: 290, clientY: 260 });
  dispatch(displayHandlers, 'pointerup');
  await settle();
  assert.deepEqual(calls.map(e => [e.id, e.button, e.x, e.y]),
      [[503, 0, 100, 100], [501, 2, 100, 100], [506, 2, 140, 120], [502, 2, 140, 120]],
      'Middle drag did not send scaled camera input');
  assert.ok(dispatch(displayHandlers, 'auxclick').prevented, 'Middle click opened browser autoscroll');
  assert.ok(!dispatch(displayHandlers, 'pointerdown', { button: 0 }).prevented,
      'Normal game clicks must pass through');

  calls.length = 0;
  now += 200;
  dispatch(displayHandlers, 'wheel', { deltaY: 120, deltaMode: 0, ctrlKey: false });
  dispatch(displayHandlers, 'wheel', { deltaY: -60, deltaMode: 0, ctrlKey: false });
  dispatch(displayHandlers, 'wheel', { deltaY: 20, deltaMode: 0, ctrlKey: false });
  dispatch(displayHandlers, 'wheel', { deltaY: 20, deltaMode: 0, ctrlKey: false });
  dispatch(displayHandlers, 'wheel', { deltaY: 20, deltaMode: 0, ctrlKey: false });
  await settle();
  assert.deepEqual(calls.map(e => e.zoom), [2, -1, 1], 'Wheel direction or trackpad accumulation is wrong');
  assert.ok(!dispatch(displayHandlers, 'wheel',
      { deltaY: 120, deltaMode: 0, ctrlKey: false, clientX: 1310, clientY: 820 }).prevented,
      'Inventory wheel scrolling was intercepted');
  assert.ok(!dispatch(displayHandlers, 'wheel',
      { deltaY: 120, deltaMode: 0, ctrlKey: true }).prevented, 'Browser Ctrl+wheel zoom was intercepted');

  calls.length = 0;
  dispatch(windowHandlers, 'keydown', { key: 'ArrowUp' });
  dispatch(displayHandlers, 'pointerdown');
  dispatch(windowHandlers, 'blur');
  await settle();
  assert.ok(calls.some(e => e.key === 38 && !e.down), 'Focus loss left an arrow held');
  assert.ok(calls.some(e => e.id === 502 && e.button === 2), 'Focus loss left camera drag held');
  console.log('Desktop arrow, middle-drag and wheel checks passed.');
})().catch(error => { console.error(error); process.exitCode = 1; });
