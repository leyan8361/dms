package dms;

import javax.annotation.Resource;

import org.junit.Test;

import dms.service.PlanService;
import dms.utils.FilePath;
import dms.utils.Utils;

public class PlanTest extends BaseJunit4Test {

	@Resource(name = "planService")
	private PlanService planService;

	@Test
	public void getProcessContent() {

		String path = FilePath.accidentReportAttachPath + "1535519898571_fontconfig-user.txt";
		System.out.println(Utils.getCreateTime(path));
	}
}
