package com.newpecunia.unicredit;

import java.math.BigDecimal;


public class ForeignPayment {
	private BigDecimal amount;
	private String currency;
	
	private String transactionText1;
	private String statisticalCode = "335"; //let's use 335 as default (335 = financial services)
	
	//payee information
	private String name;
	private String address; //street
	private String city;
	private String postalCode;
	private Country country;

	//payee bank information
	private String accountNumber;
	private String swift; //SWIFT / BIC code of the bank
	private String bankName;
	private String bankAddress; //street
	private String bankCity;
	private String bankPostalCode;
	private Country bankCountry;
	
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTransactionText1() {
		return transactionText1;
	}
	public void setTransactionText1(String transactionText1) {
		this.transactionText1 = transactionText1;
	}	
	public String getStatisticalCode() {
		return statisticalCode;
	}
	public void setStatisticalCode(String statisticalCode) {
		this.statisticalCode = statisticalCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getSwift() {
		return swift;
	}
	public void setSwift(String swift) {
		this.swift = swift;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAddress() {
		return bankAddress;
	}
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}
	public String getBankCity() {
		return bankCity;
	}
	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}
	public String getBankPostalCode() {
		return bankPostalCode;
	}
	public void setBankPostalCode(String bankPostalCode) {
		this.bankPostalCode = bankPostalCode;
	}
	public Country getBankCountry() {
		return bankCountry;
	}
	public void setBankCountry(Country bankCountry) {
		this.bankCountry = bankCountry;
	}
	
}
