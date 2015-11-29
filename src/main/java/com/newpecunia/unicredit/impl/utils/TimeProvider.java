package com.newpecunia.unicredit.impl.utils;

import java.util.Date;

/**
 * Time (actual Date and DateTime) is encapsulated into provider in order to be mockable in tests
 */
public interface TimeProvider {
	
	Date nowDate();
}
