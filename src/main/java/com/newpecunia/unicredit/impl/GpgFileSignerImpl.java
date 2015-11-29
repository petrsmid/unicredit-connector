package com.newpecunia.unicredit.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.SignatureException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newpecunia.unicredit.UnicreditConnectorException;
import com.newpecunia.unicredit.configuration.Credentials;
import com.newpecunia.unicredit.impl.pgp.BouncyCastleSigner;
import com.newpecunia.unicredit.impl.utils.TimeProvider;

public class GpgFileSignerImpl implements GpgFileSigner {
	
	private static final Logger logger = LoggerFactory.getLogger(GpgFileSignerImpl.class);	
	
	private Credentials credentials;
	private Provider securityProvider;
	private TimeProvider timeProvider;

	public GpgFileSignerImpl(Credentials credentials, TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
		this.credentials = credentials;
		this.securityProvider = new BouncyCastleProvider();
        Security.addProvider(securityProvider);
	}
	
	@Override
	public byte[] sign(byte[] content) {
		logger.trace("Signing package file");
        try {
			return BouncyCastleSigner.signFile(content, "fakeFileName", new FileInputStream(credentials.getPrivateSignatureKeyFilePath()), 
					credentials.getPrivateKeyPassword().toCharArray(), securityProvider, timeProvider);
		} catch (NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException | IOException
				| PGPException e) {
			
			throw new UnicreditConnectorException("Error ocurred while signing file.", e);
		}
	}

	
	
}
