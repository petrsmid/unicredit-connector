package com.newpecunia.unicredit.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.newpecunia.unicredit.ForeignPayment;
import com.newpecunia.unicredit.PackageStatus;
import com.newpecunia.unicredit.UnicreditConnector;
import com.newpecunia.unicredit.UnicreditConnectorException;
import com.newpecunia.unicredit.configuration.Credentials;
import com.newpecunia.unicredit.configuration.PayerConfiguration;
import com.newpecunia.unicredit.configuration.WebdavConfiguration;
import com.newpecunia.unicredit.impl.utils.TimeProvider;


public class UnicreditConnectorImpl implements UnicreditConnector {

	private static final Logger logger = LoggerFactory.getLogger(UnicreditConnectorImpl.class);	

	private UnicreditFileNameResolver fileNameResolver = new UnicreditFileNameResolver();

	private PayerConfiguration payerConfig;

	private WebdavConfiguration webdavConfig;

	private TimeProvider timeProvider;

	private Credentials credentials;

	private GpgFileSigner gpgFileSigner;
	
	private StructuredMulticashStatementParser structuredMulticashStatementParser;
	
	private PackageStatusParser packageStatusParser;

		

	public UnicreditConnectorImpl(TimeProvider timeProvider, PayerConfiguration payerConfig, WebdavConfiguration webdavConfig, 
			Credentials credentials, GpgFileSigner gpgFileSigner,StructuredMulticashStatementParser structuredMulticashStatementParser,
			PackageStatusParser packageStatusParser) {
		this.timeProvider = timeProvider;
		this.payerConfig = payerConfig;
		this.webdavConfig = webdavConfig;
		this.credentials = credentials;
		this.gpgFileSigner = gpgFileSigner;
		this.structuredMulticashStatementParser = structuredMulticashStatementParser;
		this.packageStatusParser = packageStatusParser;
	}
	
	private Sardine getSardine() {
		return SardineFactory.begin(credentials.getUnicreditWebdavUsername(), credentials.getUnicreditWebdavPassword());
	}
	
	private List<String> listFolder(String url) throws IOException {
		List<String> files = new ArrayList<>();
		List<DavResource> resources = getSardine().list(url);
		for (DavResource res : resources) {
		     files.add(res.getName());
		}
		return files;		
	}
	
	private String getFile(String url) throws IOException {
		InputStream stream = getSardine().get(url);		
		return IOUtils.toString(stream, Charsets.UTF_8);		
	}
	
	private void uploadFile(String url, byte[] data) throws IOException {
		getSardine().put(url, data);
	}
	
	private String getUploadFolderPath() {
		return webdavConfig.getWebdavBaseFolder()+webdavConfig.getWebdavForeignUploadFolder();
	}
	
	private String getStatusFolderPath() {
		return webdavConfig.getWebdavBaseFolder()+webdavConfig.getWebdavStatusFolder();
	}

	private String getStatementsFolderPath() {
		return webdavConfig.getWebdavBaseFolder()+webdavConfig.getWebdavStatementsFolder();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> listForeignUploadedPackages() throws IOException {
		logger.trace("Listing uploaded packages.");
		List<String> uploadedFileNames = listFolder(getUploadFolderPath());
		List<String> ids = new ArrayList<>();
		for (String fileName : uploadedFileNames) {
			String id = fileNameResolver.getIdFromUploadFile(fileName);
			ids.add(id);
		}
		return ids;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public List<String> listPackagesWithStatus() throws IOException {
		logger.trace("Listing status files.");
		List<String> statusFileNames = listFolder(getStatusFolderPath());
		List<String> ids = new ArrayList<>();
		for (String fileName : statusFileNames) {
			String id = fileNameResolver.getIdFromStatusFile(fileName);
			ids.add(id);
		}
		return ids;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String uploadForeignPaymentPackage(String reference, ForeignPayment foreignPayment) throws IOException {
		logger.trace("Uploading package file. Reference: "+reference);
		String multicashReference = checkReferenceLength(reference);
		MulticashForeignPaymentPackage foreignPaymentPackage = new MulticashForeignPaymentPackage(multicashReference, foreignPayment, timeProvider, payerConfig); //one payment per package
		
		String fileName = fileNameResolver.getUploadFileNameForId(reference);
		byte[] data = foreignPaymentPackage.toMultiCashFileContent().getBytes(Charsets.UTF_8);
		byte[] signedData = gpgFileSigner.sign(data);
		uploadFile(fileName, signedData);
		return fileName;
	}
	
	private String checkReferenceLength(String paymentId) {
		String reference = paymentId;
		if (reference.length() > 16) { //reference can be maximally 16 characters long
			throw new UnicreditConnectorException("The reference of a multicash package can be maximally 16 characters long."); 
		}
		return reference;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PackageStatus getStatusOfPackage(String packageId) throws IOException {
		logger.trace("Getting status of package "+packageId);
		List<String> statusFilesNames = listFolder(getStatusFolderPath());
		for (String statusFileName : statusFilesNames) {
			String id = fileNameResolver.getIdFromStatusFile(statusFileName);
			if (packageId.equals(id)) {
				String statusFileContent = getFile(statusFileName);
				return packageStatusParser.parseFile(statusFileContent);
			}
		}
		logger.info("Status file not found for package with ID "+packageId);
		return null;
	}

	private Statement getLastStatement() throws IOException {
		List<String> statementSubFolders = listFolder(getStatementsFolderPath());
		if (!statementSubFolders.isEmpty()) {
			Collections.sort(statementSubFolders);
			String lastSubfolder = statementSubFolders.get(statementSubFolders.size()-1);			
			String statementFileContent = getFile(getStatementsFolderPath()+lastSubfolder+"/"+webdavConfig.getWebdavStatementsFileName());
			return structuredMulticashStatementParser.parseStatements(statementFileContent);
		} else {
			return null;
		}
	}
	
	@Override
	public BigDecimal getLastBalance() throws IOException {
		logger.trace("Getting last balance.");
		Statement lastStatement = getLastStatement();
		if (lastStatement == null) {
			return null;
		} else {
			return lastStatement.getBalance();
		}
	}
}
