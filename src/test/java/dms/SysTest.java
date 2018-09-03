package dms;

import javax.annotation.Resource;

import org.junit.Test;

import dms.service.SysService;

public class SysTest extends BaseJunit4Test {

	@Resource(name = "sysService")
	private SysService sysService;
	
	@Test
	public void getPageFunctionList() {
		
		System.out.println(sysService.getPageFunctionList());
	}
}
