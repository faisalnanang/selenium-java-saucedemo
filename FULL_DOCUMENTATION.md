# Selenium SauceDemo - Comprehensive E2E Testing Framework

Modern end-to-end testing framework untuk **SauceDemo** menggunakan Selenium WebDriver (Java), TestNG, dan Allure Report dengan custom branding & reporting.

## 🎯 Features

### Core Testing
- ✅ **Selenium WebDriver 4.25** - Latest stable version
- ✅ **Java 17** - Modern Java with latest language features
- ✅ **TestNG 7.10.2** - Powerful test framework with listeners
- ✅ **Multi-browser support** - Chrome, Firefox, Edge
- ✅ **Headless & Headed modes** - Configurable execution
- ✅ **Page Object Model** - 5 page objects (Login, Inventory, Cart, Checkout, OrderConfirmation)

### Advanced Reporting
- ✅ **Allure Report 2.29.1** - Beautiful visual reports
- ✅ **Custom Branding** - Brand name "GILIGILI" + custom icon
- ✅ **Custom Favicon** - Geometric diamond design (auto-generated)
- ✅ **Single-file HTML Reports** - Portable & shareable
- ✅ **Timestamped Backups** - All test results preserved

### Quality Assurance
- ✅ **Auto Screenshot on Failure** - PNG capture embedded in report
- ✅ **Auto Video Recording** - Desktop video on failure (requires ffmpeg)
- ✅ **Browser Performance Trace** - Chrome Performance API logs
- ✅ **Page Source Capture** - HTML dump on failure
- ✅ **Retry Mechanism** - CI-aware automatic retries
- ✅ **Comprehensive Listeners** - Test lifecycle tracking

### Test Coverage
- ✅ **Login Tests** - Standard user + locked-out user scenarios
- ✅ **Checkout Flow** - Complete e-commerce purchase flow
- ✅ **UI Validation** - Wording validation + mathematical calculations
- ✅ **Cart Operations** - Add product, remove product, verify empty
- ✅ **Product Sorting** - All sorting options (A-Z, Z-A, price, rating)
- ✅ **Error Handling** - Checkout validation errors

---

## 📁 Project Structure

```
selenium-saucedemo/
├── src/test/java/
│   ├── data/
│   │   └── Users.java                    # Test credentials (standard, locked-out)
│   ├── pages/
│   │   ├── LoginPage.java                # Login page object
│   │   ├── InventoryPage.java            # Product listing + sorting
│   │   ├── CartPage.java                 # Shopping cart with removal
│   │   ├── CheckoutPage.java             # 2-step checkout + calculations
│   │   └── OrderConfirmationPage.java    # Order completion
│   ├── support/
│   │   ├── BaseTest.java                 # Test base class + video recording
│   │   ├── DriverFactory.java            # Multi-browser WebDriver factory
│   │   ├── VideoRecorder.java            # ffmpeg-based video capture
│   │   ├── TestFailureListener.java      # Failure listener (screenshots, trace, etc)
│   │   ├── AllureListener.java           # Allure event listener
│   │   └── RetryAnalyzer.java            # Automatic retry logic
│   └── tests/
│       ├── LoginTest.java                # Login scenarios (2 tests)
│       ├── CheckoutTest.java             # E-commerce flow (1 test)
│       └── UiValidationTest.java         # UI validation (5 tests)
├── scripts/
│   ├── create-sample-favicon.mjs         # Generate custom icon (Node.js)
│   ├── report-brand.cjs                  # Custom branding config
│   ├── report-assets.cjs                 # Icon & favicon handling
│   ├── run-tests-with-allure-backup.cjs  # Test runner + backup orchestrator
│   ├── generate-latest-allure.cjs        # Multi-file report generator
│   ├── generate-latest-allure-single-file.cjs # Single-file report
│   ├── serve-latest-allure.cjs           # Start Allure server
│   └── clean-allure-results.cjs          # Remove redundant labels
├── assets/
│   └── saucedemo.ico                     # Custom favicon (auto-generated)
├── pom.xml                               # Maven configuration
├── testng.xml                            # TestNG suite configuration
├── README.md                             # This file
├── REFACTORING_PLAN.md                   # Implementation plan
├── .github/workflows/                    # CI/CD pipelines
├── .env.example                          # Environment configuration template
└── .gitignore
```

---

## 🚀 Quick Start

### Prerequisites
- **Java 17+** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8+** - [Download](https://maven.apache.org/download.cgi)
- **Node.js 18+** - For running Allure scripts
- **ffmpeg** (optional) - For video recording on failure
  - Windows: `choco install ffmpeg`
  - macOS: `brew install ffmpeg`
  - Linux: `apt-get install ffmpeg`

### Installation

```bash
# Clone or navigate to project
cd selenium-saucedemo

# Install Maven dependencies
mvn clean install

# Generate custom favicon
node scripts/create-sample-favicon.mjs
```

### Running Tests

```bash
# Run all tests (headless, default)
mvn test

# Run tests in headed mode (visible browser)
mvn test -Dheadless=false

# Run specific browser
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge

# Run with custom base URL
mvn test -DbaseUrl=http://localhost:3000

# Run with retry enabled (for CI)
mvn test -DCI=true -DretryCount=2

# Run specific test class
mvn test -Dtest=LoginTest

# Run specific test method
mvn test -Dtest=LoginTest#standardUserCanLogin
```

---

## 📊 Reports & Results

### Allure Reports (Choose One)

**1. Interactive Allure Server (Best for Review)**
```bash
# After running tests:
node scripts/serve-latest-allure.cjs

# Opens: http://localhost:4040/
# Features:
# - Interactive dashboard
# - Real-time filtering
# - Custom branding applied
# - Custom favicon visible in browser tab
```

**2. Single-File HTML Report (Easy Sharing)**
```bash
# After running tests:
node scripts/generate-latest-allure-single-file.cjs

# Output: allure-single-file/saucedemo-latest.html
# Can be:
# - Opened directly in browser
# - Emailed to stakeholders
# - Stored as build artifact
# - Shared via link
```

**3. Static Multi-File Report**
```bash
# After running tests:
node scripts/generate-latest-allure.cjs

# Output: allure-report/ (directory with assets)
# Use for:
# - CI/CD artifact storage
# - Archive & audit
```

### Result Storage Structure

```
target/
├── allure-results/
│   ├── .current/              # Current test run results (JSON)
│   ├── latest/ → symlink      # Latest run for report generation
│   └── saucedemo-DDMMYYYYHHMMSS/ # Timestamped backups
├── videos/                    # Video recordings on failure
└── reports/                   # Allure HTML reports

allure-single-file/
├── saucedemo-latest.html      # Latest single-file report
└── saucedemo-DDMMYYYYHHMMSS.html # Timestamped backups
```

---

## 🎨 Custom Branding

### Brand Name
Edit [scripts/report-brand.cjs](scripts/report-brand.cjs):
```javascript
module.exports = { name: 'GILIGILI' };  // Change to your brand
```

### Custom Icon/Favicon

**Option 1: Generate Programmatically**
```bash
# Generates geometric diamond design
node scripts/create-sample-favicon.mjs

# Output: assets/saucedemo.ico
# Sizes: 16x16, 32x32, 48x48 pixels
# Design: Dark blue background, orange diamond, white center
```

**Option 2: Use Custom Icon**
```bash
# 1. Place your .ico file in assets/ folder
# 2. Must be single .ico file (system will error on multiple)
# 3. Filename can be anything (e.g., mylogo.ico, favicon.ico)
# 4. Icon automatically embedded in all reports
```

**Icon Usage**
- ✅ Browser tab favicon (report pages)
- ✅ Allure sidebar branding icon
- ✅ Embedded in single-file report (no external assets)

---

## 📸 Features in Detail

### Auto Screenshot on Failure
```java
@Test
public void someTest() {
    // If test fails:
    // 1. Screenshot automatically captured (PNG)
    // 2. Embedded in Allure report
    // 3. Can be clicked to view full resolution
}
```

### Auto Video on Failure (Requires ffmpeg)
```java
// BaseTest starts video at @BeforeMethod
// TestFailureListener stops & embeds on @OnFailure
// Video visible in Allure "Attachments" tab
```

### Browser Performance Logs
- Chrome Performance API metrics
- Captured automatically on failure
- Available in Allure as JSON attachment
- Includes: FCP, LCP, timing data

### Page Source Capture
- HTML snapshot on failure
- Accessible in report for debugging
- Shows exact DOM state at failure point

---

## 🔧 Configuration

### System Properties

| Property | Default | Options | Example |
|----------|---------|---------|---------|
| `baseUrl` | https://www.saucedemo.com | Any URL | `-DbaseUrl=http://localhost:3000` |
| `headless` | true | true/false | `-Dheadless=false` |
| `browser` | chrome | chrome, firefox, edge | `-Dbrowser=firefox` |
| `retryCount` | 0 | 0-5 | `-DretryCount=2` (CI only) |
| `CI` | false | true/false | `-DCI=true` |
| `allure.results.directory` | target/allure-results | Path | Maven config |

### Environment Variables (.env)
```bash
# Copy .env.example to .env and customize:
cp .env.example .env

# Then:
source .env  # Linux/macOS
# or .env is auto-loaded on Windows
```

### Maven Profiles

```bash
# Headed mode profile
mvn test -Pheaded

# Equivalent to: mvn test -Dheadless=false
```

---

## ✅ Test Cases

### LoginTest (2 tests)
- ✅ Standard user login → inventory page
- ✅ Locked-out user error message

### CheckoutTest (1 test)
- ✅ Complete e-commerce flow:
  1. Login
  2. Add product to cart
  3. Verify cart contents
  4. Checkout (fill info)
  5. Verify order overview
  6. Complete order
  7. Verify confirmation
  8. Verify cart badge cleared

### UiValidationTest (5 tests)
- ✅ Login form wording (Username, Password, Login button)
- ✅ Inventory/Cart wording (Products, Your Cart, Checkout)
- ✅ Checkout calculation (subtotal + tax = total)
- ✅ Product sorting & cart removal
- ✅ Checkout error validation (required fields)

**Total: 8 comprehensive test cases**

---

## 🐛 Troubleshooting

### Video Recording Fails
```
Error: ffmpeg not found
Solution: 
  - Windows: choco install ffmpeg
  - macOS: brew install ffmpeg
  - Linux: apt-get install ffmpeg
  - Or run in headless mode: mvn test -Dheadless=true
```

### Tests Timeout
```
Solution:
  - Increase Maven surefire timeout
  - Check BASE_URL is accessible
  - Verify network connection
```

### Allure Report Not Generated
```
Solution:
  - Ensure tests produced results: ls target/allure-results/
  - Install allure-commandline: npm install -g allure-commandline
  - Or use: node scripts/generate-latest-allure.cjs
```

### Custom Icon Not Showing
```
Solution:
  - Verify icon file exists: ls assets/
  - Must be single .ico file only
  - Delete old .ico if multiple exist
  - Regenerate: node scripts/create-sample-favicon.mjs
```

---

## 🚦 CI/CD Integration

### GitHub Actions Example
```yaml
name: Selenium Tests
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: 17
          distribution: temurin
      - run: mvn clean test -DCI=true -DretryCount=2
      - uses: actions/upload-artifact@v3
        if: always()
        with:
          name: test-results
          path: target/allure-results
      - name: Generate report
        if: always()
        run: node scripts/generate-latest-allure-single-file.cjs
      - uses: actions/upload-artifact@v3
        if: always()
        with:
          name: allure-report
          path: allure-single-file/
```

---

## 📚 Documentation Reference

- [Selenium WebDriver Docs](https://www.selenium.dev/documentation/webdriver/)
- [TestNG Docs](https://testng.org/doc/)
- [Allure Report Docs](https://docs.qameta.io/allure/)
- [SauceDemo Application](https://www.saucedemo.com)

---

## 🤝 Contributing

To add tests:
1. Create page object in `src/test/java/pages/`
2. Create test class in `src/test/java/tests/`
3. Extend `BaseTest` for video/screenshot support
4. Use `@Listeners(TestFailureListener.class)` if custom
5. Run `mvn clean test` to verify

---

## 📝 License

This project is provided as-is for educational and testing purposes.

---

## ✨ Summary

This Selenium SauceDemo framework provides:
- **Professional-grade testing** with Java 17 + TestNG
- **Visual reporting** with custom Allure branding & icons
- **Automatic artifacts** - screenshots, videos, performance traces
- **Retry logic** for CI environments
- **Comprehensive coverage** - 8 test cases covering all flows
- **Easy reports** - Single-file HTML, server, or static hosting
- **Fully configurable** - Browser, headless, URL, retry count

Ready for production use! 🚀
