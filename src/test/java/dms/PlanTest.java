package dms;

import java.util.Set;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class PlanTest extends BaseJunit4Test {

	@Test
	public void test() {

		String str = "{\"userId\":\"s\",\"sd\":\"ccc\",\"vfe\":\"ss\"}";
		JSONObject jo = JSONObject.parseObject(str);
		Set<String> keySet = jo.keySet();
		for (String string : keySet) {
			System.out.println(string + ":" + jo.getString(string));
		}
	}
}
