'use strict';

const assert = require('node:assert/strict');
const test = require('node:test');
const { createFormatter } = require('./message-format');

const woodcutting = '<:woodcutting:123456789012345678>';
const dragon = '<:kbd:123456789012345679>';
const levelUp = { type: 'level_up', player: 'Gradwahl', server: 'Callum', skill: 'Woodcutting', level: 6 };
const bossKill = { type: 'boss_kill', player: 'Gradwahl', server: 'Callum', boss: 'King Black Dragon', npcId: 50 };

test('uses configured skill emoji inline and preserves message text', () => {
  const format = createFormatter(JSON.stringify({ skills: { ' woodcutting ': woodcutting } }));
  assert.equal(format(levelUp), `${woodcutting} **Callum** reached **6 Woodcutting** on **Gradwahl**!`);
  assert.match(format({ ...levelUp, skill: 'Fishing' }), /^🎉 /);
});

test('matches boss names and prefers NPC ID mapping', () => {
  const format = createFormatter(JSON.stringify({ bosses: { 'king black dragon': dragon, 50: woodcutting } }));
  assert.match(format(bossKill), new RegExp(`^${woodcutting} `));
  assert.match(format({ ...bossKill, npcId: 51 }), new RegExp(`^${dragon} `));
  assert.match(format({ ...bossKill, boss: 'Unknown', npcId: 52 }), /^☠️ /);
});

test('keeps defaults without configuration and escapes player-supplied emoji markup', () => {
  const format = createFormatter();
  assert.equal(format(levelUp), '🎉 **Callum** reached **6 Woodcutting** on **Gradwahl**!');
  assert.match(format(bossKill), /^☠️ /);
  const configured = createFormatter(JSON.stringify({ skills: { woodcutting } }));
  const message = configured({ ...levelUp, player: '<:fake:123456789012345678>', server: '@everyone' });
  assert.ok(message.startsWith(`${woodcutting} `));
  assert.ok(message.includes('\\<:fake:123456789012345678\\>'));
});

test('rejects malformed mapping and arbitrary message injection', () => {
  for (const raw of ['broken', 'null', '[]', '{"skills":[]}', '{"items":{}}', '{"skills":{"woodcutting":"@everyone"}}']) {
    assert.throws(() => createFormatter(raw));
  }
  assert.doesNotThrow(() => createFormatter('{"skills":{"woodcutting":"<a:woodcutting:123456789012345678>"}}'));
});

test('rejects invalid events even with icons configured', () => {
  const format = createFormatter(JSON.stringify({ skills: { woodcutting } }));
  for (const event of [null, [], {}, { ...levelUp, level: 0 }, { ...levelUp, player: '' }, { ...bossKill, npcId: -1 }]) {
    assert.equal(format(event), null);
  }
});
