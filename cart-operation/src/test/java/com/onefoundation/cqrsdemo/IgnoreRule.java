package com.onefoundation.cqrsdemo;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class IgnoreRule implements TestRule {
	private boolean ignore = false;

	public IgnoreRule() {
		
	}
	
	public IgnoreRule(boolean ignore) {
		this.ignore = ignore;
	}
	
	public boolean isIgnore() {
		return ignore;
	}


	public IgnoreRule setIgnore(boolean ignore) {
		this.ignore = ignore;
		return this;
	}

	@Override
	public Statement apply(Statement statement, Description description) {
		if (ignore) {
			return new Statement() {

				@Override
				public void evaluate() throws Throwable {
					// Return an empty Statement object for those tests
					// that shouldn't run on the specified dataset.
				}
			};
		} else {
			return statement;
		}
	}
}
