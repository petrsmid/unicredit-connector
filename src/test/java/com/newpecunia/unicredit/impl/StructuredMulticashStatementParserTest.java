package com.newpecunia.unicredit.impl;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.newpecunia.unicredit.impl.Statement;
import com.newpecunia.unicredit.impl.StructuredMulticashStatementParser;

public class StructuredMulticashStatementParserTest {
	
	private String testFile = 
			"{1:F01UNCRSKBXAXXX0000000000}{2:I940UNCRSKBXAXXXN}{4:\n"+
			":20:PEKAT\n"+
			":25:UNCRSKBX/1222977006\n"+
			":28C:2/1\n"+
			":60F:C130910USD105,37\n"+
			":61:1309200920D50,FMSC20130920602478\n"+
			"//3263100903\n"+
			":86:020?00S-TUZ-VYSLA/DOM-STNDTRNSF-O?2001222977022?22USD 50,00?24New\n"+
			" Pecunia X1KJVDUJIX?25our BTCs?30BACXSKBA?3101222977022?32UN\n"+
			"ICREDIT BANK SLOVAKIA A.S?33. 1/A, SANCOVA?60PT 000\n"+
			":62F:C130920USD55,37\n"+
			":64:C130920USD15,37\n"+
			"-}";

	
	@Test
	public void testBalance() {
		StructuredMulticashStatementParser parser = new StructuredMulticashStatementParser();
		Statement stats = parser.parseStatements(testFile);
		Assert.assertTrue(stats.getBalance().compareTo(new BigDecimal("55.37")) == 0);
	}
	
}
