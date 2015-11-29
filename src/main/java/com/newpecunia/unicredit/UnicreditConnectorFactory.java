package com.newpecunia.unicredit;

import com.newpecunia.unicredit.configuration.Credentials;
import com.newpecunia.unicredit.configuration.PayerConfiguration;
import com.newpecunia.unicredit.configuration.WebdavConfiguration;
import com.newpecunia.unicredit.impl.GpgFileSignerImpl;
import com.newpecunia.unicredit.impl.PackageStatusParserImpl;
import com.newpecunia.unicredit.impl.StructuredMulticashStatementParser;
import com.newpecunia.unicredit.impl.UnicreditConnectorImpl;
import com.newpecunia.unicredit.impl.utils.TimeProvider;
import com.newpecunia.unicredit.impl.utils.TimeProviderImpl;

public class UnicreditConnectorFactory {

	public static UnicreditConnector createConnector(PayerConfiguration payerConfig, WebdavConfiguration webdavConfig, Credentials credentials) {
		TimeProvider timeProvider = new TimeProviderImpl();
		
		UnicreditConnector connector = new UnicreditConnectorImpl(
				timeProvider, 
				payerConfig, 
				webdavConfig,
				credentials, 
				new GpgFileSignerImpl(credentials, timeProvider), 
				new StructuredMulticashStatementParser(),
				new PackageStatusParserImpl());
		
		return connector;
	}

}
