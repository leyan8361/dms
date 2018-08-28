package dms;

import javax.annotation.Resource;

import org.junit.Test;

import dms.service.PlanService;

public class PlanTest extends BaseJunit4Test {

	@Resource(name = "planService")
	private PlanService planService;

	@Test
	public void getProcessContent() {

//		Utils.transStrToIntArr("");
	}
}
