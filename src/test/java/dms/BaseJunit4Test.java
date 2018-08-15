package dms;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class) // 使用junit4进行测试
@ContextConfiguration(locations = { "classpath:applicationContext.xml" }) // 加载配置文件
@Rollback(value = true) // 默认测试完后数据库操作回滚
@Transactional(transactionManager = "transactionManager")
public class BaseJunit4Test {

}