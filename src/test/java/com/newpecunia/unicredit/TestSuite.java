package com.newpecunia.unicredit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.unicredit.impl.GpgFileSignerImplTest;
import com.newpecunia.unicredit.impl.MulticashForeignPaymentPackageTest;
import com.newpecunia.unicredit.impl.StatusImplTest;
import com.newpecunia.unicredit.impl.StructuredMulticashStatementParserTest;
import com.newpecunia.unicredit.impl.UnicreditFileNameResolverTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	GpgFileSignerImplTest.class,
	MulticashForeignPaymentPackageTest.class,
	StatusImplTest.class,
	StructuredMulticashStatementParserTest.class,
	UnicreditFileNameResolverTest.class
})	
public class TestSuite {

}
