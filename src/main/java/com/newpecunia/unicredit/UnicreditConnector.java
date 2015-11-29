package com.newpecunia.unicredit;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


public interface UnicreditConnector {

	/**
	 * Returns list of IDs of uploaded packages
	 */
	List<String> listForeignUploadedPackages() throws IOException;

	/**
	 * Returns list of IDs of packages with status (in status folder)
	 */
	List<String> listPackagesWithStatus() throws IOException;

	/**
	 * Creates a package with one foreign payment and uploads it
	 * @param reference unique reference of the package  
	 * @return filename of the package
	 * @throws IOException 
	 */
	String uploadForeignPaymentPackage(String reference, ForeignPayment foreignPayment) throws IOException;

	/**
	 * Returns actual status of the uploaded package or null if the status file for the package was not found
	 * @param packageId id of the package
	 */
	PackageStatus getStatusOfPackage(String reference) throws IOException;

	/**
	 * Returns balance found in last statement file
	 * @throws IOException 
	 */
	BigDecimal getLastBalance() throws IOException;


}
