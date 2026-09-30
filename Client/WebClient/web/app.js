(() => {
  'use strict';

  const WIDTH = 765;
  const HEIGHT = 503;
  const canvas = document.getElementById('game');
  const ctx = canvas.getContext('2d', { alpha: false });
  const status = document.getElementById('status');
  const keyboardButton = document.getElementById('keyboard');
  const softInput = document.getElementById('softInput');
  const wsUrl = `${location.protocol === 'https:' ? 'wss' : 'ws'}://${location.host}/client`;

  let socket = null;
  let frameSeen = false;
  let decoding = false;
  let queuedFrame = null;
  let pointer = null;
  let lastMoveSent = 0;

  function setStatus(text, hide = false) {
    status.textContent = text;
    status.classList.toggle('hidden', hide);
  }

  function connect() {
    setStatus('Connecting to Java client…');
    socket = new WebSocket(wsUrl);
    socket.binaryType = 'arraybuffer';
    socket.addEventListener('open', () => setStatus('Connected · waiting for Java client frame…'));
    socket.addEventListener('message', (event) => queueFrame(event.data));
    socket.addEventListener('close', () => {
      setStatus('Java client disconnected · reconnecting…');
      setTimeout(connect, 1000);
    });
    socket.addEventListener('error', () => setStatus('Unable to connect to Java client'));
  }

  function queueFrame(arrayBuffer) {
    queuedFrame = arrayBuffer;
    if (!decoding) decodeNextFrame();
  }

  async function decodeNextFrame() {
    if (!queuedFrame) return;
    decoding = true;
    const bytes = queuedFrame;
    queuedFrame = null;
    try {
      const blob = new Blob([bytes], { type: 'image/png' });
      if ('createImageBitmap' in window) {
        const bitmap = await createImageBitmap(blob);
        ctx.drawImage(bitmap, 0, 0, WIDTH, HEIGHT);
        bitmap.close?.();
      } else {
        await drawBlobWithImage(blob);
      }
      if (!frameSeen) {
        frameSeen = true;
        setStatus('Java client live', true);
      }
    } catch (error) {
      console.error(error);
    } finally {
      decoding = false;
      if (queuedFrame) decodeNextFrame();
    }
  }

  function drawBlobWithImage(blob) {
    return new Promise((resolve, reject) => {
      const url = URL.createObjectURL(blob);
      const image = new Image();
      image.onload = () => {
        ctx.drawImage(image, 0, 0, WIDTH, HEIGHT);
        URL.revokeObjectURL(url);
        resolve();
      };
      image.onerror = () => {
        URL.revokeObjectURL(url);
        reject(new Error('Unable to decode Java client frame.'));
      };
      image.src = url;
    });
  }

  function send(bytes) {
    if (socket && socket.readyState === WebSocket.OPEN) socket.send(Uint8Array.from(bytes));
  }

  function u16(value) {
    const v = Math.max(0, Math.min(65535, Math.round(value)));
    return [v >>> 8 & 255, v & 255];
  }

  function gamePoint(event) {
    const rect = canvas.getBoundingClientRect();
    const scale = Math.min(rect.width / WIDTH, rect.height / HEIGHT);
    const drawnWidth = WIDTH * scale;
    const drawnHeight = HEIGHT * scale;
    const left = rect.left + (rect.width - drawnWidth) / 2;
    const top = rect.top + (rect.height - drawnHeight) / 2;
    return {
      x: Math.max(0, Math.min(WIDTH - 1, (event.clientX - left) / scale)),
      y: Math.max(0, Math.min(HEIGHT - 1, (event.clientY - top) / scale))
    };
  }

  function mouseMove(x, y) {
    send([1, ...u16(x), ...u16(y)]);
  }

  function mouseDown(button, x, y) {
    send([2, button, ...u16(x), ...u16(y)]);
  }

  function mouseUp(button, x, y) {
    send([3, button, ...u16(x), ...u16(y)]);
  }

  function click(button, x, y) {
    mouseMove(x, y);
    mouseDown(button, x, y);
    setTimeout(() => mouseUp(button, x, y), 18);
  }

  canvas.addEventListener('pointerdown', (event) => {
    event.preventDefault();
    const point = gamePoint(event);
    canvas.setPointerCapture?.(event.pointerId);
    mouseMove(point.x, point.y);

    if (event.pointerType === 'mouse') {
      const button = event.button === 2 ? 3 : event.button === 1 ? 2 : 1;
      mouseDown(button, point.x, point.y);
      pointer = { id: event.pointerId, type: 'mouse', button, x: point.x, y: point.y, down: true };
      return;
    }

    pointer = {
      id: event.pointerId,
      type: 'touch',
      startX: point.x,
      startY: point.y,
      x: point.x,
      y: point.y,
      started: performance.now(),
      dragging: false,
      down: false
    };
  }, { passive: false });

  canvas.addEventListener('pointermove', (event) => {
    if (!pointer || pointer.id !== event.pointerId) return;
    event.preventDefault();
    const point = gamePoint(event);
    pointer.x = point.x;
    pointer.y = point.y;
    const now = performance.now();
    if (now - lastMoveSent > 20) {
      mouseMove(point.x, point.y);
      lastMoveSent = now;
    }
    if (pointer.type === 'touch' && !pointer.dragging) {
      const distance = Math.hypot(point.x - pointer.startX, point.y - pointer.startY);
      if (distance > 7) {
        pointer.dragging = true;
        pointer.down = true;
        mouseDown(1, pointer.startX, pointer.startY);
      }
    }
  }, { passive: false });

  canvas.addEventListener('pointerup', (event) => {
    if (!pointer || pointer.id !== event.pointerId) return;
    event.preventDefault();
    const current = pointer;
    pointer = null;
    const point = gamePoint(event);

    if (current.type === 'mouse') {
      if (current.down) mouseUp(current.button, point.x, point.y);
      return;
    }

    if (current.dragging) {
      mouseUp(1, point.x, point.y);
      return;
    }

    const held = performance.now() - current.started;
    click(held >= 450 ? 3 : 1, point.x, point.y);
  }, { passive: false });

  canvas.addEventListener('pointercancel', (event) => {
    if (!pointer || pointer.id !== event.pointerId) return;
    const current = pointer;
    pointer = null;
    if (current.down) mouseUp(current.button || 1, current.x, current.y);
  });

  canvas.addEventListener('contextmenu', (event) => {
    event.preventDefault();
    const point = gamePoint(event);
    click(3, point.x, point.y);
  });

  function sendKey(type, code, character = 0) {
    send([type, ...u16(code), ...u16(character)]);
  }

  function sendKeyPair(code, character = 0) {
    sendKey(4, code, character);
    setTimeout(() => sendKey(5, code, character), 12);
  }

  function javaKeyCodeFromChar(character) {
    const code = character.charCodeAt(0);
    if (code >= 97 && code <= 122) return code - 32;
    if (code >= 65 && code <= 90) return code;
    if (code >= 48 && code <= 57) return code;
    const map = {
      ' ': 32, ',': 44, '-': 45, '.': 46, '/': 47, ';': 59, '=': 61,
      '[': 91, '\\': 92, ']': 93, '`': 192, "'": 222
    };
    return map[character] || 32;
  }

  function javaCodeFromKeyboardEvent(event) {
    const named = {
      Backspace: 8, Tab: 9, Enter: 10, Shift: 16, Control: 17, Alt: 18,
      Escape: 27, ' ': 32, PageUp: 33, PageDown: 34, End: 35, Home: 36,
      ArrowLeft: 37, ArrowUp: 38, ArrowRight: 39, ArrowDown: 40,
      Delete: 127
    };
    if (named[event.key] !== undefined) return named[event.key];
    if (event.key && event.key.length === 1) return javaKeyCodeFromChar(event.key);
    return event.keyCode || event.which || 0;
  }

  window.addEventListener('keydown', (event) => {
    if (document.activeElement === softInput) return;
    const code = javaCodeFromKeyboardEvent(event);
    const character = event.key && event.key.length === 1 ? event.key.charCodeAt(0) : 0;
    if (code) sendKey(4, code, character);
  });

  window.addEventListener('keyup', (event) => {
    if (document.activeElement === softInput) return;
    const code = javaCodeFromKeyboardEvent(event);
    const character = event.key && event.key.length === 1 ? event.key.charCodeAt(0) : 0;
    if (code) sendKey(5, code, character);
  });

  keyboardButton.addEventListener('click', () => {
    softInput.value = '';
    softInput.focus({ preventScroll: true });
  });

  softInput.addEventListener('beforeinput', (event) => {
    if (event.inputType === 'deleteContentBackward') {
      event.preventDefault();
      sendKeyPair(8, 8);
      softInput.value = '';
      return;
    }
    if (event.inputType === 'insertText' && event.data) {
      event.preventDefault();
      for (const character of event.data) {
        sendKeyPair(javaKeyCodeFromChar(character), character.charCodeAt(0));
      }
      softInput.value = '';
    }
  });

  softInput.addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
      event.preventDefault();
      sendKeyPair(10, 10);
      softInput.value = '';
    } else if (event.key.startsWith('Arrow')) {
      event.preventDefault();
      sendKeyPair(javaCodeFromKeyboardEvent(event), 0);
    }
  });

  ctx.fillStyle = '#000';
  ctx.fillRect(0, 0, WIDTH, HEIGHT);
  connect();

  if ('serviceWorker' in navigator && location.protocol !== 'file:') {
    addEventListener('load', () => navigator.serviceWorker.register('/sw.js').catch(() => {}));
  }
})();
