package com.newpecunia.unicredit.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newpecunia.unicredit.ForeignPayment;
import com.newpecunia.unicredit.UnicreditConnectorException;
import com.newpecunia.unicredit.configuration.PayerConfiguration;
import com.newpecunia.unicredit.impl.utils.TimeProvider;

public class MulticashForeignPaymentPackage {

	private static final Logger logger = LoggerFactory.getLogger(MulticashForeignPaymentPackage.class);	
	
	private static final String NEWLINE = "\r\n";

	private ForeignPayment payment;

	private TimeProvider timeProvider;

	private String shortPaymentReference;

	private PayerConfiguration configuration;

	/**
	 * We use "one payment per package" strategy.
	 */
	public MulticashForeignPaymentPackage(String reference, ForeignPayment foreignPayment, TimeProvider timeProvider, PayerConfiguration configuration) {
		this.shortPaymentReference = reference;
		this.payment = foreignPayment;
		this.timeProvider = timeProvider;
		this.configuration = configuration;
	}
	
	
	/**
	 * Creates file content for the foreign multicash package file. Always use UTF-8 to encode the file. 
	 */
	public String toMultiCashFileContent() {
		logger.trace("Creating Multicash package file content.");
		BigDecimal roundedAmount = payment.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
		
		DecimalFormat formatter = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.GERMAN)); //We don't want to use default locale. The German Locale is OK.
		String amountStr = formatter.format(roundedAmount); 					

		//header
		StringBuilder builder = new StringBuilder();
		appendNewLine(builder);
		appendFixed(builder, 4, ":01:");
		if (!StringUtils.isBlank(shortPaymentReference)) {
			appendVariable(builder, 16, shortPaymentReference);
		}
		appendNewLine(builder);
		
		appendFixed(builder, 4, ":02:");
		appendVariable(builder, 17, amountStr);
		appendNewLine(builder);

		appendFixed(builder, 4, ":03:");
		appendVariable(builder, 5, "1"); //number of payments is always 1 (we put only one payment into the package)
		appendNewLine(builder);
		
		appendFixed(builder, 4, ":04:");
		appendVariable(builder, 11, formatField(configuration.getPayerBankSwift()));
		appendNewLine(builder);

		appendFixed(builder, 4, ":05:");
		appendVariable(builder, 35, formatField(configuration.getPayerName()));
		appendNewLine(builder);
		if (!StringUtils.isBlank(configuration.getPayerStreet())) {
			appendVariable(builder, 35, formatField(configuration.getPayerStreet()));
			appendNewLine(builder);
		}
		if (!StringUtils.isBlank(configuration.getPayerCity())) {
			appendVariable(builder, 35, formatField(configuration.getPayerCity()));
			appendNewLine(builder);
		}
		if (!StringUtils.isBlank(configuration.getPayerCountry())) {
			appendVariable(builder, 35, formatField(configuration.getPayerCountry()));
			appendNewLine(builder);
		}
		appendFixed(builder, 4, ":07:");
		String referenceSuffix = shortPaymentReference.length() <= 12 ? shortPaymentReference : shortPaymentReference.substring(shortPaymentReference.length()-12, shortPaymentReference.length());
		appendVariable(builder, 12, referenceSuffix); //should be file name but it is not necessary to provide the real name of the file
		appendNewLine(builder);

		
		builder.append("{1:F01");
		appendVariable(builder, 16, formatField(configuration.getPayerBankSwiftLong()));
		builder.append("0001000001}{2:I100");
		appendVariable(builder, 16, formatField(payment.getSwift()));
		builder.append("N1}{4:");
		appendNewLine(builder);
		
		//body (text block)
		appendFixed(builder, 4, ":20:");
		appendFixed(builder, 16, shortPaymentReference);
		appendNewLine(builder);

		appendFixed(builder, 5, ":32A:");
		appendFixed(builder, 6, (new SimpleDateFormat("yyMMdd")).format(timeProvider.nowDate()));
		appendFixed(builder, 3, formatField(payment.getCurrency()));
		appendVariable(builder, 15, amountStr);
		appendNewLine(builder);

		appendFixed(builder, 4, ":50:");
		appendVariable(builder, 35, formatField(configuration.getPayerName()));
		appendNewLine(builder);
		if (!StringUtils.isBlank(configuration.getPayerStreet())) {
			appendVariable(builder, 35, formatField(configuration.getPayerStreet()));
			appendNewLine(builder);
		}
		if (!StringUtils.isBlank(configuration.getPayerCity())) {
			appendVariable(builder, 35, formatField(configuration.getPayerCity()));
			appendNewLine(builder);
		}
		if (!StringUtils.isBlank(configuration.getPayerCountry())) {
			appendVariable(builder, 35, formatField(configuration.getPayerCountry()));
			appendNewLine(builder);
		}
		
		appendFixed(builder, 5, ":52D:");
		String accountWithLeadingZeroes = addLeadingZeroes(formatField(configuration.getPayerAccountNumber()),16);
		appendFixed(builder, 16, formatField(accountWithLeadingZeroes));
		appendNewLine(builder);
		appendFixed(builder, 16, formatField(accountWithLeadingZeroes));
		appendNewLine(builder);
		appendFixed(builder, 3, formatField(configuration.getPayerAccountCurrency()));
		appendBlank(builder);
		appendFixed(builder, 3, formatField(configuration.getPayerAccountCurrency()));
		appendNewLine(builder);
		appendFixed(builder, 3, formatField(payment.getStatisticalCode()));
		appendBlank(builder);
		appendFixed(builder, 2, formatField(payment.getBankCountry().getIsoCode()));
		appendBlank(builder);
		appendFixed(builder, 2, formatField(payment.getCountry().getIsoCode()));
		appendNewLine(builder);

		appendFixed(builder, 5, ":57A:");
		appendVariable(builder, 34, formatField(payment.getSwift()));
		appendNewLine(builder);

		appendFixed(builder, 5, ":57D:");
		appendVariable(builder, 35, formatField(payment.getBankName()));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(payment.getBankAddress()));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(payment.getBankCity()));
		appendNewLine(builder);
		
		String bankCountry = payment.getBankCountry().getEnglishName();
		if (bankCountry.length() > 35) {bankCountry = payment.getBankCountry().getIsoCode();}
		appendVariable(builder, 35, formatField(bankCountry));
		appendNewLine(builder);

		appendFixed(builder, 4, ":59:");
		builder.append('/');
		appendVariable(builder, 34, formatField(payment.getAccountNumber()));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(payment.getName()));
		appendNewLine(builder);
		if (!StringUtils.isBlank(payment.getAddress())) {
			appendVariable(builder, 35, formatField(payment.getAddress()));
			appendNewLine(builder);
		}
		if (!StringUtils.isBlank(payment.getCity()) || !StringUtils.isBlank(payment.getPostalCode())) {
			String city = payment.getCity() == null ? "" : payment.getCity();
			String postalCode = payment.getPostalCode() == null ? "" : payment.getPostalCode().replaceAll(" ", "");
			String pcWithCity = postalCode + " " + city;
			if (pcWithCity.length() > 35) {
				pcWithCity = city.substring(0, Math.min(35, city.length()));
			}
			appendVariable(builder, 35, formatField(pcWithCity));		
			appendNewLine(builder);
		}
		if (payment.getCountry() != null) {
			String countryName = payment.getCountry().getEnglishName();
			if (countryName.length() > 35) {countryName = payment.getCountry().getIsoCode();}
			appendVariable(builder, 35, countryName);		
			appendNewLine(builder);
		}
				
		appendFixed(builder, 4, ":70:");
		appendVariable(builder, 35, formatField(payment.getTransactionText1()));
		appendNewLine(builder);
		//here are allowed 4 lines but we use only the first one
	
		appendFixed(builder, 5, ":71A:");
		appendFixed(builder, 3, "BN1"); //charge splitting details: BN1 - expenses are shared,  OUR - all expenses are payed by the ordering party, BN2 - all expenses are payed by the beneficiary party (! NOT ALLOWED IN EU)
		appendNewLine(builder);
		
		appendFixed(builder, 4, ":72:");
		appendFixed(builder, 2, "00");
		appendBlank(builder);
		appendFixed(builder, 2, "00");
		appendBlank(builder);
		appendFixed(builder, 2, "00");
		appendBlank(builder);
		appendFixed(builder, 2, "00");
		appendNewLine(builder);
		
		appendFixed(builder, 35, ""); //must be empty - filled with spaces
		appendNewLine(builder);
		appendFixed(builder, 35, ""); //must be empty - filled with spaces
		appendNewLine(builder);
		appendFixed(builder, 35, ""); //must be empty - filled with spaces
		appendNewLine(builder);
		
		//footer
		builder.append("-}");
		
		String multicashContent = builder.toString();
		logger.trace("Package file content: "+multicashContent);
		return multicashContent;
	}
			

	private String addLeadingZeroes(String text, int length) {
		StringBuilder sb = new StringBuilder(text);
		while (sb.length() < length) {
			sb.insert(0, '0');
		}
		return sb.toString();
	}


	private void appendFixed(StringBuilder builder, int length, String text) {
		appendVariable(builder, length, text);
		//add whitespace
		for(int i = text.length(); i < length; i++) {
			builder.append(' ');
		}
	}
	
	private void appendVariable(StringBuilder builder, int maxLength, String text) {
		 if (text.length() > maxLength) {
			 throw new UnicreditConnectorException("Error ocurred while creating multicash package. " +
			 		"The text '"+text+"' is longer as "+maxLength+" characters.");
		 } else {
			 builder.append(text == null ? "" : formatTextToAllowedChars(text));
		 }
	}
	
	private String formatTextToAllowedChars(String text) {
		//Here it is possible to filter characters or transfer them to e.g. ASCII. 
		//However according to my tests the bank accepts UTF-8 without any problems.
		return text;
	}
	
	private String formatField(String text) {
		return text == null ? "" : formatTextToAllowedChars(text.trim());
	}
	
	private void appendNewLine(StringBuilder builder) {
		builder.append(NEWLINE);
	}

	private void appendBlank(StringBuilder builder) {
		builder.append(' ');
	}
}
