package com.newpecunia.unicredit.impl;

public interface GpgFileSigner {

	public byte[] sign(byte[] content);
	
}
