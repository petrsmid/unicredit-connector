package com.newpecunia.unicredit;

public final class Country {
	private String isoCode; //two letter code - e.g.: US, AT, CZ, ...
	private String englishName; //e.g. Austria, Czech Republic, ...
	
	public Country(String isoCode, String englishName) {
		super();
		this.isoCode = isoCode;
		this.englishName = englishName;
	}

	public String getIsoCode() {
		return isoCode;
	}
	public String getEnglishName() {
		return englishName;
	}

}