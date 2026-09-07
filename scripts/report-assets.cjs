const fs = require('node:fs');
const path = require('node:path');

const projectRoot = path.resolve(__dirname, '..');
const assetsDirectory = path.join(projectRoot, 'assets');
const generatedFaviconFileName = 'report-favicon.ico';

function findFaviconPath() {
  if (!fs.existsSync(assetsDirectory)) {
    return null;
  }

  const faviconFiles = fs.readdirSync(assetsDirectory)
    .filter((fileName) => fileName.toLowerCase().endsWith('.ico'))
    .sort();

  if (faviconFiles.length > 1) {
    throw new Error(`Multiple .ico files found in assets: ${faviconFiles.join(', ')}. Keep only one custom icon.`);
  }

  return faviconFiles.length === 1 ? path.join(assetsDirectory, faviconFiles[0]) : null;
}

function replaceFaviconHref(html, href) {
  const updatedHtml = html.replace(
    /(<link\s+rel=["']icon["']\s+href=["'])[^"']*(["'])/i,
    `$1${href}$2`
  );

  return updatedHtml === html
    ? html.replace('</head>', `    <link rel="icon" href="${href}">\n</head>`)
    : updatedHtml;
}

function installStaticFavicon(reportDirectory) {
  const faviconPath = findFaviconPath();
  if (!faviconPath) {
    return;
  }

  const assetDirectory = path.join(reportDirectory, 'assets');
  const reportIndex = path.join(reportDirectory, 'index.html');
  const assetPath = path.join(assetDirectory, generatedFaviconFileName);

  fs.copyFileSync(faviconPath, assetPath);
  const html = fs.readFileSync(reportIndex, 'utf8');
  fs.writeFileSync(reportIndex, replaceFaviconHref(html, `assets/${generatedFaviconFileName}`));
}

function replaceSingleFileFavicon(html) {
  const faviconPath = findFaviconPath();
  if (!faviconPath) {
    return html;
  }

  return replaceFaviconHref(html, getFaviconDataUri());
}

function getFaviconDataUri() {
  const faviconPath = findFaviconPath();
  if (!faviconPath) {
    return '';
  }

  return `data:image/x-icon;base64,${fs.readFileSync(faviconPath).toString('base64')}`;
}

function createBrandIconScript(iconUrl) {
  return `<script>(function(){function updateBrandIcon(){document.querySelectorAll('.side-nav__brand-icon').forEach(function(element){if(element.dataset.customBrandIcon==='true'){return;}element.style.backgroundImage='url("${iconUrl}")';element.style.backgroundSize='contain';element.style.backgroundPosition='center';element.style.backgroundRepeat='no-repeat';element.dataset.customBrandIcon='true';});}updateBrandIcon();new MutationObserver(updateBrandIcon).observe(document.documentElement,{childList:true,subtree:true});})();</script>`;
}

function installStaticBrandIcon(reportIndex) {
  if (!findFaviconPath()) {
    return;
  }

  const html = fs.readFileSync(reportIndex, 'utf8');
  fs.writeFileSync(reportIndex, html.replace('</body>', `${createBrandIconScript(`assets/${generatedFaviconFileName}`)}</body>`));
}

module.exports = {
  createBrandIconScript,
  getFaviconDataUri,
  installStaticFavicon,
  installStaticBrandIcon,
  replaceFaviconHref,
  replaceSingleFileFavicon
};
