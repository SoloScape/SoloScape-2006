"""Audit exact catalogue IDs against Weird Gloop's earliest RS price records.

Does not change the live catalogue. Results are cached so interrupted audits
can resume without fetching the same item history again.
"""
import concurrent.futures
import csv
import datetime as dt
import http.client
import hashlib
import json
from pathlib import Path
import threading
import time

ROOT = Path(__file__).resolve().parents[2]
CATALOGUE = ROOT / "Server/data/content/grandExchangeItems2006.tsv"
OUTPUT = ROOT / "Server/qa-output/ge-historical-prices"
CACHE = OUTPUT / "cache"
CUTOFF = "2008-12-31"
LOCAL = threading.local()


def fetch(row):
    item_id, old_price, name = row
    cache_file = CACHE / f"{item_id}.json"
    if cache_file.exists():
        result = json.loads(cache_file.read_text(encoding="utf-8"))
    else:
        path = f"/exchange/history/rs/all?id={item_id}&compress=true"
        url = "https://api.weirdgloop.org" + path
        for attempt in range(4):
            try:
                if not getattr(LOCAL, "connection", None):
                    LOCAL.connection = http.client.HTTPSConnection("api.weirdgloop.org", timeout=45)
                LOCAL.connection.request("GET", path, headers={
                    "User-Agent": "SoloScape-2006 historical GE catalogue audit"})
                response = LOCAL.connection.getresponse()
                body = response.read()
                if response.status != 200:
                    raise ValueError(f"HTTP {response.status}")
                data = json.loads(body)
                records = [{"id": str(item_id), "timestamp": r[0], "price": r[1]}
                           for r in data.get(str(item_id), [])]
                valid = [r for r in records if isinstance(r.get("price"), int)
                         and 0 < r["price"] <= 2147483647
                         and str(r.get("id")) == str(item_id)]
                if valid:
                    first = min(valid, key=lambda r: r["timestamp"])
                    date = dt.datetime.fromtimestamp(first["timestamp"] / 1000,
                                                     dt.timezone.utc).date().isoformat()
                    result = {"status": "historical" if date <= CUTOFF else "later_history",
                              "date": date, "price": first["price"],
                              "records": len(valid), "source": url}
                elif data.get("error") == "No results returned" or str(item_id) in data:
                    result = {"status": "no_history", "source": url}
                else:
                    raise ValueError(f"Unexpected API response: {str(data)[:200]}")
                cache_file.write_text(json.dumps(result), encoding="utf-8")
                break
            except Exception as exc:
                if getattr(LOCAL, "connection", None):
                    LOCAL.connection.close()
                    LOCAL.connection = None
                if attempt == 3:
                    result = {"status": "request_error", "error": str(exc), "source": url}
                else:
                    time.sleep(2 ** attempt)
    return {"id": item_id, "name": name, "current_price": old_price, **result}


def main():
    CACHE.mkdir(parents=True, exist_ok=True)
    rows = []
    for line in CATALOGUE.read_text(encoding="utf-8").splitlines():
        if line and not line.startswith("#"):
            item_id, price, name = line.split("\t", 2)
            rows.append((int(item_id), int(price), name))
    assert len({r[0] for r in rows}) == len(rows)
    results = []
    with concurrent.futures.ThreadPoolExecutor(max_workers=8) as pool:
        futures = [pool.submit(fetch, row) for row in rows]
        for future in concurrent.futures.as_completed(futures):
            results.append(future.result())
            if len(results) % 100 == 0:
                print(f"Audited {len(results)}/{len(rows)} items", flush=True)
    results.sort(key=lambda r: r["id"])
    fields = ["id", "name", "current_price", "status", "date", "price", "records", "source", "error"]
    with (OUTPUT / "catalogue-price-audit.csv").open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=fields)
        writer.writeheader()
        writer.writerows(results)
    historical = [r for r in results if r["status"] == "historical"]
    with (OUTPUT / "verified-historical-prices.tsv").open("w", encoding="utf-8", newline="") as stream:
        writer = csv.writer(stream, delimiter="\t")
        writer.writerow(["id", "historical_price", "name", "date", "source"])
        writer.writerows([r["id"], r["price"], r["name"], r["date"], r["source"]]
                         for r in historical)
    with (OUTPUT / "unresolved-items.csv").open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=fields)
        writer.writeheader()
        writer.writerows(r for r in results if r["status"] != "historical")
    counts = {status: sum(r["status"] == status for r in results)
              for status in sorted({r["status"] for r in results})}
    summary = {"total": len(rows), "historical_cutoff": CUTOFF, "counts": counts,
               "catalogue_sha256": hashlib.sha256(CATALOGUE.read_bytes()).hexdigest(),
               "historical_prices_differ": sum(r["price"] != r["current_price"] for r in historical),
               "catalogue_changed": False}
    (OUTPUT / "summary.json").write_text(json.dumps(summary, indent=2), encoding="utf-8")
    print(json.dumps(summary, indent=2), flush=True)


if __name__ == "__main__":
    main()
