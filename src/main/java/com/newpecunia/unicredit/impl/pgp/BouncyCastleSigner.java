package com.newpecunia.unicredit.impl.pgp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SignatureException;
import java.util.Iterator;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;

import com.newpecunia.unicredit.impl.utils.TimeProvider;

/**
 * This class is based on example class SignedFileProcessor from Bouncy Castle jar.
 */
public class BouncyCastleSigner {
	
	public static byte[] signFile(byte[] content, String fileName, InputStream keyIn,
			char[] pass, Provider securityProvider, TimeProvider timeProvider) throws IOException, NoSuchAlgorithmException,
			NoSuchProviderException, PGPException, SignatureException {

		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		ArmoredOutputStream armoredOut = new ArmoredOutputStream(byteArrayOut);
		PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator();
		PGPCompressedDataGenerator compressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZLIB);

		try {
			PGPSecretKey pgpSecretKey = BouncyCastlePGPExampleUtil.readSecretKey(keyIn);
			PGPPrivateKey pgpPrivateKey = pgpSecretKey.extractPrivateKey(pass, securityProvider);
			PGPSignatureGenerator signatureGenerator = new PGPSignatureGenerator(pgpSecretKey
					.getPublicKey().getAlgorithm(), PGPUtil.SHA1, securityProvider);
	
			signatureGenerator.initSign(PGPSignature.BINARY_DOCUMENT, pgpPrivateKey);
	
			Iterator<?> it = pgpSecretKey.getPublicKey().getUserIDs();
			if (it.hasNext()) { //get first user ID
				PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();
	
				spGen.setSignerUserID(false, (String) it.next());
				signatureGenerator.setHashedSubpackets(spGen.generate());
			} else {
				throw new PGPException("No user ID found in the public key of the certificate.");
			}
			
	
			BCPGOutputStream bOut = new BCPGOutputStream(compressedDataGenerator.open(armoredOut));
	
			signatureGenerator.generateOnePassVersion(false).encode(bOut);
	
			OutputStream literalOut = literalDataGenerator.open(bOut, PGPLiteralData.BINARY, fileName, timeProvider.nowDate(), content);
	
			literalOut.write(content);
			signatureGenerator.update(content);
	
			signatureGenerator.generate().encode(bOut);

		} finally {
			literalDataGenerator.close();
			compressedDataGenerator.close();
			armoredOut.close();
		}
		
		return byteArrayOut.toByteArray();
	}

}
