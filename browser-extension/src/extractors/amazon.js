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
