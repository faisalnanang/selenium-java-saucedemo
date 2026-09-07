# Selenium SauceDemo Refactoring Plan
## Target: Align with Playwright Custom Reporting & Enhanced Testing

### COMPLETION CHECKLIST

#### PHASE 1: Allure Branding & Custom Assets ✓
- [ ] Generate custom favicon (geometric diamond design)
- [ ] Setup Allure custom branding scripts
- [ ] Configure Maven to support scripts execution
- [ ] Test favicon + branding integration

#### PHASE 2: Screenshot & Video Enhancements
- [ ] Auto-capture screenshot on test failure (already partial)
- [ ] Auto-capture video on test failure (already partial)
- [ ] Organize screenshots/videos in results
- [ ] Embed artifacts in Allure reports

#### PHASE 3: Test Infrastructure Improvements
- [ ] Add retry mechanism for CI (Annotation-based)
- [ ] Enhance BaseTest with Allure steps
- [ ] Add performance logging
- [ ] Create comprehensive TestFailureListener

#### PHASE 4: Missing Test Cases
- [ ] Complete UI wording validation (login form, inventory, cart)
- [ ] Add cart badge validation after order
- [ ] Add cart product removal test
- [ ] Add product sorting test
- [ ] Add multiple product checkout test
- [ ] Add invalid checkout info validation

#### PHASE 5: CI/CD Configuration
- [ ] Create GitHub Actions workflow
- [ ] Setup retry logic in CI
- [ ] Parallel test execution
- [ ] Allure report integration

### FILES TO CREATE/MODIFY

**New Files:**
- `scripts/create-sample-favicon.mjs` - Node.js version to generate favicon
- `src/test/java/support/ScreenshotListener.java` - Custom screenshot logic
- `src/test/java/support/AllureListener.java` - Allure step tracking
- `src/test/java/tests/CartTest.java` - Cart removal & operations
- `src/test/java/tests/ProductSortingTest.java` - Sorting functionality

**Modify:**
- `pom.xml` - Add screenshot dependencies, exec plugin
- `testng.xml` - Add retry parameters
- `src/test/java/support/BaseTest.java` - Add lifecycle enhancements
- `src/test/java/tests/UiValidationTest.java` - Complete validation tests
- `src/test/java/pages/InventoryPage.java` - Add removal methods
- `src/test/java/pages/CartPage.java` - Add removal methods

---

## Current Status vs Target

### ✅ ALREADY HAVE:
- [x] Java 17 + Maven setup
- [x] Selenium 4.25.0
- [x] TestNG configuration
- [x] Allure TestNG integration
- [x] Multi-browser support (Chrome, Firefox, Edge)
- [x] Headless/Headed modes
- [x] Video recording on failure
- [x] Page Object Model (5 pages)
- [x] Driver Factory with password manager disable
- [x] BaseTest with @Listeners
- [x] Allure scripts (same as Playwright)
- [x] Custom branding (GILIGILI)

### ⚠️ NEED TO ADD/ENHANCE:
- [ ] Generate actual favicon file
- [ ] Proper screenshot capture on failure  
- [ ] Organize artifacts in test results
- [ ] Retry mechanism annotation
- [ ] Enhanced UI wording tests
- [ ] Cart operations tests (remove, update qty)
- [ ] Product sorting tests
- [ ] Performance metrics in Allure
- [ ] CI workflow with Maven

---

## Next Steps Priority

**IMMEDIATE (Start Now):**
1. Generate favicon using Node.js script  
2. Verify Allure scripts work with Selenium results
3. Test custom branding in report

**SHORT-TERM (This Session):**
4. Enhance ScreenshotListener
5. Add retry mechanism  
6. Complete missing test cases

**FOLLOW-UP:**
7. Setup CI/CD workflow
8. Add performance monitoring
9. Documentation & troubleshooting guide

---
