const API_BASE = 'http://localhost:8080/api/v1';
const SUPPORTED_HOST_PATTERN = /\b(amazon|walmart|bestbuy)\./i;
const TOKEN_KEY = 'shopsenseToken';

const withActiveTab = (callback) => {
  chrome.tabs.query({ active: true, currentWindow: true }, (tabs) => {
    if (tabs.length > 0) {
      callback(tabs[0]);
    }
  });
};

const flashBadge = (text, timeout = 2000) => {
  chrome.action.setBadgeBackgroundColor({ color: '#0ea5e9' });
  chrome.action.setBadgeText({ text });
  setTimeout(() => chrome.action.setBadgeText({ text: '' }), timeout);
};

const executeScript = (tabId, files) =>
  new Promise((resolve, reject) => {
    chrome.scripting.executeScript({ target: { tabId }, files }, () => {
      if (chrome.runtime.lastError) {
        reject(chrome.runtime.lastError);
      } else {
        resolve();
      }
    });
  });

const injectContentStack = async (tabId) => {
  await executeScript(tabId, ['src/extractors.bundle.js']);
  await executeScript(tabId, ['src/content.js']);
};

const requestExtraction = (tab) => {
  chrome.tabs.sendMessage(tab.id, { type: 'shopsense.extract' }, () => {
    if (chrome.runtime.lastError) {
      injectContentStack(tab.id)
        .then(() => {
          chrome.tabs.sendMessage(tab.id, { type: 'shopsense.extract' });
        })
        .catch((error) => {
          console.warn('ShopSense AI content script error', error);
          flashBadge('Err');
        });
    }
  });
};

chrome.action.onClicked.addListener(() => {
  withActiveTab((tab) => {
    if (!SUPPORTED_HOST_PATTERN.test(tab.url || '')) {
      flashBadge('N/A');
      // still attempt injection so users on regional domains (e.g., amazon.co.uk) can opt-in after seeing alert
      requestExtraction(tab);
      return;
    }
    requestExtraction(tab);
  });
});

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.type === 'shopsense.request-insights') {
    fetch(`${API_BASE}/insights/analyze`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${message.token || ''}`
      },
      body: JSON.stringify(message.payload)
    })
      .then((res) => res.json())
      .then((payload) => sendResponse({ ok: true, payload }))
      .catch((error) => sendResponse({ ok: false, error: error.message }));
    return true;
  }
  if (message.type === 'shopsense.auth.login') {
    fetch(`${API_BASE}/users/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(message.payload)
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error('Invalid credentials');
        }
        return res.json();
      })
      .then((data) => {
        chrome.storage.sync.set({ [TOKEN_KEY]: data.data?.accessToken ?? '' }, () => {
          sendResponse({ ok: true, token: data.data?.accessToken });
        });
      })
      .catch((error) => sendResponse({ ok: false, error: error.message }));
    return true;
  }
  if (message.type === 'shopsense.auth.register') {
    fetch(`${API_BASE}/users`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(message.payload)
    })
      .then((res) => {
        if (!res.ok) {
          return res.json().then((body) => {
            throw new Error(body.message || 'Registration failed');
          });
        }
        return res.json();
      })
      .then((data) => sendResponse({ ok: true, user: data.data }))
      .catch((error) => sendResponse({ ok: false, error: error.message }));
    return true;
  }
  return false;
});
