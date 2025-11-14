# ShopSense AI Browser Extension

Manifest V3 extension that respects user consent and retailer policies. Extraction occurs only after the shopper clicks the action button or context menu entry.

## Features
- Amazon/Walmart/Best Buy DOM parsers (no network scraping).
- Animated sidebar with real-time AI insights streamed from `/api/v1/insights/analyze`.
- Privacy toggles stored in `chrome.storage.sync`.
- Edge-compatible (Chromium-based browsers).

## Development
1. Enable Developer Mode in Chrome.
2. Click "Load unpacked" and select the `browser-extension` folder.
3. Pin the extension, navigate to a supported retailer page, and click the toolbar icon.

## Compliance Notes
- Displays consent copy before capturing DOM data.
- Sends only the URL, retailer, and structured product payload to the backend.
- Honors user opt-out and deletes cached data locally when disabled.
