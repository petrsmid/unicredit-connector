package com.newpecunia.unicredit.impl;

import org.junit.Assert;
import org.junit.Test;

import com.newpecunia.unicredit.PackageStatus;
import com.newpecunia.unicredit.impl.PackageStatusParserImpl;

public class StatusImplTest {
	
	private String testFile = 
			"XYZ\n" +
			"Status : Signed\n" +
			"aeiouy";
	
	@Test
	public void testStatusParsing() {
		PackageStatus status = new PackageStatusParserImpl().parseFile(testFile);
		Assert.assertEquals(PackageStatus.SIGNED, status);
	}

}
