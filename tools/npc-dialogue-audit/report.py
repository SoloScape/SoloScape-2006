"""Join offline routing results to the pinned preservation source; never marks text verified."""
from pathlib import Path
import argparse,csv,re,collections

parser=argparse.ArgumentParser(description=__doc__)
parser.add_argument('--reference',type=Path,default=Path('qa-output/dialogue-reference'))
parser.add_argument('--before',type=Path,default=Path('qa-output/npc-dialogue-before.tsv'))
parser.add_argument('--after',type=Path,default=Path('qa-output/npc-dialogue-audit.tsv'))
parser.add_argument('--output',type=Path,default=Path('Server/docs/npc-dialogue-coverage.tsv'))
args=parser.parse_args()
pack={v:int(k) for k,v in (line.split('=',1) for line in (args.reference/'pack/npc.pack').read_text().splitlines() if '=' in line)}
names={id:name for name,id in pack.items()}
sources={}
for path in (args.reference/'scripts').rglob('*.rs2'):
    for name in re.findall(r'^\[opnpc1,([^\]]+)\]',path.read_text(encoding='utf8'),re.M):
        if name in pack:sources[pack[name]]=str(path.relative_to(args.reference)).replace('\\','/')
programs=set()
for line in Path('Server/data/content/npcDialogues2006.tsv').read_text(encoding='utf8').splitlines():
    if not line.startswith('#') and '\t-1\tSTART\t' in line:programs.add(int(line.split('\t')[0]))
before={r['npc_id']:r for r in csv.DictReader(args.before.open(),delimiter='\t')} if args.before.exists() else {}
after=list(csv.DictReader(args.after.open(),delimiter='\t'))
args.output.parent.mkdir(parents=True,exist_ok=True)
with args.output.open('w',encoding='utf8',newline='') as file:
    writer=csv.writer(file,delimiter='\t',lineterminator='\n')
    writer.writerow(['npc_id','name','spawned','talk_slots','before_entry','after_entry','shop_fallback','imported_program',
                     'may_2006_symbol','reference_script','audit_scope','detail'])
    for row in after:
        id=int(row['npc_id']);scope='entry only; full transcript and quest states unverified'
        if row['entry_status']=='missing' and row['shop_fallback']=='true':scope='existing generic shop dialogue; specific transcript unverified'
        if row['talk_slots']!='1':scope='secondary Talk-to option; first-click dialogue probe only'
        if row['entry_status']=='error':scope='requires world/owned NPC context; offline fixture exception'
        writer.writerow([id,row['name'],row['spawned'],row['talk_slots'],before.get(row['npc_id'],{}).get('entry_status','not measured'),
                         row['entry_status'],row['shop_fallback'],id in programs,names.get(id,''),sources.get(id,''),scope,row['detail']])
with args.output.with_name('npc-dialogue-placeholders.tsv').open('w',encoding='utf8',newline='') as file:
    writer=csv.writer(file,delimiter='\t',lineterminator='\n');writer.writerow(['file','line','source'])
    paths=list(Path('Server/src/main/java/com/rs2/model/quest/impl').glob('*.java'))
    paths+=list(Path('Server/src/main/java/com/rs2/model/dialogue').glob('*.java'))
    for path in sorted(paths):
        for line,text in enumerate(path.read_text(encoding='utf8').splitlines(),1):
            if 'This option is currently missing' in text:writer.writerow([str(path).replace('\\','/'),line,text.strip()])
spawned=[r for r in after if r['spawned']=='true']
print('All Talk-to definitions:',len(after),'spawned:',len(spawned),collections.Counter(r['entry_status'] for r in spawned))
print('Spawned missing without generic shop fallback:',sum(r['entry_status']=='missing' and r['shop_fallback']=='false' for r in spawned))
print('Imported programs:',len(programs),'report:',args.output)
