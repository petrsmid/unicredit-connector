package com.newpecunia.unicredit.configuration;

public class UsualWebdavConfigForUnicreditSK implements WebdavConfiguration {

	@Override
	public String getWebdavBaseFolder() {
		return "https://sk.unicreditbanking.net/webdav/";
	}

	@Override
	public String getWebdavForeignUploadFolder() {		
		return "upload/foreign/MultiCash%20format/UTF-8/";  // !! Always use folder for uploading files in UTF-8.
	}

	@Override
	public String getWebdavStatusFolder() {
		return "upload/status/";
	}

	@Override
	public String getWebdavStatementsFolder() {		
		return "statements/";
	}

	@Override
	public String getWebdavStatementsFileName() {
		return "statements.txt";
	}

}
