package com.newpecunia.unicredit.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newpecunia.unicredit.PackageStatus;

public class PackageStatusParserImpl implements PackageStatusParser {

	private static final Logger logger = LoggerFactory.getLogger(PackageStatusParserImpl.class);	
	
	@Override
	public PackageStatus parseFile(String statusFile) {
		PackageStatus packageStatus = null;
		String lines[] = statusFile.split("\\r?\\n"); //the status file has either Windows or Unix line endings
		String statusLine = null;
		for (String line : lines) {
			if (line.trim().startsWith("Status")) {
				statusLine = line;
				break;
			}
		}
		if (statusLine == null) {
			logger.error("Could not find status in status file.");
			packageStatus = null;
		} else {
			String statusText = statusLine.substring(statusLine.indexOf(':')+1).trim().toLowerCase();
			switch (statusText) {
			case "error":
				packageStatus = PackageStatus.ERROR;
				break;
			case "signed":
				packageStatus = PackageStatus.SIGNED;
				break;
			default:
				logger.trace("Status found in file: '"+statusText+"' - mapping to PREPARING.");
				packageStatus = PackageStatus.PREPARING;
				break;
			}
		}
		
		return packageStatus;
	}	
}
