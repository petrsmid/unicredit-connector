package com.newpecunia.unicredit.configuration;


public class PayerConfiguration {
	
	private String PayerAccountCurrency;
	private String payerAccountNumber;
	private String payerName;
	private String payerStreet;
	private String payerCity;
	private String payerCountry;
	private String payerBankSwift;
	private String payerBankSwiftLong;
	
	
	
	public String getPayerAccountCurrency() {
		return PayerAccountCurrency;
	}
	public void setPayerAccountCurrency(String payerAccountCurrency) {
		PayerAccountCurrency = payerAccountCurrency;
	}
	public String getPayerAccountNumber() {
		return payerAccountNumber;
	}
	public void setPayerAccountNumber(String payerAccountNumber) {
		this.payerAccountNumber = payerAccountNumber;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getPayerStreet() {
		return payerStreet;
	}
	public void setPayerStreet(String payerStreet) {
		this.payerStreet = payerStreet;
	}
	public String getPayerCity() {
		return payerCity;
	}
	public void setPayerCity(String payerCity) {
		this.payerCity = payerCity;
	}
	public String getPayerCountry() {
		return payerCountry;
	}
	public void setPayerCountry(String payerCountry) {
		this.payerCountry = payerCountry;
	}
	public String getPayerBankSwift() {
		return payerBankSwift;
	}
	public void setPayerBankSwift(String payerBankSwift) {
		this.payerBankSwift = payerBankSwift;
	}
	public String getPayerBankSwiftLong() {
		return payerBankSwiftLong;
	}
	public void setPayerBankSwiftLong(String payerBankSwiftLong) {
		this.payerBankSwiftLong = payerBankSwiftLong;
	}


}