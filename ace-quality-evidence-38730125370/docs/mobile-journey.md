# Mobile Journey Design: Beneficiary to Transfer

## Scope
Representative Appium journey for Android and iOS. The journey uses only the synthetic fixtures declared in `src/test/resources/data/synthetic-banking-manifest.json`; do not point it at a production service or real customer account.

## Journey
1. Authenticate as the synthetic customer `SYN-CUST-001` with a test-only identity provider.
2. Open Beneficiaries and add `SYN-BEN-001`.
3. Complete OTP using the controlled test OTP provider; verify beneficiary status is `PENDING`.
4. Refresh after the activation delay and verify the beneficiary becomes `ACTIVE`.
5. Initiate a USD 25.50 transfer and submit the unique idempotency key.
6. Verify the confirmation screen and transaction history show one transfer only.
7. Re-submit the same request after a simulated timeout; verify the same transfer reference is returned and no duplicate debit appears.

## Appium implementation outline
- Appium 2 with UiAutomator2 (Android) and XCUITest (iOS), using accessibility IDs rather than positional selectors.
- Keep OTP and service responses behind injectable test adapters; never bypass production controls in a production environment.
- Capture screenshots, device/OS versions, app build, API trace ID, and test run ID as evidence.
- Verify screen-reader labels and focus order on each financial confirmation screen.

## Device and degradation matrix
| Platform | Coverage | Network/interaction variation |
| --- | --- | --- |
| Android current and previous supported OS | Small and large viewport | Normal, 400 ms latency, offline during submit |
| iOS current and previous supported OS | Small and large viewport | Normal, connection reset after accepted submit |
| Android accessibility profile | Font scaling to 200%, TalkBack | Keyboard navigation and focus restoration |
| iOS accessibility profile | Dynamic Type, VoiceOver | VoiceOver labels and focus restoration |

## Pass/fail controls
- No transfer is submitted before the beneficiary is active.
- Timeout/retry results in exactly one accepted transaction.
- Error and offline states retain no sensitive payment data on screen after session expiry.
- All critical controls are reachable and announced to assistive technology.
- Flaky retries are reported separately and cannot turn a failed financial assertion into a pass.
