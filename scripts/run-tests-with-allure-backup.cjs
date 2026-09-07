const fs = require('node:fs');
const path = require('node:path');
const { spawnSync } = require('node:child_process');

const projectRoot = path.resolve(__dirname, '..');
const resultsRoot = path.join(projectRoot, 'allure-results');
const activeResults = path.join(resultsRoot, '.current');
const latestResults = path.join(resultsRoot, 'latest');
const singleFileGenerator = path.join(projectRoot, 'scripts', 'generate-latest-allure-single-file.cjs');
const resultsCleaner = path.join(projectRoot, 'scripts', 'clean-allure-results.cjs');

function pad(value) {
  return String(value).padStart(2, '0');
}

function timestamp() {
  const now = new Date();
  return [
    pad(now.getDate()),
    pad(now.getMonth() + 1),
    now.getFullYear(),
    pad(now.getHours()),
    pad(now.getMinutes()),
    pad(now.getSeconds())
  ].join('');
}

function copyDirectory(source, destination) {
  fs.cpSync(source, destination, { recursive: true });
}

fs.rmSync(activeResults, { recursive: true, force: true });
fs.mkdirSync(activeResults, { recursive: true });

const mavenCommand = process.platform === 'win32' ? 'mvn.cmd' : 'mvn';
const mavenArguments = ['clean', 'test', ...process.argv.slice(2), `-Dallure.results.directory=${activeResults}`];
const mavenEnv = { ...process.env };
delete mavenEnv.JAVA_TOOL_OPTIONS;
delete mavenEnv._JAVA_OPTIONS;
const testRun = spawnSync(mavenCommand, mavenArguments, {
  cwd: projectRoot,
  env: mavenEnv,
  shell: process.platform === 'win32',
  stdio: 'inherit'
});

const hasResults = fs.existsSync(activeResults) && fs.readdirSync(activeResults).length > 0;

if (hasResults) {
  const cleanedResults = spawnSync(process.execPath, [resultsCleaner, activeResults], {
    cwd: projectRoot,
    stdio: 'inherit'
  });

  if (cleanedResults.error || cleanedResults.status !== 0) {
    console.warn('\nAllure result cleanup failed; original labels were preserved.');
  }

  const backupTimestamp = timestamp();
  const backupDirectory = path.join(resultsRoot, `saucedemo-${backupTimestamp}`);
  copyDirectory(activeResults, backupDirectory);

  fs.rmSync(latestResults, { recursive: true, force: true });
  copyDirectory(activeResults, latestResults);

  const singleFileBackup = spawnSync(process.execPath, [singleFileGenerator], {
    cwd: projectRoot,
    env: {
      ...process.env,
      ALLURE_BACKUP_TIMESTAMP: backupTimestamp
    },
    stdio: 'inherit'
  });

  if (singleFileBackup.error || singleFileBackup.status !== 0) {
    console.warn('\nSingle-file backup gagal dibuat.');
  }

  console.log(`\nAllure backup tersimpan: ${path.relative(projectRoot, backupDirectory)}`);
  console.log(`Allure latest diperbarui: ${path.relative(projectRoot, latestResults)}`);
} else {
  console.warn('\nTidak ada file Allure yang dihasilkan; backup tidak dibuat.');
}

if (testRun.error) {
  console.error(`Gagal menjalankan Maven: ${testRun.error.message}`);
  process.exitCode = 1;
} else {
  process.exitCode = typeof testRun.status === 'number' ? testRun.status : 1;
}

