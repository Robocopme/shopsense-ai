const streamEl = document.getElementById('insight-stream');
const statusEl = document.getElementById('status');
const consentToggle = document.getElementById('consent-toggle');
const analyzeBtn = document.getElementById('analyze-btn');
const consentCard = document.getElementById('consent');
const insightsCard = document.getElementById('insights');
const authCard = document.getElementById('auth');
const loginBtn = document.getElementById('login-btn');
const registerBtn = document.getElementById('register-btn');
const authStatus = document.getElementById('auth-status');
const authEmail = document.getElementById('auth-email');
const authPassword = document.getElementById('auth-password');
const authName = document.getElementById('auth-name');

chrome.storage.sync.get(['shopsenseConsent'], (result) => {
  if (result.shopsenseConsent) {
    consentToggle.checked = true;
  }
});

consentToggle.addEventListener('change', (event) => {
  chrome.storage.sync.set({ shopsenseConsent: event.target.checked });
});

const renderChunk = (chunk) => {
  const div = document.createElement('div');
  div.className = 'card';
  div.innerHTML = `<strong>${chunk.title}</strong><p>${chunk.body}</p>`;
  streamEl.prepend(div);
};

const authState = {
  token: null
};

const setAuthState = (token) => {
  authState.token = token;
  if (token) {
    authCard.classList.add('hidden');
    consentCard.classList.remove('hidden');
  } else {
    authCard.classList.remove('hidden');
    consentCard.classList.add('hidden');
    insightsCard.classList.add('hidden');
  }
};

chrome.storage.sync.get(['shopsenseToken'], (result) => {
  if (result.shopsenseToken) {
    setAuthState(result.shopsenseToken);
  } else {
    setAuthState(null);
  }
});

const requestInsights = (payload) => {
  chrome.runtime.sendMessage(
    { type: 'shopsense.request-insights', payload, token: authState.token },
    (response) => {
      if (!response?.ok) {
        statusEl.textContent = `Error: ${response?.error ?? 'Unknown'}`;
        return;
      }
      statusEl.textContent = 'Insights ready';
      response.payload.sections.forEach(renderChunk);
    }
  );
};

const handleAuthResponse = (response) => {
  if (!response?.ok) {
    authStatus.textContent = response?.error || 'Authentication failed';
    return;
  }
  if (response.token) {
    setAuthState(response.token);
    authStatus.textContent = 'Signed in!';
    chrome.storage.sync.set({ shopsenseToken: response.token });
  } else if (response.user) {
    authStatus.textContent = 'Account created. Please sign in.';
  }
};

loginBtn.addEventListener('click', () => {
  authStatus.textContent = 'Signing in…';
  chrome.runtime.sendMessage(
    {
      type: 'shopsense.auth.login',
      payload: {
        email: authEmail.value,
        password: authPassword.value
      }
    },
    handleAuthResponse
  );
});

registerBtn.addEventListener('click', () => {
  authStatus.textContent = 'Creating account…';
  chrome.runtime.sendMessage(
    {
      type: 'shopsense.auth.register',
      payload: {
        email: authEmail.value,
        fullName: authName.value || authEmail.value,
        password: authPassword.value,
        role: 'USER',
        marketingOptIn: true,
        preferences: []
      }
    },
    handleAuthResponse
  );
});

analyzeBtn.addEventListener('click', () => {
  if (!authState.token) {
    alert('Please sign in first.');
    return;
  }
  if (!consentToggle.checked) {
    alert('Consent required before analyzing.');
    return;
  }
  consentCard.classList.add('hidden');
  insightsCard.classList.remove('hidden');
  statusEl.textContent = 'Contacting ShopSense AI…';
  window.parent.postMessage({ type: 'shopsense.requestData' }, '*');
});

window.addEventListener('message', (event) => {
  if (event.data?.type === 'shopsense.extracted' && event.data.payload) {
    const payload = event.data.payload;
    requestInsights({
      url: payload.url ?? event.origin,
      retailer: payload.retailer,
      product: payload,
      userConsent: consentToggle.checked
    });
  }
});
