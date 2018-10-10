package dms;

import javax.annotation.Resource;

import org.junit.Test;

import dms.service.PlanService;

public class PlanTest extends BaseJunit4Test {

	@Resource(name = "planService")
	private PlanService planService;

//	@Autowired
//	private TaskDao taskDao;

	@Test
	public void recursion() {
		
	}

	
}
