package com.newpecunia.unicredit.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.newpecunia.unicredit.configuration.Credentials;
import com.newpecunia.unicredit.impl.GpgFileSignerImpl;
import com.newpecunia.unicredit.impl.utils.TimeProviderImpl;

public class GpgFileSignerImplTest {
	
	private String certificateFilePath = null;
	
	private String privateTestCertificate = 
			"-----BEGIN PGP PRIVATE KEY BLOCK-----\n"+
			"Version: GnuPG v1.4.11 (GNU/Linux)\n"+
			"\n"+
			"lQOYBFI62esBCACuTmN9GqyVV6NnMHi7RCZj9LGKNurEWcKhaiB4J9GOvjD0aKgo\n"+
			"zPl6CTvf5WQBI/9xQQEXXKUc5w9qaNDDpobsX6WYdYJZKYrqEbTvLidkLr7QiSei\n"+
			"ssaWd9zjdieqyG9/rl1ppMpqRd08DtHfpp5+xWeDg61lazykP0wIat7UxImm5ZhC\n"+
			"zrT0y2JOWr7PpcyDoXvuKw2LZQrtT6i8HTCOm4yp+NTpqvvyiXjOKaeNpC62lYiK\n"+
			"mgzyVUBdRUZlN7aT9RUB3WATHML3vRGubjBAPPITJ7LknPIKfwNoW97Mi+1Dr2kZ\n"+
			"rhAjEGUVgWMROZ7yLPikN60tprOpvjPSuvkpABEBAAEAB/0VcubyL5SaIfklCwVo\n"+
			"QdgZh7RLJjvYYpU9KKKLNpcmYggDI1fCAYsXdchs4jzemQ/nX1Djj45v0o0GkpRR\n"+
			"5w2GS7TEYH1kPGTwLbKelgbZ4Jixb5c1gZtHS8OeJ910ugpiY1HyXIld72/sYoR3\n"+
			"6XhqcB8vQ0ZUixAgw/s2s/PsqXiorBu/uz5chaBGJ52nxgA/TT3Byh7FKQdB7eQe\n"+
			"FzEBIm8yW2db9ddkZ2pifpkPwwtMlGbEB86wL5F0lAiTQGqw4Pu9ZwZgExbSHzPn\n"+
			"tub+gp/36gzLeta+QI51DpkJh8NhHo29ErnKfVCgINAxwCmYrW3x2EUxChCntDIk\n"+
			"y2ipBADOpvtG+Yfd3Iov7CEPZ7lffLvJDwKHerObaD51qp7AroeIbjFQ4m0+gl5d\n"+
			"XWM0uoN2b3KK4nQpI7QfK8MTRqF1WfY/DBAYR98GZgWwK+BswgTQWDYoFQEGFbug\n"+
			"PGfuwiWqPDDpszuw1pHV6rOkQsXhpna0AAtkv6+jzV5QXrn79QQA1+4I7+qZrGP7\n"+
			"BGS8km1YyJlVPtifd4BsOGXHWG6Ck5vYpQYY0j/+Qa0XYozSVbxZHHldhBRIJpCs\n"+
			"OEA02bQJygSo7r1yoF9gPudUTounkKA+wEZUxH0ubenVcW/iKHIdGDnj1Eef2ZYC\n"+
			"oHQiaZJVWW8bFVmNPnfML3gpzStK2+UEAIYRmmZM6nXs2eR5GVH2vHB4wgp+KtJK\n"+
			"rua2uxSAeFH5k7eB2OBaf7rXyPRauOsR8MLlI41rgoTrbGxQzlc2dlRnAd535Glg\n"+
			"KC/EDt1mAL3m9m+oMkwAY8hZSOMfU7tzXG4Xgm6bZ4VJEvPXqQe5gR9AhFOKcUrz\n"+
			"HXwGmhpfWfIINJm0LFRlc3QgVGVzdCAodGVzdCBjZXJ0aWZpY2F0ZSkgPHRlc3RA\n"+
			"dGVzdC5jb20+iQE4BBMBAgAiBQJSOtnrAhsPBgsJCAcDAgYVCAIJCgsEFgIDAQIe\n"+
			"AQIXgAAKCRBxMn6jQN0M/Hu3B/0fJNC3vJoLMuh46MZtuf2v4tJI5eHJIBaQausa\n"+
			"P/DLs6rWz1e34LA5LqeZqwcvYCMCEoH5lAg1PDcZZ5H+qJT40Vf/Zo6iOd6PLBVa\n"+
			"j5s97XL3koOJiry9U9I7LzHgs2C95MUZkERVVdO9ZnQ96EnY6yWu3lDm3GTyOjhc\n"+
			"aLOu37ARIEUK74azZXpjdieLGoHOYLwUOQ6r7XdUmn4YfvUeu1hghH/ifxmKyH5w\n"+
			"seHPJb+7SksZNCsKJPuYeAUYeuCO43azb6n50F/rAxdJoVz/mK6k16TFoKrdjnJy\n"+
			"quLThQl5Tuj4h24SgsYR3WFiXi4aAKxpVD8XQOnfo/AZvmSU\n"+
			"=VZTy\n"+
			"-----END PGP PRIVATE KEY BLOCK-----\n";
	
	private Credentials credentialsMock = new Credentials(){
		@Override
		public String getPrivateSignatureKeyFilePath() {return certificateFilePath;};
		@Override
		public String getPrivateKeyPassword() {return "";}
	};
	
	@Before
	public void setup() throws IOException {
		File tmpFile = File.createTempFile("NPTest", ".asc");
		certificateFilePath = tmpFile.getPath();
		IOUtils.write(privateTestCertificate, new FileOutputStream(tmpFile));
	}
	
	@Test
	public void testSigning() {
		GpgFileSignerImpl signer = new GpgFileSignerImpl(credentialsMock, new TimeProviderImpl());
		byte[] signedBytes = signer.sign("Ahoj".getBytes());
		String signedString = new String(signedBytes);
		
		Assert.assertTrue(signedString.contains("-----BEGIN PGP MESSAGE-----"));
		Assert.assertTrue(signedString.length()>64*8+80);
		Assert.assertTrue(signedString.contains("-----END PGP MESSAGE-----"));
		
		System.out.println(signedString);
	}
	
	@After
	public void teardown() {
		File tmpFile = new File(certificateFilePath);
		tmpFile.delete();
	}

}
