const fs = require('node:fs');
const path = require('node:path');

const projectRoot = path.resolve(__dirname, '..');
const resultsDirectory = path.join(projectRoot, 'allure-results', 'latest');

if (!fs.existsSync(resultsDirectory)) {
  console.error('Allure latest results directory was not found.');
  process.exit(1);
}

const environment = [
  `Browser=${process.env.BROWSER || 'Chrome Headless'}`,
  `Java=${process.env.JAVA_VERSION || '17'}`,
  `Selenium=4.47.0`,
  `Test Framework=TestNG 7.10.2`,
  `Allure=2.43.0`,
  `Base URL=${process.env.BASE_URL || 'https://www.saucedemo.com'}`,
  `CI=${process.env.CI || 'false'}`
].join('\n') + '\n';

const categories = [
  {
    name: 'Product defect',
    matchedStatuses: ['failed'],
    messageRegex: '.*(assert|expected|actual).*'
  },
  {
    name: 'Infrastructure or browser failure',
    matchedStatuses: ['broken'],
    messageRegex: '.*(WebDriver|browser|timeout|connection|session).*'
  }
];

const executor = {
  name: process.env.GITHUB_ACTIONS === 'true' ? 'GitHub Actions' : 'Local Maven Execution',
  type: process.env.GITHUB_ACTIONS === 'true' ? 'github' : 'local',
  buildName: process.env.GITHUB_WORKFLOW || 'Selenium SauceDemo Tests',
  buildOrder: process.env.GITHUB_RUN_NUMBER ? Number(process.env.GITHUB_RUN_NUMBER) : undefined,
  buildUrl: process.env.GITHUB_SERVER_URL && process.env.GITHUB_REPOSITORY && process.env.GITHUB_RUN_ID
    ? `${process.env.GITHUB_SERVER_URL}/${process.env.GITHUB_REPOSITORY}/actions/runs/${process.env.GITHUB_RUN_ID}`
    : undefined,
  reportUrl: 'https://faisalnanang.github.io/selenium-java-saucedemo/'
};

Object.keys(executor).forEach((key) => {
  if (executor[key] === undefined) delete executor[key];
});

fs.writeFileSync(path.join(resultsDirectory, 'environment.properties'), environment);
fs.writeFileSync(path.join(resultsDirectory, 'categories.json'), JSON.stringify(categories, null, 2) + '\n');
fs.writeFileSync(path.join(resultsDirectory, 'executor.json'), JSON.stringify(executor, null, 2) + '\n');

console.log(`Allure metadata added to ${path.relative(projectRoot, resultsDirectory)}`);
