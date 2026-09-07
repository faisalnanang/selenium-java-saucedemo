const fs = require('node:fs');
const path = require('node:path');

const resultsDirectory = process.argv[2];
const labelsToRemove = new Set(['parentSuite', 'suite', 'package', 'titlePath']);

if (!resultsDirectory) {
  console.error('Usage: node scripts/clean-allure-results.cjs <results-directory>');
  process.exit(1);
}

for (const fileName of fs.readdirSync(resultsDirectory)) {
  if (!fileName.endsWith('-result.json')) {
    continue;
  }

  const filePath = path.join(resultsDirectory, fileName);
  const result = JSON.parse(fs.readFileSync(filePath, 'utf8'));

  if (Array.isArray(result.labels)) {
    result.labels = result.labels.filter((label) => !labelsToRemove.has(label.name));
  }

  fs.writeFileSync(filePath, `${JSON.stringify(result)}\n`);
}
