package com.newpecunia.unicredit.impl;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.newpecunia.unicredit.Country;
import com.newpecunia.unicredit.ForeignPayment;
import com.newpecunia.unicredit.configuration.PayerConfiguration;
import com.newpecunia.unicredit.impl.MulticashForeignPaymentPackage;
import com.newpecunia.unicredit.impl.utils.TimeProviderImpl;

public class MulticashForeignPaymentPackageTest {
	
	/**
	 * In this test it is important to have a look on the output to check if it is OK.
	 */
	@Test
	public void testMulticashToString() {
		ForeignPayment payment = new ForeignPayment();
		
		payment.setAmount(new BigDecimal("20.001"));
		payment.setCurrency("USD");
		payment.setAccountNumber("SK9511110000001222977022");

		payment.setName("PEKAT s.r.o");
		payment.setAddress("Svatopluka Cecha 57");
		payment.setCity("Brno");
		payment.setPostalCode("612 00");
		payment.setCountry(new Country("CZ", "Czech Republic"));
		
		payment.setSwift("UNCRSKBX");
		payment.setBankName("UniCredit Bank Slovakia a.s.");
		payment.setBankAddress("Sancova 1/A");
		payment.setBankCity("Bratislava");
		payment.setBankPostalCode("813 33");
		payment.setBankCountry(new Country("SK", "Slovakia"));
		
		MulticashForeignPaymentPackage pack = new MulticashForeignPaymentPackage(
				"1234567890123456", payment, new TimeProviderImpl(), preparePayerConfig());
		
		String content = pack.toMultiCashFileContent();
		System.out.println(content); //look whether the output is OK
		
		
		int lines = 1;
		int lastidx = 0;
		while (content.indexOf("\r\n", lastidx)>=0) {
			lastidx = content.indexOf("\r\n", lastidx) + 1;
			lines++;
		}
		Assert.assertEquals(38, lines);
	}

	
	private PayerConfiguration preparePayerConfig() {
		PayerConfiguration payerConfig = new PayerConfiguration();
		payerConfig.setPayerName("Your Name");
		payerConfig.setPayerStreet("Street");
		payerConfig.setPayerCity("City");
		payerConfig.setPayerCountry("Country");
		
		payerConfig.setPayerAccountNumber("1234567890"); //account number in domestic form
		payerConfig.setPayerAccountCurrency("EUR");
		
		payerConfig.setPayerBankSwift("UNCRSKBX");
		payerConfig.setPayerBankSwiftLong("UNCRSKBXAXXX");
		
		return payerConfig;
	}	
}
