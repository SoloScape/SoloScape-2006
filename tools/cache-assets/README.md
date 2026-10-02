# Grand Exchange artwork

The bundled PNGs in `Client/src/assets/grand-exchange` come from OpenRS2 cache
254: RuneScape English revision 530, dated 2009-02-01. The client continues to
use its revision-443 cache; only the exchange artwork is bundled separately.

From the repository root, regenerate the assets with Python 3 (standard library
only):

```powershell
python tools/cache-assets/extract_ge.py 'C:\Users\Callum\Downloads\cache-runescape-live-en-b530-2009-02-01-00-00-00-openrs2#254.zip'
```

The extractor reads JS5 interface groups 105, 106, 108, 110 and 389 and their
referenced sprites. `layouts.json` preserves the decoded component geometry.
The composed backgrounds use the original frame, borders and field artwork.
The item search is adapted to the revision-443 client's 479 by 96 chatbox.
Pass `--probe` to print the decoded interface components.

Historical visual references:

- https://runescape.salmoneus.net/tips/grand-exchange.html (user-selected reference for button, search, and progress bars)

- https://www.2011.rs/img/main/kbase/controls/grand_exchange/exchange_summary_intro.jpg
- https://www.2011.rs/img/main/kbase/controls/grand_exchange/offer_interface.jpg
- https://www.2011.rs/img/main/kbase/controls/grand_exchange/item_list.jpg

Both client build scripts copy the assets into the client JAR. The
`GrandExchangeCompatibilityChecks` harness exercises the paired server and
client packet handlers, checks chatbox search input and selection, and renders
the screens into `Server/qa-output/grand-exchange` for inspection.
