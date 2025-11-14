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
