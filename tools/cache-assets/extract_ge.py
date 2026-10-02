"""Extract revision 530 GE sprite assets without replacing the game's 443 cache.

Uses only Python's standard library. Run with the OpenRS2 cache ZIP path.
"""
import argparse
import bz2
import gzip
import json
import struct
import zipfile
import zlib
from pathlib import Path


class Reader:
    def __init__(self, data):
        self.data, self.pos = data, 0

    def read(self, fmt):
        result = struct.unpack_from('>' + fmt, self.data, self.pos)
        self.pos += struct.calcsize('>' + fmt)
        return result[0] if len(result) == 1 else result

    def string(self):
        end = self.data.index(0, self.pos)
        result = self.data[self.pos:end].decode('cp1252')
        self.pos = end + 1
        return result


class Cache:
    def __init__(self, path):
        with zipfile.ZipFile(path) as archive:
            self.dat = archive.read('cache/main_file_cache.dat2')
            self.indices = {i: archive.read('cache/main_file_cache.idx' + str(i))
                            for i in (3, 8, 255)}
        self.tables = {}

    def group(self, index, group):
        entry = self.indices[index][group * 6:group * 6 + 6]
        length, sector = int.from_bytes(entry[:3], 'big'), int.from_bytes(entry[3:], 'big')
        output = bytearray()
        chunk = 0
        while len(output) < length:
            start = sector * 520
            stored_group, stored_chunk = struct.unpack_from('>HH', self.dat, start)
            next_sector = int.from_bytes(self.dat[start + 4:start + 7], 'big')
            assert (stored_group, stored_chunk, self.dat[start + 7]) == (group, chunk, index)
            output.extend(self.dat[start + 8:start + 8 + min(512, length - len(output))])
            sector, chunk = next_sector, chunk + 1
        kind, size = struct.unpack_from('>BI', output)
        if kind == 0:
            return bytes(output[5:5 + size])
        expected = struct.unpack_from('>I', output, 5)[0]
        compressed = bytes(output[9:9 + size])
        result = bz2.decompress(b'BZh1' + compressed) if kind == 1 else gzip.decompress(compressed)
        assert len(result) == expected
        return result

    def table(self, index):
        if index in self.tables:
            return self.tables[index]
        r = Reader(self.group(255, index))
        protocol = r.read('B')
        assert protocol in (5, 6)
        if protocol == 6:
            r.read('I')
        flags, count = r.read('B'), r.read('H')
        ids, value = [], 0
        for _ in range(count):
            value += r.read('H')
            ids.append(value)
        names = dict(zip(ids, [r.read('i') for _ in ids])) if flags & 1 else {}
        r.pos += count * 8
        counts = [r.read('H') for _ in ids]
        files = {}
        for group, n in zip(ids, counts):
            values, value = [], 0
            for _ in range(n):
                value += r.read('H')
                values.append(value)
            files[group] = values
        self.tables[index] = files, names
        return files, names

    def files(self, index, group):
        ids = self.table(index)[0][group]
        data = self.group(index, group)
        if len(ids) == 1:
            return {ids[0]: data}
        chunks = data[-1]
        r = Reader(data)
        r.pos = len(data) - 1 - chunks * len(ids) * 4
        outputs = [bytearray() for _ in ids]
        pos = 0
        for _ in range(chunks):
            size = 0
            for out in outputs:
                size += r.read('i')
                out.extend(data[pos:pos + size])
                pos += size
        return dict(zip(ids, map(bytes, outputs)))


def component(data):
    r = Reader(data)
    if r.read('B') != 255:
        return None
    kind = r.read('B')
    r.read('H')
    x, y, width, height = r.read('hhHH')
    r.pos += 4
    parent, hidden = r.read('H'), r.read('B')
    result = dict(type=kind, x=x, y=y, width=width, height=height,
                  parent=parent, hidden=bool(hidden))
    if kind == 5:
        result['sprite'] = r.read('i')
        result['angle'] = r.read('H')
        result['tiled'] = bool(r.read('B'))
    if kind == 4:
        result['font'] = r.read('H')
        result['text'] = r.string()
    return result


def sprites(data):
    n = struct.unpack_from('>H', data, len(data) - 2)[0]
    r = Reader(data)
    r.pos = len(data) - 7 - n * 8
    full_w, full_h, colours = r.read('H'), r.read('H'), r.read('B') + 1
    xs = [r.read('H') for _ in range(n)]
    ys = [r.read('H') for _ in range(n)]
    widths = [r.read('H') for _ in range(n)]
    heights = [r.read('H') for _ in range(n)]
    palette_start = len(data) - 7 - n * 8 - (colours - 1) * 3
    palette = [0] + [int.from_bytes(data[p:p + 3], 'big') or 1
                     for p in range(palette_start, palette_start + (colours - 1) * 3, 3)]
    r.pos = 0
    result = []
    for x, y, w, h in zip(xs, ys, widths, heights):
        flags = r.read('B')
        indices = data[r.pos:r.pos + w * h]
        r.pos += w * h
        alphas = None
        if flags & 2:
            alphas = data[r.pos:r.pos + w * h]
            r.pos += w * h
        rgba = bytearray(full_w * full_h * 4)
        for row in range(h):
            for col in range(w):
                source = col * h + row if flags & 1 else row * w + col
                index = indices[source]
                colour = palette[index]
                alpha = alphas[source] if alphas is not None else (255 if index else 0)
                dest = ((row + y) * full_w + col + x) * 4
                rgba[dest:dest + 4] = bytes((colour >> 16, colour >> 8 & 255, colour & 255, alpha))
        result.append((full_w, full_h, rgba))
    return result


def png(path, w, h, rgba):
    def chunk(kind, data):
        return struct.pack('>I', len(data)) + kind + data + struct.pack('>I', zlib.crc32(kind + data))
    scanlines = b''.join(b'\0' + rgba[y * w * 4:(y + 1) * w * 4] for y in range(h))
    path.write_bytes(b'\x89PNG\r\n\x1a\n' + chunk(b'IHDR', struct.pack('>IIBBBBB', w, h, 8, 6, 0, 0, 0))
                     + chunk(b'IDAT', zlib.compress(scanlines)) + chunk(b'IEND', b''))


def blit(target, tw, source, x, y, width=None, height=None, tiled=False):
    sw, sh, pixels = source
    width, height = width or sw, height or sh
    for row in range(height):
        for col in range(width):
            sy = row % sh if tiled else row * sh // height
            sx = col % sw if tiled else col * sw // width
            src = (sy * sw + sx) * 4
            dx, dy = x + col, y + row
            if dx < 0 or dy < 0 or dx >= tw or (dy * tw + dx) * 4 >= len(target):
                continue
            alpha = pixels[src + 3]
            if alpha:
                dest = (dy * tw + dx) * 4
                for k in range(3):
                    target[dest + k] = (pixels[src + k] * alpha + target[dest + k] * (255 - alpha)) // 255
                target[dest + 3] = max(target[dest + 3], alpha)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('cache_zip')
    parser.add_argument('--probe', action='store_true')
    parser.add_argument('--output', default='Client/src/assets/grand-exchange')
    args = parser.parse_args()
    cache = Cache(args.cache_zip)
    output = Path(args.output)
    output.mkdir(parents=True, exist_ok=True)
    layouts, sprite_ids = {}, set()
    for group in (105, 106, 108, 110, 389):
        layout = {fid: component(data) for fid, data in cache.files(3, group).items()}
        layouts[group] = layout
        sprite_ids.update(c['sprite'] for c in layout.values() if c and c['type'] == 5 and c['sprite'] >= 0)
    # Script 680 supplies the original confirm-button border; 1017 is the
    # revision-530 opaque chat parchment used behind the search interface.
    sprite_ids.update(range(913, 921))
    sprite_ids.add(1017)
    decoded = {}
    for sprite in sorted(sprite_ids):
        decoded[sprite] = sprites(cache.files(8, sprite)[0])
        for frame, (w, h, rgba) in enumerate(decoded[sprite]):
            png(output / ('%d-%d.png' % (sprite, frame)), w, h, rgba)
            print('Sprite', sprite, frame, w, h)
    (output / 'layouts.json').write_text(json.dumps(layouts, indent=2), encoding='utf8')
    def border(target, width, height):
        for sprite, x, y, w, h in (
                (913, 0, 0, 9, 9), (914, width - 9, 0, 9, 9),
                (915, 0, height - 9, 9, 9), (916, width - 9, height - 9, 9, 9),
                (917, 0, 9, 9, height - 18), (918, 9, 0, width - 18, 9),
                (919, width - 9, 9, 9, height - 18), (920, 9, height - 9, width - 18, 9)):
            blit(target, width, decoded[sprite][0], x, y, w, h, True)

    confirm = bytearray(120 * 43 * 4)
    blit(confirm, 120, decoded[297][0], 0, 0, 120, 43, True)
    border(confirm, 120, 43)
    png(output / 'confirm.png', 120, 43, confirm)

    search = bytearray(479 * 96 * 4)
    blit(search, 479, decoded[1017][0], 0, 0, 479, 96, False)
    blit(search, 479, decoded[1075][0], 46, 3, 32, 74, True)
    blit(search, 479, decoded[851][0], 0, 61, 479, 32, True)
    border(search, 479, 96)
    png(output / 'search.png', 479, 96, search)

    frame = bytearray(512 * 334 * 4)
    for fid in range(1, 13):
        c = layouts[105][fid]
        blit(frame, 512, decoded[c['sprite']][0], c['x'], c['y'], c['width'], c['height'], c['tiled'])
    png(output / 'frame.png', 512, 334, frame)
    def draw_component(target, fid):
        c = layouts[105][fid]
        blit(target, 512, decoded[c['sprite']][0], c['x'], c['y'], c['width'], c['height'], c['tiled'])

    def outline(target, x, y, w, h):
        blit(target, 512, decoded[1074][0], x, y - 16, w, 32, True)
        blit(target, 512, decoded[1074][0], x, y + h - 17, w, 32, True)
        blit(target, 512, decoded[1075][0], x - 16, y, 32, h, True)
        blit(target, 512, decoded[1075][0], x + w - 17, y, 32, h, True)

    overview = frame.copy()
    empty_slot = bytearray(140 * 110 * 4)
    for fid in range(20, 28):
        c = layouts[105][fid]
        blit(empty_slot, 140, decoded[c['sprite']][0], c['x'], c['y'], c['width'], c['height'], c['tiled'])
    png(output / 'empty-slot.png', 140, 110, empty_slot)
    for slot in range(6):
        x, y = 30 + slot % 3 * 156, 80 + slot // 3 * 120
        outline(overview, x, y, 140, 110)
        blit(overview, 512, decoded[1074][0], x, y + 22 - 16, 140, 32, True)
    png(output / 'overview.png', 512, 334, overview)

    def progress_track(target, stride, x, y, width, height):
        # The offer bars are square, one-pixel framed rectangles, rather than
        # the rounded quantity/price field artwork.
        for row in range(height):
            for col in range(width):
                color = (0, 0, 0) if row in (0, height - 1) or col in (0, width - 1) else (65, 57, 46)
                pos = ((y + row) * stride + x + col) * 4
                target[pos:pos + 4] = bytes((*color, 255))

    active_slot = bytearray(140 * 110 * 4)
    blit(active_slot, 140, decoded[1137][0], 7, 27, 40, 36, False)
    progress_track(active_slot, 140, 6, 80, 128, 15)
    png(output / 'active-slot.png', 140, 110, active_slot)

    editor = frame.copy()
    outline(editor, 43, 60, 426, 90)
    outline(editor, 43, 152, 426, 84)
    for fid in (128, 130, 132, 135, 136, 147, 148, 149, 152, 153, 154, 186, 187, 188):
        draw_component(editor, fid)
    png(output / 'editor.png', 512, 334, editor)
    status = editor.copy()
    for fid in (207, 204, 205, 206, 208, 210):
        draw_component(status, fid)
    progress_track(status, 512, 70, 299, 300, 15)
    png(output / 'status.png', 512, 334, status)
    if not args.probe:
        return
    for group in cache.table(3)[0]:
        files = cache.files(3, group)
        matches = [(fid, data) for fid, data in files.items()
                   if b'Grand Exchange' in data or b'Search for' in data]
        if matches:
            print('Interface', group, 'components', len(files))
            for fid, data in matches:
                print(fid, repr(data))
    for group, layout in layouts.items():
        for fid, c in layout.items():
            if c and (c['type'] in (4, 5)):
                print(group, fid, c)


if __name__ == '__main__':
    main()
