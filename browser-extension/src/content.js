(() => {
  if (window.ShopSenseContentLoaded) {
    return;
  }
  window.ShopSenseContentLoaded = true;

  const SCRIPT_ID = 'shopsense-ai-sidebar';
  let latestPayload = null;
  let ensureExtractorsPromise = null;

  const hostname = window.location.hostname.toLowerCase();
  const retailers = {
    amazon: hostname.includes('amazon.'),
    walmart: hostname.includes('walmart.'),
    bestbuy: hostname.includes('bestbuy.')
  };

  const ensureExtractors = () => {
    if (window.ShopSenseExtractors) {
      return Promise.resolve();
    }
    if (ensureExtractorsPromise) {
      return ensureExtractorsPromise;
    }
    ensureExtractorsPromise = new Promise((resolve, reject) => {
      const script = document.createElement('script');
      script.src = chrome.runtime.getURL('src/extractors.bundle.js');
      script.onload = () => {
        if (window.ShopSenseExtractors) {
          resolve();
        } else {
          reject(new Error('extractors unavailable'));
        }
      };
      script.onerror = () => reject(new Error('extractors failed to load'));
      document.documentElement.appendChild(script);
    });
    return ensureExtractorsPromise;
  };

  const loadSidebar = async () => {
    const existing = document.getElementById(SCRIPT_ID);
    if (existing) {
      existing.classList.add('visible');
      return existing;
    }
    const frame = document.createElement('iframe');
    frame.id = SCRIPT_ID;
    frame.src = chrome.runtime.getURL('src/sidebar.html');
    frame.style.cssText = 'position:fixed;top:0;right:0;height:100vh;width:420px;border:none;z-index:2147483647;box-shadow:-12px 0 24px rgba(15,23,42,0.15);transform:translateX(420px);transition:transform 0.35s ease;';
    document.body.appendChild(frame);
    requestAnimationFrame(() => {
      frame.style.transform = 'translateX(0)';
    });
    return frame;
  };

  const postToSidebar = (message) => {
    const frame = document.getElementById(SCRIPT_ID);
    if (frame?.contentWindow) {
      frame.contentWindow.postMessage(message, '*');
    }
  };

  const extractProduct = () => {
    if (retailers.amazon) {
      return window.ShopSenseExtractors.amazon();
    }
    if (retailers.walmart) {
      return window.ShopSenseExtractors.walmart();
    }
    if (retailers.bestbuy) {
      return window.ShopSenseExtractors.bestbuy();
    }
    return null;
  };

  chrome.runtime.onMessage.addListener((message) => {
    if (message.type === 'shopsense.extract') {
      ensureExtractors()
        .then(() => {
          const payload = extractProduct();
          if (!payload) {
            alert('ShopSense AI: Unsupported retailer on this page.');
            return;
          }
          latestPayload = payload;
          loadSidebar().then(() => postToSidebar({ type: 'shopsense.extracted', payload }));
        })
        .catch((error) => {
          console.error('ShopSense AI extraction failed', error);
          alert('ShopSense AI could not access this page. Refresh and try again.');
        });
    }
  });

  window.addEventListener('message', (event) => {
    if (event.data?.type === 'shopsense.requestData' && latestPayload) {
      event.source?.postMessage({ type: 'shopsense.extracted', payload: latestPayload }, event.origin);
    }
  });
})();
