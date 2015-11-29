package com.newpecunia.unicredit.impl;

import com.newpecunia.unicredit.PackageStatus;

public interface PackageStatusParser {

	PackageStatus parseFile(String statusFile);

}
