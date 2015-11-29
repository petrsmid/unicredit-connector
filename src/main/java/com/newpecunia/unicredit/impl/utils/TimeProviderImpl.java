package com.newpecunia.unicredit.impl.utils;

import java.util.Date;

/**
 * Date is encapsulated into this class in order to be easily mockable (e.g. with a stale date) in tests if needed
 */
public class TimeProviderImpl implements TimeProvider {

	@Override
	public Date nowDate() {
		return new Date();
	}

}
