package com.newpecunia.unicredit.impl;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StructuredMulticashStatementParser {
	
	private static final Logger logger = LoggerFactory.getLogger(StructuredMulticashStatementParser.class);	
		
	public StructuredMulticashStatementParser() {}
	
	public Statement parseStatements(String inputFile) {
		logger.trace("Parsing statement file: "+inputFile);
		Statement result = new Statement();
		result.setBalance(parseBalance(inputFile));
		return result;
	}

	private BigDecimal parseBalance(String inputFile) {
		String lastBalanceLine = null;
		Pattern pattern = Pattern.compile("(?m)^:62F:(.*)$");
		Matcher matcher = pattern.matcher(inputFile);
		while(matcher.find()) {
			lastBalanceLine = matcher.group(1);
		}
		
		int idx = lastBalanceLine.lastIndexOf("USD") + 3; /*length of USD*/
		String amountStr = lastBalanceLine.substring(idx);
		String amountStrWithDotAsDeliminer = amountStr.replace(',', '.');
		BigDecimal amount = new BigDecimal(amountStrWithDotAsDeliminer);
		return amount;
	}
	
}
