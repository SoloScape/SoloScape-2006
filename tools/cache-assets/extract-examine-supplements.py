"""Extract name-matched 2009scape descriptions for gaps in May 2006 content.

Run examine-checks.ps1 first to inventory the actual revision 443 cache.
"""
import csv
from collections import defaultdict
import json
from pathlib import Path
import re
from urllib.parse import quote
from urllib.request import urlopen

PIN = 'c6322d0976986b176691eee9775b596bda071a9f'
ROOT = Path(__file__).resolve().parents[2]
reference = ROOT / 'qa-output/examine-reference'
reference.mkdir(parents=True, exist_ok=True)
base = 'https://gitlab.com/api/v4/projects/2009scape%2F2009scape/repository/files/'

def download(path, local):
    target = reference / local
    target.write_bytes(urlopen(base + quote(path, safe='') + '/raw?ref=' + PIN, timeout=60).read())
    return target

npcs = json.loads(download('Server/data/configs/npc_configs.json', 'npc_configs.json').read_text())
objects = json.loads(download('Server/data/configs/object_configs.json', 'object_configs.json').read_text())
names = {}
current = None
for line in download('dumps/530/config/dump.loc', 'dump.loc').read_text().splitlines():
    match = re.fullmatch(r'\[loc_(\d+)\]', line)
    if match:
        current = int(match[1])
    elif current is not None and line.startswith('name='):
        names[current] = line[5:]
data = {}
for npc in npcs:
    if npc.get('examine'):
        data['npc', int(npc['id'])] = (npc.get('name', ''), npc['examine'])
for obj in objects:
    if obj.get('examine'):
        for id in obj.get('ids', '').split(','):
            if id.strip().isdigit() and int(id) in names:
                data['object', int(id)] = (names[int(id)], obj['examine'])
rows = []
by_name = defaultdict(set)
historical_by_id = {}
for (kind, id), (name, description) in data.items():
    if name and description and description.casefold() not in ('null', 'none'):
        by_name[kind, name.casefold()].add(' '.join(description.split()))
with (ROOT / 'Server/data/content/examines2006.tsv').open(encoding='utf-8') as historical:
    for line in historical:
        if line.startswith('#'):
            continue
        kind, id, name, description = line.rstrip('\n').split('\t')
        historical_by_id[kind, int(id)] = name
        if name:
            by_name[kind, name.casefold()].add(description)
with (ROOT / 'qa-output/examine-checks/coverage.tsv').open(encoding='utf-8') as source:
    for row in csv.DictReader(source, delimiter='\t'):
        if row['kind'] == 'item':
            continue
        original_name = historical_by_id.get((row['kind'], int(row['id'])))
        if original_name is not None and (not original_name or original_name.casefold() == row['name'].casefold()):
            continue
        match = data.get((row['kind'], int(row['id'])))
        if not match or match[0].casefold() != row['name'].casefold():
            variants = by_name.get((row['kind'], row['name'].casefold()), set())
            # Only inherit a description if all source variants agree on the text.
            match = (row['name'], next(iter(variants))) if len(variants) == 1 else None
        if match and match[0].casefold() == row['name'].casefold():
            text = ' '.join(match[1].split())
            if text and text.casefold() not in ('null', 'none'):
                rows.append((row['kind'], int(row['id']), row['name'], text))
output = ROOT / 'Server/data/content/examines2009scape.tsv'
with output.open('w', encoding='utf-8', newline='\n') as target:
    target.write('# 2009scape ' + PIN + '; name-matched supplement; kind\tid\tname\texamine\n')
    for row in sorted(rows):
        target.write('\t'.join(map(str, row)) + '\n')
download('LICENSE', 'LICENSE')
(ROOT / 'Server/data/content/examines2009scape.LICENSE.txt').write_bytes((reference / 'LICENSE').read_bytes())
print('Name-matched supplement rows:', len(rows))
