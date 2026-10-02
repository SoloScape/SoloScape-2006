"""Extract descriptions from the pinned May 2006 Lost City content configs."""
import argparse
from pathlib import Path
import subprocess

PIN = '4949e619a53f40c0fe83395df89bd35ff1f044f4'
cli = argparse.ArgumentParser(description=__doc__)
cli.add_argument('--reference', type=Path, default=Path('qa-output/dialogue-reference'))
cli.add_argument('--output', type=Path, default=Path('Server/data/content/examines2006.tsv'))
args = cli.parse_args()
if subprocess.check_output(['git', '-C', str(args.reference), 'rev-parse', 'HEAD'], text=True).strip() != PIN:
    raise SystemExit('Reference commit differs from pinned May 2006 content')
rows = []
for extension, kind in [('obj', 'item'), ('npc', 'npc'), ('loc', 'object')]:
    pack = dict(line.split('=', 1)[::-1] for line in
                (args.reference / 'pack' / (extension + '.pack')).read_text().splitlines() if '=' in line)
    configs = {}
    # Reconstructed configs override the older unpacked baseline by symbol.
    files = sorted((args.reference / 'scripts').rglob('*.' + extension),
                   key=lambda p: (0 if '_unpack' in p.parts else 1, p.as_posix()))
    for path in files:
        symbol = None
        for line in path.read_text(encoding='utf-8-sig').splitlines():
            line = line.strip()
            if line.startswith('[') and line.endswith(']'):
                symbol = line[1:-1]
                configs.setdefault(symbol, {})
            elif symbol and '=' in line and not line.startswith('//'):
                key, value = line.split('=', 1)
                if key in ('name', 'desc'):
                    configs[symbol][key] = value
    for symbol, values in configs.items():
        description = values.get('desc', '')
        if symbol in pack and description and description.lower() != 'null':
            rows.append((kind, int(pack[symbol]), values.get('name', ''), description))
args.output.parent.mkdir(parents=True, exist_ok=True)
with args.output.open('w', encoding='utf-8', newline='\n') as output:
    output.write('# LostCityRS/Content ' + PIN + '; kind\tid\tname\texamine\n')
    for row in sorted(rows):
        if any('\t' in str(value) for value in row):
            raise SystemExit('Unexpected tab in config')
        output.write('\t'.join(map(str, row)) + '\n')
for kind in ('item', 'npc', 'object'):
    print(kind, sum(row[0] == kind for row in rows))
