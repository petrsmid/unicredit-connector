package com.newpecunia.unicredit.configuration;


public class Credentials {

	private String unicreditWebdavUsername;
	private String unicreditWebdavPassword;
	private String privateSignatureKeyFilePath;
	private String privateKeyPassword;
	
	
	
	public String getUnicreditWebdavUsername() {
		return unicreditWebdavUsername;
	}
	public void setUnicreditWebdavUsername(String unicreditWebdavUsername) {
		this.unicreditWebdavUsername = unicreditWebdavUsername;
	}
	public String getUnicreditWebdavPassword() {
		return unicreditWebdavPassword;
	}
	public void setUnicreditWebdavPassword(String unicreditWebdavPassword) {
		this.unicreditWebdavPassword = unicreditWebdavPassword;
	}
	public String getPrivateSignatureKeyFilePath() {
		return privateSignatureKeyFilePath;
	}
	public void setPrivateSignatureKeyFilePath(String privateSignatureKeyFilePath) {
		this.privateSignatureKeyFilePath = privateSignatureKeyFilePath;
	}
	public String getPrivateKeyPassword() {
		return privateKeyPassword;
	}
	public void setPrivateKeyPassword(String privateKeyPassword) {
		this.privateKeyPassword = privateKeyPassword;
	}
	
		
}