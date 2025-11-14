(function(){
      window.ShopSenseExtractors = window.ShopSenseExtractors || {};
window.ShopSenseExtractors.amazon = () => {
  const title = document.getElementById('productTitle')?.innerText?.trim();
  const price = document.querySelector('#corePrice_feature_div span.a-offscreen')?.innerText;
  const image = document.querySelector('#landingImage')?.src;
  return {
    retailer: 'amazon',
    title,
    price,
    image,
    url: window.location.href
  };
};

window.ShopSenseExtractors = window.ShopSenseExtractors || {};
window.ShopSenseExtractors.walmart = () => {
  const title = document.querySelector('h1[data-automation="product-title"]')?.innerText?.trim();
  const price = document.querySelector('[itemprop="price"]')?.getAttribute('content');
  const image = document.querySelector('img[data-automation="main-image"]')?.src;
  return {
    retailer: 'walmart',
    title,
    price,
    image,
    url: window.location.href
  };
};

window.ShopSenseExtractors = window.ShopSenseExtractors || {};
window.ShopSenseExtractors.bestbuy = () => {
  const title = document.querySelector('div.sku-title h1')?.innerText?.trim();
  const price = document.querySelector('div.priceView-hero-price span')?.innerText;
  const image = document.querySelector('div.primary-image-container img')?.src;
  return {
    retailer: 'bestbuy',
    title,
    price,
    image,
    url: window.location.href
  };
};

    })();
