package com.allwyn.bookstore.support;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failing test up to {@value #MAX_RETRIES} times before giving up.
 *
 * <p>Rationale: the FakeRestAPI is a shared public demo that occasionally
 * returns transient errors or times out. Retrying absorbs that infrastructure
 * noise so the report reflects genuine failures, not flakiness. A real failure
 * still fails — it just fails consistently across all attempts.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES = 2;

    private int attempts = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (attempts < MAX_RETRIES) {
            attempts++;
            return true;
        }
        return false;
    }
}
