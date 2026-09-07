const fs = require('node:fs');
const path = require('node:path');
const { spawnSync } = require('node:child_process');
const { name: reportBrand } = require('./report-brand.cjs');
const { installStaticBrandIcon, installStaticFavicon } = require('./report-assets.cjs');

const projectRoot = path.resolve(__dirname, '..');
const latestResults = path.join(projectRoot, 'allure-results', 'latest');
const reportDirectory = path.join(projectRoot, 'allure-report');

if (!fs.existsSync(latestResults) || fs.readdirSync(latestResults).length === 0) {
  console.error('Hasil Allure terbaru belum tersedia. Jalankan npm test terlebih dahulu.');
  process.exit(1);
}

const npxCommand = process.platform === 'win32' ? 'npx.cmd' : 'npx';
const generation = spawnSync(
  npxCommand,
  ['allure', 'generate', latestResults, '--clean', '--report-name', reportBrand, '-o', reportDirectory],
  {
    cwd: projectRoot,
    shell: process.platform === 'win32',
    stdio: 'inherit'
  }
);

if (generation.error) {
  console.error(`Gagal membuat Allure report: ${generation.error.message}`);
  process.exit(1);
}

if (generation.status !== 0) {
  process.exit(generation.status ?? 1);
}

installStaticFavicon(reportDirectory);
installStaticBrandIcon(path.join(reportDirectory, 'index.html'));
