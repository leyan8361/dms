package dms;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import dms.dao.MessageDao;
import dms.service.SysService;

public class SysTest extends BaseJunit4Test {

	@Resource(name = "sysService")
	private SysService sysService;

	@Autowired
	private MessageDao messageDao;

	@Test
	public void test() {

		System.out.println(JSON.toJSONString(messageDao.getAllMessageGroupInfo(),SerializerFeature.DisableCircularReferenceDetect));
	}
}
