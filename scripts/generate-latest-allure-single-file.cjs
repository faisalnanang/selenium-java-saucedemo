const fs = require('node:fs');
const path = require('node:path');
const { spawnSync } = require('node:child_process');
const { name: reportBrand } = require('./report-brand.cjs');
const { createBrandIconScript, getFaviconDataUri, replaceSingleFileFavicon } = require('./report-assets.cjs');

const projectRoot = path.resolve(__dirname, '..');
const latestResults = path.join(projectRoot, 'allure-results', 'latest');
const generatedDirectory = path.join(projectRoot, 'allure-single-file', '.generated');
const singleFile = path.join(projectRoot, 'allure-single-file', 'saucedemo-latest.html');
const backupTimestamp = process.env.ALLURE_BACKUP_TIMESTAMP;

if (!fs.existsSync(latestResults) || fs.readdirSync(latestResults).length === 0) {
  console.error('Hasil Allure terbaru belum tersedia. Jalankan npm test terlebih dahulu.');
  process.exit(1);
}

fs.rmSync(generatedDirectory, { recursive: true, force: true });
fs.mkdirSync(generatedDirectory, { recursive: true });

const npxCommand = process.platform === 'win32' ? 'npx.cmd' : 'npx';
const generation = spawnSync(
  npxCommand,
  [
    'allure', 
    'generate',
    latestResults,
    '--clean',
    '--single-file',
    '--report-name',
    reportBrand,
    '-o',
    generatedDirectory
  ],
  { cwd: projectRoot, shell: process.platform === 'win32', stdio: 'inherit' }
);

if (generation.error) {
  console.error(`Gagal membuat single-file report: ${generation.error.message}`);
  process.exit(1);
}

if (generation.status !== 0) {
  process.exit(generation.status ?? 1);
}

const generatedIndex = path.join(generatedDirectory, 'index.html');
if (!fs.existsSync(generatedIndex)) {
  console.error(`File report tidak ditemukan pada ${generatedIndex}`);
  process.exit(1);
}

const reportContent = fs.readFileSync(generatedIndex, 'utf8');
const brandingScript = `<script>(function(){function renameBrand(){document.querySelectorAll('.side-nav__brand-text').forEach(function(element){if(element.textContent!==${JSON.stringify(reportBrand)}){element.textContent=${JSON.stringify(reportBrand)};}});}renameBrand();new MutationObserver(renameBrand).observe(document.documentElement,{childList:true,subtree:true});})();</script>`;
const iconScript = createBrandIconScript(getFaviconDataUri());
fs.writeFileSync(singleFile, replaceSingleFileFavicon(reportContent).replace('</body>', `${brandingScript}${iconScript}</body>`));
console.log(`\nSingle-file report tersimpan: ${path.relative(projectRoot, singleFile)}`);

if (backupTimestamp) {
  const backupFile = path.join(projectRoot, 'allure-single-file', `saucedemo-${backupTimestamp}.html`);
  fs.copyFileSync(singleFile, backupFile);
  console.log(`Single-file backup tersimpan: ${path.relative(projectRoot, backupFile)}`);
}

