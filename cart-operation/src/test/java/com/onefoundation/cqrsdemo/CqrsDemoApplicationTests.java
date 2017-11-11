package com.onefoundation.cqrsdemo;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class CqrsDemoApplicationTests {
	
	@Rule
	public IgnoreRule ignoreRule = new IgnoreRule(getIgnore());
	//public IgnoreRule ignoreRule;
	
	private boolean getIgnore() {
		return true;
	}
	
	@Before
	public void setUp() {
		//ignoreRule.setIgnore(true);
	}
	
	@Test
	public void testList() {
		assertTrue(false);
	}
}
