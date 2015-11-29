package com.newpecunia.unicredit.impl;

import org.junit.Assert;
import org.junit.Test;

import com.newpecunia.unicredit.impl.UnicreditFileNameResolver;

public class UnicreditFileNameResolverTest {
	
	private String SOME_ID = "12345678";
	
	//This is just an visual test that the name is generated correctly. Check it visually.
	@Test
	public void testGetUploadFileNameForId() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();
		String UploadFilename = fnr.getUploadFileNameForId(SOME_ID);
		System.out.println(UploadFilename);
	}
	
	@Test
	public void testGetIdFromStatusFile() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();
		String id = fnr.getIdFromStatusFile("12312123-2013-08-25_16-23-11_"+SOME_ID+".csv.txt");
		Assert.assertEquals(SOME_ID, id);
	}

	@Test
	public void testGetIdFromStatusFileInvalid() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();		
		String id = fnr.getIdFromStatusFile("12312123-2013-08-25-16-23-11-BAD-ID.csv.txt");
		Assert.assertNull(id);
	}
	
	@Test
	public void testGetIdFromUploadFile() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();
		String id = fnr.getIdFromUploadFile("2013-08-25_16-26-21_"+SOME_ID+".csv");
		Assert.assertEquals(SOME_ID, id);
	}
	
	@Test
	public void testGetIdFromUploadFileInvalid() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();		
		String id = fnr.getIdFromUploadFile("2013-08-25-16-26-21-BAD-ID.csv");
		Assert.assertNull(id);
	}
	
	
	@Test
	public void testCreateUploadFileName() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();
		String uploadFileName = fnr.getUploadFileNameForId(SOME_ID);
		String id = fnr.getIdFromUploadFile(uploadFileName);
		
		Assert.assertEquals(id, SOME_ID);		
	}

}
