package com.newpecunia.unicredit;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newpecunia.unicredit.configuration.Credentials;
import com.newpecunia.unicredit.configuration.PayerConfiguration;
import com.newpecunia.unicredit.configuration.UsualWebdavConfigForUnicreditSK;

/**
 * This test demonstrates how to use the Unicredit Connector
 */
public class Example {
	
	private static final Logger logger = LoggerFactory.getLogger(Example.class);	
	

	@Ignore  //this is just an example - we don't want to run the test
	@Test
	public void simpleUsage() throws IOException {
		//start the connector
		UnicreditConnector connector = buildConnector();
				
		//create payment
		ForeignPayment payment = preparePayment(); 
		
		//Pay!
		connector.uploadForeignPaymentPackage("ref_12345", payment);		
	}

	
	@Ignore  //this is just an example - we don't want to run the test
	@Test
	public void detailedPaymentAndStatusUsage() throws IOException {
		UnicreditConnector connector = buildConnector();
		
		//Prepare a payment
		ForeignPayment payment = preparePayment();
		
		//Create package with the payment. (Package = file with payments. Note: this library never puts more than one payment into a file)
		connector.uploadForeignPaymentPackage("Payment12345", payment);
		
		//...after some time...
		
		//Have a look in internet banking which packages have been uploaded
		List<String> uploadedPackages = connector.listForeignUploadedPackages();
		assertTrue(uploadedPackages.contains("Payment12345")); //Cool! The package is really uploaded in the internet banking
		
		//...after some time...
		
		//Have a look in internet banking which packages have been already processed (or are being processed now)
		List<String> packagesWithStatus = connector.listPackagesWithStatus();
		assertTrue(packagesWithStatus.contains("Payment12345")); //Cool! the internet banking started processing the uploaded package
		
		//Get status of one package
		PackageStatus status = connector.getStatusOfPackage("Payment12345");		
		if (status == null) {
			logger.error("The package with the reference has not been found. Maybe the internet banking did not start proces the package yet?");
		} else { 		
			switch(status) {
				case SIGNED: logger.info("Congratulations!!! The package with the payment has been signed and the payment was accepted by the internet banking!"); break;
				
				case PREPARING: logger.info("The package with the payment is being currently processed by the internet banking.");break;
				case PARTLY_SIGNED: logger.info("The package with the payment has been processed but not yet signed by all signers.");break; //applies only in case your account is "multisignature"
				
				case ERROR: logger.error("Some problem ocurred while processing the package with the payment. Check the internet banking to see the details."); break;
			}
		}
	}
	
	@Ignore  //this is just an example - we don't want to run the test
	@Test
	public void balanceUsage() throws IOException {
		UnicreditConnector connector = buildConnector();
		
		BigDecimal balance = connector.getLastBalance(); //reads balance from the last statement file. The information is not up to date. For up-to-date information you would have to buy XXXX product at Unicredit Bank.
		//if balance > 1.000.000  =>  you are rich!
		
	}


	private ForeignPayment preparePayment() {
		ForeignPayment payment = new ForeignPayment();
		
		//let's send 10 Euro
		payment.setAmount(new BigDecimal(10));
		payment.setCurrency("EUR");
		
		payment.setTransactionText1("A gift for children."); 
		
		//payee account information (payee = receiver of the payment)
		payment.setAccountNumber("AT466000000001516500"); //account number
		payment.setSwift("OPSKATWW"); //SWIFT / BIC code of the bank
		
		//payee information
		payment.setName("UNICEF Oesterreich");
		payment.setAddress("Mariahilfer Strasse 176/10"); //street
		payment.setCity("Vienna");
		payment.setPostalCode("1150");
		payment.setCountry(new Country("AT", "Austria"));

		//payee bank information
		payment.setBankName("BAWAG P.S.K.");
		payment.setBankAddress("SEITZERGASSE 2-4"); //street
		payment.setBankCity("Vienna");
		payment.setBankPostalCode("1010");
		payment.setBankCountry(new Country("AT", "Austria"));
		
		return payment;
	}
	
	private UnicreditConnector buildConnector() {
		return UnicreditConnectorFactory.createConnector(
				preparePayerConfig(), 
				new UsualWebdavConfigForUnicreditSK(), 
				prepareCredentials());
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

	private Credentials prepareCredentials() {
		Credentials credentials = new Credentials();
		credentials.setUnicreditWebdavUsername("username");
		credentials.setUnicreditWebdavPassword("password");
		credentials.setPrivateSignatureKeyFilePath("path/to/your/certificate");
		credentials.setPrivateKeyPassword("password_of_certificate");
		
		return credentials;
	}
	
	
}
