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
